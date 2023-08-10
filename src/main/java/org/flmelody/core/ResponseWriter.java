/*
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
public interface ResponseWriter {
  /**
   * write response data
   *
   * @param code http code
   * @param data data
   * @param <T> data type
   */
  <T> void write(int code, T data);

  /**
   * write response data
   *
   * @param code http code
   * @param contentType contentType
   * @param data data
   * @param <T> data type
   */
  <T> void write(int code, String contentType, T data);

  /**
   * write response data
   *
   * @param code http code
   * @param contentType contentType
   * @param data data
   * @param close close connection
   * @param <T> data type
   */
  <T> void write(int code, String contentType, T data, boolean close);

  /**
   * write response data
   *
   * @param code http code
   * @param contentType contentType
   * @param data data
   * @param <T> data type
   */
  <T> void write(int code, String contentType, Map<String, Object> headers, T data, boolean close);

  /**
   * write response data
   *
   * @param code http code
   * @param contentType contentType
   * @param data data
   * @param <T> data type
   */
  <T> void writeAndClose(int code, String contentType, T data);

  /** close connection */
  void close();
}
