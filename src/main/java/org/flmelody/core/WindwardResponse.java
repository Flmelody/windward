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

package org.flmelody.core;

import java.util.Map;

/**
 * @author esotericman
 */
public class WindwardResponse {
  private ResponseWriter responseWriter;

  /**
   * write data into response
   *
   * @param code http code
   * @param data data
   * @param <T> data type
   */
  public <T> void write(int code, T data) {
    responseWriter.write(code, data);
  }

  /**
   * write data into response
   *
   * @param code http code
   * @param contentType response contentType
   * @param data data
   * @param <T> data type
   */
  public <T> void write(int code, String contentType, T data) {
    responseWriter.write(code, contentType, data);
  }

  /**
   * write data into response
   *
   * @param code http code
   * @param contentType response contentType
   * @param headers responses headers
   * @param data data
   * @param <T> data type
   */
  public <T> void write(int code, String contentType, Map<String, Object> headers, T data) {
    responseWriter.write(code, contentType, headers, data);
  }

  /** close connection */
  public void close() {
    responseWriter.close();
  }

  public static WindwardResponseBuild newBuilder() {
    return new WindwardResponseBuild(new WindwardResponse());
  }

  private WindwardResponse() {}

  /** builder for WindwardResponse */
  public static class WindwardResponseBuild {
    private final WindwardResponse windwardResponse;

    private WindwardResponseBuild(WindwardResponse windwardResponse) {
      this.windwardResponse = windwardResponse;
    }

    public WindwardResponseBuild responseWriter(ResponseWriter responseWriter) {
      this.windwardResponse.responseWriter = responseWriter;
      return this;
    }

    public WindwardResponse build() {
      return this.windwardResponse;
    }
  }
}
