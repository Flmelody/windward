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

package org.flmelody.core.plugin.ws;

import java.util.List;
import org.flmelody.core.ws.WebSocketParser;
import org.flmelody.core.ws.codec.WebSocketCodec;

/**
 * Holder of websockets codecs or parsers. This plugin is globally scoped.
 *
 * @author esotericman
 */
public abstract class ExtensionalWebSocketPlugin implements WebSocketPlugin {
  private final List<WebSocketCodec> webSocketCodecs;
  private final List<WebSocketParser> webSocketParsers;

  protected ExtensionalWebSocketPlugin(
      List<WebSocketCodec> webSocketCodecs, List<WebSocketParser> webSocketParsers) {
    this.webSocketCodecs = webSocketCodecs;
    this.webSocketParsers = webSocketParsers;
  }

  public List<WebSocketCodec> getWebSocketCodecs() {
    return webSocketCodecs;
  }

  public List<WebSocketParser> getWebSocketParsers() {
    return webSocketParsers;
  }
}
