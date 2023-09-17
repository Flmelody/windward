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

package org.flmelody.core.ws;

import org.flmelody.core.HttpStatus;
import org.flmelody.core.MediaType;
import org.flmelody.core.WindwardRequest;
import org.flmelody.core.WindwardResponse;
import org.flmelody.core.context.AbstractWindwardContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author esotericman
 */
public class WebSocketWindwardContext extends AbstractWindwardContext {
  private static final Logger logger = LoggerFactory.getLogger(WebSocketWindwardContext.class);
  private WebSocketEvent webSocketEvent;
  private Object webSocketData;
  private boolean upgradedContext;

  public WebSocketWindwardContext(
      WindwardRequest windwardRequest, WindwardResponse windwardResponse) {
    super(windwardRequest, windwardResponse);
  }

  public void setWebSocketEvent(WebSocketEvent webSocketEvent) {
    this.webSocketEvent = webSocketEvent;
    upgradeContext();
  }

  public void setWebSocketData(Object webSocketData) {
    this.webSocketData = webSocketData;
  }

  public WebSocketEvent getWebSocketEvent() {
    return webSocketEvent;
  }

  public Object getWebSocketData() {
    return webSocketData;
  }

  public boolean isUpgradedContext() {
    return upgradedContext;
  }

  private void upgradeContext() {
    if (WebSocketEvent.ON_CONNECT.equals(webSocketEvent)) {
      upgradedContext = true;
    }
  }

  @Override
  public <T> void writeJson(T data) {
    if (processCheck()) {
      super.writeJson(data);
    }
  }

  @Override
  public <T> void writeJson(int code, T data) {
    if (processCheck()) {
      super.writeJson(code, data);
    }
  }

  @Override
  public void writeString(String data) {
    if (processCheck()) {
      super.writeString(data);
    }
  }

  @Override
  public void writeString(int code, String data) {
    if (processCheck()) {
      super.writeString(code, data);
    }
  }

  /**
   * send any data
   *
   * @param data any data or raw data
   * @param <T> data type
   */
  public <T> void writeBinaryData(T data) {
    if (processCheck()) {
      windwardResponse.write(HttpStatus.OK.value(), MediaType.WEB_SOCKET_BINARY, data);
    }
  }

  /**
   * Make sure context upgraded already which means {@link WebSocketEvent#ON_CONNECT} already done.
   * In general,we only need to access context like below:
   *
   * <pre>{@code
   *  switch (webSocketWindwardContext.getWebSocketEvent()) {
   *   case ON_CONNECT:
   *     webSocketWindwardContext.writeString("Hello World!");
   *     break;
   *   case ON_MESSAGE:
   *     Object webSocketData =
   *         webSocketWindwardContext.getWebSocketData(); // webSocketFrame or just strings
   *     webSocketWindwardContext.writeString("Oh?");
   *     break;
   *   default:
   * }
   *
   * }</pre>
   *
   * @return result
   */
  private boolean processCheck() {
    if (upgradedContext) {
      return true;
    }
    logger.warn("Context not upgraded!");
    return false;
  }

  @Override
  public Boolean isCacheable() {
    return Boolean.TRUE;
  }
}
