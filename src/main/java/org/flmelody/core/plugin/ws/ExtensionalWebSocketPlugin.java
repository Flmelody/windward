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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.flmelody.core.Order;
import org.flmelody.core.ws.WebSocketParser;
import org.flmelody.core.ws.codec.WebSocketCodec;
import org.flmelody.util.AntPathMatcher;

/**
 * Holder of websockets codecs or parsers. This plugin is globally scoped. Note that currently at
 * least one codec and one message parser are included for this plugin to work.
 *
 * @see WebSocketCodec
 * @see WebSocketParser
 * @author esotericman
 */
public class ExtensionalWebSocketPlugin implements WebSocketPlugin {
  private final AntPathMatcher pathMatcher = AntPathMatcher.newBuild().withIgnoreCase().build();
  protected final String pattern;
  private final List<WebSocketCodec> webSocketCodecs;
  private final List<WebSocketParser<?>> webSocketParsers;

  public ExtensionalWebSocketPlugin() {
    this(Collections.emptyList(), Collections.emptyList());
  }

  public ExtensionalWebSocketPlugin(
      List<WebSocketCodec> webSocketCodecs, List<WebSocketParser<?>> webSocketParsers) {
    this("/**", webSocketCodecs, webSocketParsers);
  }

  public ExtensionalWebSocketPlugin(
      String pattern,
      List<WebSocketCodec> webSocketCodecs,
      List<WebSocketParser<?>> webSocketParsers) {
    if (pattern == null || pattern.isEmpty()) {
      throw new IllegalArgumentException("Pattern can't be empty");
    }
    this.pattern = pattern;
    if (webSocketCodecs == null) {
      this.webSocketCodecs = Collections.emptyList();
    } else {
      this.webSocketCodecs =
          webSocketCodecs.stream()
              .sorted(Comparator.comparingInt(Order::getOrder))
              .collect(Collectors.toList());
    }
    if (webSocketParsers == null) {
      this.webSocketParsers = Collections.emptyList();
    } else {
      this.webSocketParsers =
          webSocketParsers.stream()
              .sorted(Comparator.comparingInt(Order::getOrder))
              .collect(Collectors.toList());
    }
  }

  public boolean isMatch(String path) {
    return this.pathMatcher.isMatch(pattern, path);
  }

  public List<WebSocketCodec> getWebSocketCodecs() {
    return webSocketCodecs;
  }

  public List<WebSocketParser<?>> getWebSocketParsers() {
    return webSocketParsers;
  }
}
