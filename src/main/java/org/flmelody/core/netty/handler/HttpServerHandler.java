/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import java.util.function.Function;
import java.util.function.Supplier;
import org.flmelody.core.ExceptionHandler;
import org.flmelody.core.Filter;
import org.flmelody.core.FunctionMetaInfo;
import org.flmelody.core.HttpStatus;
import org.flmelody.core.Windward;
import org.flmelody.core.WindwardRequest;
import org.flmelody.core.WindwardResponse;
import org.flmelody.core.context.EmptyWindwardContext;
import org.flmelody.core.context.EnhancedWindwardContext;
import org.flmelody.core.context.SimpleWindwardContext;
import org.flmelody.core.context.WindwardContext;
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
      String uri = fullHttpRequest.uri().split("\\?")[0];
      FunctionMetaInfo<?> functionMetaInfo =
          Windward.findRouter(uri, fullHttpRequest.method().name());
      WindwardContext windwardContext = initContext(ctx, fullHttpRequest, functionMetaInfo);
      handle(functionMetaInfo, windwardContext);
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

  private <I> WindwardContext initContext(
      ChannelHandlerContext ctx,
      FullHttpRequest fullHttpRequest,
      FunctionMetaInfo<I> functionMetaInfo) {
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
    if (functionMetaInfo == null) {
      windwardResponseBuild
          .build()
          .write(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.reasonPhrase());
    } else {
      try {
        Class<? extends WindwardContext> clazz = functionMetaInfo.getClazz();
        if (clazz.isAssignableFrom(SimpleWindwardContext.class)) {
          return new SimpleWindwardContext(
              windwardRequestBuilder.pathVariables(functionMetaInfo.getPathVariables()).build(),
              windwardResponseBuild.build());
        } else if (clazz.isAssignableFrom(EnhancedWindwardContext.class)) {
          return new EnhancedWindwardContext(
              windwardRequestBuilder.pathVariables(functionMetaInfo.getPathVariables()).build(),
              windwardResponseBuild.build());
        }
      } catch (Exception e) {
        logger.error("Failed to construct context");
      }
    }
    return new EmptyWindwardContext();
  }

  private void handle(FunctionMetaInfo<?> functionMetaInfo, WindwardContext windwardContext) {
    if (windwardContext.isClosed()) {
      return;
    }
    for (Filter filter : Windward.filters()) {
      try {
        filter.filter(windwardContext);
      } catch (Exception e) {
        logger.error("Handler error", e);
        windwardContext.writeString(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase());
        windwardContext.close();
        return;
      }
    }
    execute(functionMetaInfo, windwardContext);
  }

  private void execute(FunctionMetaInfo<?> functionMetaInfo, WindwardContext windwardContext) {
    try {
      Object function = functionMetaInfo.getFunction();
      if (function instanceof Consumer) {
        @SuppressWarnings("unchecked")
        final Consumer<WindwardContext> contextConsumer = (Consumer<WindwardContext>) function;
        contextConsumer.accept(windwardContext);
      } else if (function instanceof Function) {
        @SuppressWarnings("unchecked")
        final Function<WindwardContext, ?> contextConsumer =
            (Function<WindwardContext, ?>) function;
        contextConsumer.apply(windwardContext);
      } else if (function instanceof Supplier) {
        Supplier<?> supplier = (Supplier<?>) function;
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
      if (!handleException(windwardContext, e)) {
        windwardContext.writeString(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase());
      }
    }
  }

  private boolean handleException(WindwardContext windwardContext, Exception e) {
    List<ExceptionHandler> exceptionHandlers = Windward.exceptionHandlers();
    boolean alreadyDone = false;
    for (ExceptionHandler exceptionHandler : exceptionHandlers) {
      try {
        if (exceptionHandler.supported(e)) {
          exceptionHandler.handle(windwardContext);
          alreadyDone = true;
        }
      } catch (Exception exception) {
        alreadyDone = false;
        logger.error("Handle exception error", e);
      }
    }
    return alreadyDone;
  }
}
