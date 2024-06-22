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

package org.flmelody.core.context;

import java.util.List;
import java.util.Map;
import org.flmelody.core.HttpStatus;
import org.flmelody.core.WindwardRequest;

/**
 * Http context
 *
 * @author esotericman
 */
public interface WindwardContext {

  /**
   * Get parameter by name and type
   *
   * @param parameterName parameterName
   * @param <P> class type
   * @return parameter
   */
  <P> P getRequestParameter(String parameterName);

  /**
   * Get parameter as list
   *
   * @param parameterName parameterName
   * @return parameters list
   */
  List<String> getRequestParameters(String parameterName);

  /**
   * Get path variables
   *
   * @return path variables
   */
  Map<String, Object> getPathVariables();

  /**
   * Get request body
   *
   * @return request body
   */
  String getRequestBody();

  /**
   * Get windwardRequest
   *
   * @return windwardRequest
   */
  WindwardRequest windwardRequest();

  /** Close context */
  void close();

  /**
   * Check if current context is already closed
   *
   * @return is closed
   */
  boolean isClosed();

  /**
   * Whether context can be cached
   *
   * @return cacheable flag
   */
  default boolean isCacheable() {
    return false;
  }

  /**
   * Response data
   *
   * @param contentType contentType
   * @param data data
   * @param <T> type
   */
  default <T> void write(String contentType, T data) {
    write(HttpStatus.OK.value(), contentType, data);
  }

  /**
   * Response data
   *
   * @param code response code
   * @param contentType contentType
   * @param data data
   * @param <T> type
   */
  <T> void write(int code, String contentType, T data);

  /**
   * Response json
   *
   * @param data data
   * @param <T> type
   */
  default <T> void writeJson(T data) {
    writeJson(HttpStatus.OK.value(), data);
  }

  /**
   * Response json
   *
   * @param code response code
   * @param data data
   * @param <T> type
   */
  <T> void writeJson(int code, T data);

  /**
   * Response plain string
   *
   * @param data strings
   */
  default void writeString(String data) {
    writeString(HttpStatus.OK.value(), data);
  }

  /**
   * Response plain string
   *
   * @param code response code
   * @param data strings
   */
  void writeString(int code, String data);
}
