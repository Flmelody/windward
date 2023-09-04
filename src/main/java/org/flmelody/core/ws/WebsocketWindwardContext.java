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

import org.flmelody.core.WindwardRequest;
import org.flmelody.core.WindwardResponse;
import org.flmelody.core.context.AbstractWindwardContext;

/**
 * @author esotericman
 */
public class WebsocketWindwardContext extends AbstractWindwardContext {
  private WebsocketEvent websocketEvent;
  private boolean alreadyDispatched;

  public WebsocketWindwardContext(
      WindwardRequest windwardRequest, WindwardResponse windwardResponse) {
    super(windwardRequest, windwardResponse);
  }

  public void setWebsocketEvent(WebsocketEvent websocketEvent) {
    if (alreadyDispatched) {
      return;
    }
    this.websocketEvent = websocketEvent;
    alreadyDispatched = true;
  }

  public WebsocketEvent getWebsocketEvent() {
    return websocketEvent;
  }

  public Object getMessage() {
    return getRequestBody();
  }

  /**
   * send any data
   *
   * @param data any data or raw data
   * @param <T> data type
   */
  public <T> void writeAnyData(T data) {}

  @Override
  public Boolean isCacheable() {
    return Boolean.TRUE;
  }
}
