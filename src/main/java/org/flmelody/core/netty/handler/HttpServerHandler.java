package org.flmelody.core.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.flmelody.core.HttpStatus;
import org.flmelody.core.MediaType;
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
      HttpMethod method = fullHttpRequest.method();
      String uri = fullHttpRequest.uri();
      ByteBuf content = fullHttpRequest.content();
      boolean keepAlive = HttpUtil.isKeepAlive(fullHttpRequest);

      QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri);
      Map<String, List<String>> params = queryStringDecoder.parameters();

      uri = uri.split("\\?")[0];
      if (logger.isDebugEnabled()) {
        logger.debug("Accept request uri {}", uri);
      }
      Object router = Windward.findRouter(uri, method.name());
      if (router instanceof Consumer) {
        @SuppressWarnings("unchecked")
        Consumer<WindwardContext> contextConsumer = (Consumer<WindwardContext>) router;
        WindwardRequest.WindwardRequestBuilder windwardRequestBuilder =
            WindwardRequest.newBuild()
                .method(method.name())
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
        WindwardContext windwardContext =
            new WindwardContext(windwardRequestBuilder.build(), windwardResponseBuild.build());
        contextConsumer.accept(windwardContext);
      } else if (router instanceof Supplier) {
        Supplier<?> supplier = (Supplier<?>) router;
        Object object = supplier.get();
        NettyResponseWriter nettyResponseWriter = new NettyResponseWriter(ctx);
        if (object instanceof Serializable && !(object instanceof String)) {
          nettyResponseWriter.writeAndClose(
              HttpStatus.OK.value(), MediaType.APPLICATION_JSON_VALUE, object);
        } else {
          nettyResponseWriter.writeAndClose(
              HttpStatus.OK.value(), MediaType.TEXT_PLAIN_VALUE, object);
        }
      } else {
        NettyResponseWriter nettyResponseWriter = new NettyResponseWriter(ctx);
        nettyResponseWriter.writeAndClose(
            HttpStatus.NOT_FOUND.value(),
            MediaType.TEXT_PLAIN_VALUE,
            HttpStatus.NOT_FOUND.reasonPhrase());
      }
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    ctx.close();
  }
}
