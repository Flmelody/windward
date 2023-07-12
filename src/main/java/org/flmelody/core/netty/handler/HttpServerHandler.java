package org.flmelody.core.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.flmelody.core.Handler;
import org.flmelody.core.HttpStatus;
import org.flmelody.core.Windward;
import org.flmelody.core.WindwardContext;
import org.flmelody.core.WindwardRequest;
import org.flmelody.core.WindwardResponse;
import org.flmelody.core.netty.NettyResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author esotericman
 */
public class HttpServerHandler extends SimpleChannelInboundHandler<Object> {
  private static final Logger logger = LoggerFactory.getLogger(HttpServerHandler.class);

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    ctx.flush();
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
    if (msg instanceof FullHttpRequest) {
      FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
      WindwardContext windwardContext = initContext(ctx, fullHttpRequest);
      Object router =
          Windward.findRouter(
              windwardContext.windwardRequest().getUri(), fullHttpRequest.method().name());
      for (Handler handler : Windward.handlers()) {
        try {
          handler.invoke(windwardContext);
        } catch (Exception e) {
          logger.error("Handler error", e);
          windwardContext.writeString(
              HttpStatus.INTERNAL_SERVER_ERROR.value(),
              HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase());
          windwardContext.close();
          return;
        }
      }
      try {
        if (router instanceof Consumer) {
          @SuppressWarnings("unchecked")
          final Consumer<WindwardContext> contextConsumer = (Consumer<WindwardContext>) router;
          contextConsumer.accept(windwardContext);
        } else if (router instanceof Supplier) {
          Supplier<?> supplier = (Supplier<?>) router;
          Object object = supplier.get();
          if (object instanceof Serializable && !(object instanceof String)) {
            windwardContext.writeJson(object);
          } else {
            windwardContext.writeString(object.toString());
          }
        } else {
          windwardContext.writeString(
              HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.reasonPhrase());
        }
      } catch (Exception e) {
        logger.error("Error occurred", e);
        windwardContext.writeString(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase());
      }
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    ctx.close();
  }

  private Map<String, List<String>> prepareHeaders(HttpHeaders httpHeaders) {
    Iterator<Map.Entry<String, String>> entryIterator = httpHeaders.iteratorAsString();
    Map<String, List<String>> headers = new HashMap<>();
    while (entryIterator.hasNext()) {
      Map.Entry<String, String> next = entryIterator.next();
      if (headers.containsKey(next.getKey())) {
        headers.get(next.getKey()).add(next.getValue());
      } else {
        ArrayList<String> values = new ArrayList<>();
        values.add(next.getValue());
        headers.put(next.getKey(), values);
      }
    }
    return headers;
  }

  private WindwardContext initContext(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) {
    String uri = fullHttpRequest.uri();
    ByteBuf content = fullHttpRequest.content();
    boolean keepAlive = HttpUtil.isKeepAlive(fullHttpRequest);
    QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri);
    Map<String, List<String>> params = queryStringDecoder.parameters();

    uri = uri.split("\\?")[0];
    WindwardRequest.WindwardRequestBuilder windwardRequestBuilder =
        WindwardRequest.newBuild()
            .headers(prepareHeaders(fullHttpRequest.headers()))
            .method(fullHttpRequest.method().name())
            .uri(uri)
            .keepAlive(keepAlive)
            .querystring(params);
    if (content.isReadable()) {
      String string = content.toString(CharsetUtil.UTF_8);
      windwardRequestBuilder.requestBody(string);
    }
    WindwardResponse.WindwardResponseBuild windwardResponseBuild =
        WindwardResponse.newBuilder()
            .keepConnection(keepAlive)
            .responseWriter(new NettyResponseWriter(ctx));
    return new WindwardContext(windwardRequestBuilder.build(), windwardResponseBuild.build());
  }
}
