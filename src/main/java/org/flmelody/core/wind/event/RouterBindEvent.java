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

package org.flmelody.core.wind.event;

/**
 * @author esotericman
 */
public class RouterBindEvent implements Event {
  private String requestUrl;
  private String classname;
  private String method;

  private RouterBindEvent() {}

  public static RouterBindEventBuilder builder() {
    return new RouterBindEventBuilder(new RouterBindEvent());
  }

  public String getRequestUrl() {
    return requestUrl;
  }

  public String getClassname() {
    return classname;
  }

  public String getMethod() {
    return method;
  }

  /** Builder for RouterBindEvent */
  public static class RouterBindEventBuilder {
    private final RouterBindEvent routerBindEvent;

    private RouterBindEventBuilder(RouterBindEvent routerBindEvent) {
      this.routerBindEvent = routerBindEvent;
    }

    public RouterBindEventBuilder requestUrl(String requestUrl) {
      this.routerBindEvent.requestUrl = requestUrl;
      return this;
    }

    public RouterBindEventBuilder classname(String classname) {
      this.routerBindEvent.classname = classname;
      return this;
    }

    public RouterBindEventBuilder method(String method) {
      this.routerBindEvent.method = method;
      return this;
    }

    public RouterBindEvent build() {
      if (this.routerBindEvent.classname == null || this.routerBindEvent.method == null) {
        throw new IllegalArgumentException(
            "RouterBindEvent should contain both classname and method's name !");
      }
      return this.routerBindEvent;
    }
  }
}
