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
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.flmelody.core.ws.WebSocketEvent;
import org.flmelody.core.ws.WebSocketFireEvent;

/**
 * @author esotericman
 */
public class SocketTailHandler extends ChannelInboundHandlerAdapter {
  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
      ctx.pipeline()
          .addBefore(
              ctx.pipeline().context(HttpServerHandler.class).name(),
              HttpResponseEncoder.class.getSimpleName(),
              new HttpResponseEncoder());
      ctx.pipeline()
          .fireUserEventTriggered(
              WebSocketFireEvent.builder().reset().event(WebSocketEvent.ON_CONNECT).build());
    } else {
      super.userEventTriggered(ctx, evt);
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    ctx.fireUserEventTriggered(
        WebSocketFireEvent.builder().reset().event(WebSocketEvent.ON_ERROR).build());
    super.exceptionCaught(ctx, cause);
  }
}
