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
import io.netty.util.CharsetUtil;
import org.flmelody.core.AbstractResponseWriter;
import org.flmelody.core.MediaType;

/**
 * @author esotericman
 */
public class NettyResponseWriter extends AbstractResponseWriter {
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
    Channel channel = ctx.channel();
    if (!channel.isActive()) {
      return;
    }
    String response = null;
    if (MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
      response = jsonPlugin.toJson(data);

    } else if (MediaType.TEXT_PLAIN_VALUE.equals(contentType)) {
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
