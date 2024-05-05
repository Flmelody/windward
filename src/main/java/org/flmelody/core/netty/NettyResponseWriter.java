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

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
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
  private final boolean keepConnection;

  public NettyResponseWriter(ChannelHandlerContext ctx, boolean keepConnection) {
    this.ctx = ctx;
    this.keepConnection = keepConnection;
  }

  @Override
  public <T> void write(int code, T data) {
    write(code, MediaType.APPLICATION_JSON_VALUE.value, data);
  }

  @Override
  public <T> void write(int code, String contentType, T data) {
    write(code, contentType, data, !keepConnection);
  }

  @Override
  public <T> void write(int code, String contentType, Map<String, Object> headers, T data) {
    write(code, contentType, headers, data, !keepConnection);
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
    MediaType mediaType = MediaType.detectMediaType(contentType);
    ByteBuf response = resolveRawResponse(mediaType, data);
    FullHttpResponse httpResponse = new DefaultFullHttpResponse(HTTP_1_1, OK, response);
    httpResponse.setStatus(HttpResponseStatus.valueOf(code));
    paddingHeaders(httpResponse, mediaType, headers, close);
    ctx.writeAndFlush(httpResponse);
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
    if (ctx.channel().isActive()) {
      ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }
  }

  // Processing the raw response
  private <T> ByteBuf resolveRawResponse(MediaType mediaType, T rawResponse) {
    ByteBuf response;
    if (rawResponse == null) {
      response = Unpooled.EMPTY_BUFFER;
    } else {
      if (MediaType.APPLICATION_JSON_VALUE.equals(mediaType)) {
        response =
            Unpooled.copiedBuffer(
                Windward.plugin(JsonPlugin.class).toJson(rawResponse), CharsetUtil.UTF_8);
      } else {
        response = Unpooled.copiedBuffer(String.valueOf(rawResponse), CharsetUtil.UTF_8);
      }
    }
    return response;
  }

  // Padding headers value for response
  private void paddingHeaders(
      FullHttpResponse httpResponse,
      MediaType mediaType,
      Map<String, Object> headers,
      boolean close) {
    HttpHeaders httpHeaders = httpResponse.headers();
    httpHeaders.set(HttpHeaderNames.CONTENT_TYPE, mediaType.value);
    if (headers != null && !headers.isEmpty()) {
      headers.keySet().forEach(key -> httpHeaders.set(key, headers.get(key)));
    }
    if (!MediaType.TEXT_EVENT_STREAM_VALUE.equals(mediaType)) {
      httpHeaders.setInt(HttpHeaderNames.CONTENT_LENGTH, httpResponse.content().readableBytes());
    }
    if (!close) {
      httpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
    }
  }
}
