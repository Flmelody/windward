/*
 * Copyright (C) 2023 Flmelody.
 *
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

package org.flmelody.core.netty;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;
import java.util.Collections;
import java.util.Map;
import org.flmelody.core.MediaType;
import org.flmelody.core.ResponseWriter;
import org.flmelody.core.Windward;
import org.flmelody.core.plugin.json.JsonPlugin;

/**
 * @author esotericman
 */
public class NettyResponseWriter implements ResponseWriter {
  private final ChannelHandlerContext ctx;

  public NettyResponseWriter(ChannelHandlerContext ctx) {
    this.ctx = ctx;
  }

  @Override
  public <T> void write(int code, T data) {
    write(code, MediaType.APPLICATION_JSON_VALUE, data);
  }

  @Override
  public <T> void write(int code, String contentType, T data) {
    write(code, contentType, data, true);
  }

  @Override
  public <T> void write(int code, String contentType, T data, boolean close) {
    write(code, contentType, Collections.emptyMap(), data, close);
  }

  @Override
  public <T> void write(
      int code, String contentType, Map<String, Object> headers, T data, boolean close) {
    Channel channel = ctx.channel();
    if (!channel.isActive()) {
      return;
    }
    String response;
    if (MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
      response = Windward.plugin(JsonPlugin.class).toJson(data);
    } else {
      response = String.valueOf(data);
    }
    if (response == null) {
      ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
      return;
    }
    FullHttpResponse httpResponse =
        new DefaultFullHttpResponse(
            HTTP_1_1, OK, Unpooled.copiedBuffer(response, CharsetUtil.UTF_8));
    httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, contentType);
    httpResponse.setStatus(HttpResponseStatus.valueOf(code));
    if (headers != null && !headers.isEmpty()) {
      headers.keySet().forEach(key -> httpResponse.headers().set(key, headers.get(key)));
    }
    if (!close) {
      httpResponse
          .headers()
          .setInt(HttpHeaderNames.CONTENT_LENGTH, httpResponse.content().readableBytes());
      httpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
    }
    ctx.write(httpResponse);
    if (close) {
      close();
    }
  }

  @Override
  public <T> void writeAndClose(int code, String contentType, T data) {
    write(code, contentType, data, Boolean.TRUE);
  }

  @Override
  public void close() {
    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
  }
}
