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
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import java.util.List;
import org.flmelody.core.MediaType;

/**
 * @author esotericman
 */
public class HttpResponseEncoder extends MessageToMessageEncoder<FullHttpResponse> {

  @Override
  protected void encode(ChannelHandlerContext ctx, FullHttpResponse msg, List<Object> out) {
    WebSocketFrame webSocketFrame;
    if (msg.headers().contains(MediaType.WEB_SOCKET_BINARY)) {
      webSocketFrame = new BinaryWebSocketFrame(msg.content());
    } else {
      webSocketFrame = new TextWebSocketFrame(msg.content());
    }
    out.add(webSocketFrame);
    msg.retain();
  }
}
