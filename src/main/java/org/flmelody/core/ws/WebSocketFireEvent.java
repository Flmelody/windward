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

/**
 * @author esotericman
 */
public class WebSocketFireEvent {

  private WebSocketEvent event;
  private Object data;

  public WebSocketEvent getEvent() {
    return event;
  }

  public Object getData() {
    return data;
  }

  private WebSocketFireEvent() {}

  public static WebSocketFireEventBuilder builder() {
    return new WebSocketFireEventBuilder(new WebSocketFireEvent());
  }

  /** */
  public static class WebSocketFireEventBuilder {
    private final WebSocketFireEvent webSocketFireEvent;

    private WebSocketFireEventBuilder(WebSocketFireEvent webSocketFireEvent1) {
      this.webSocketFireEvent = webSocketFireEvent1;
    }

    public WebSocketFireEvent build() {
      return this.webSocketFireEvent;
    }

    public WebSocketFireEventBuilder reset() {
      webSocketFireEvent.event = null;
      webSocketFireEvent.data = null;
      return this;
    }

    public WebSocketFireEventBuilder event(WebSocketEvent event) {
      webSocketFireEvent.event = event;
      return this;
    }

    public WebSocketFireEventBuilder data(Object data) {
      webSocketFireEvent.data = data;
      return this;
    }
  }
}
