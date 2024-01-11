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

package org.flmelody.core.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.AttributeKey;
import org.flmelody.core.ws.WebSocketEvent;
import org.flmelody.core.ws.WebSocketFireEvent;

/**
 * @author esotericman
 */
public class WebSocketHandler extends ChannelInboundHandlerAdapter {
  static final AttributeKey<Boolean> MULTIPLE_SUBSCRIBER =
      AttributeKey.newInstance("windward_multiple_subscriber");

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    if (msg instanceof WebSocketFrame) {
      if (msg instanceof CloseWebSocketFrame) {
        ctx.fireUserEventTriggered(
            WebSocketFireEvent.builder()
                .reset()
                .event(WebSocketEvent.ON_CLOSE)
                .data(msg.toString())
                .build());
      } else if (msg instanceof TextWebSocketFrame) {
        ctx.fireUserEventTriggered(
            WebSocketFireEvent.builder()
                .reset()
                .event(WebSocketEvent.ON_MESSAGE)
                .data(((TextWebSocketFrame) msg).text())
                .build());
      } else {
        // BinaryWebSocketFrame
        // PingWebSocketFrame
        // PongWebSocketFrame
        ctx.fireUserEventTriggered(
            WebSocketFireEvent.builder()
                .reset()
                .event(WebSocketEvent.ON_MESSAGE)
                .data(msg)
                .build());
      }
      boolean b = ctx.channel().hasAttr(MULTIPLE_SUBSCRIBER);
      if (b) {
        ((WebSocketFrame) msg).retain();
        ctx.fireChannelRead(msg);
      }
    }
  }
}
