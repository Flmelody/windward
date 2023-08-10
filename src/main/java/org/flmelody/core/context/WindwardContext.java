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
import org.flmelody.core.HttpStatus;
import org.flmelody.core.WindwardRequest;

/**
 * http context
 *
 * @author esotericman
 */
public interface WindwardContext {

  /**
   * get parameter by name and type
   *
   * @param parameterName parameterName
   * @param <P> class type
   * @return parameter
   */
  <P> P getRequestParameter(String parameterName);

  /**
   * get parameter as list
   *
   * @param parameterName parameterName
   * @return parameters list
   */
  List<String> getRequestParameters(String parameterName);

  /**
   * get request body
   *
   * @return request body
   */
  String getRequestBody();

  /**
   * get windwardRequest
   *
   * @return windwardRequest
   */
  WindwardRequest windwardRequest();

  /** close context */
  void close();

  /**
   * check if current context is already closed
   *
   * @return is closed
   */
  Boolean isClosed();

  /**
   * read request body into new object possibly
   *
   * @param clazz objects class
   * @param <I> objects type
   * @return object
   */
  <I> I readJson(Class<I> clazz);

  /**
   * bind request body to specific class. and return instance of the class
   *
   * @param clazz objects class
   * @param groups validate group
   * @param <I> objects type
   * @return object
   */
  <I> I bindJson(Class<I> clazz, Class<?>... groups);

  /**
   * response json
   *
   * @param data data
   * @param <T> type
   */
  default <T> void writeJson(T data) {
    writeJson(HttpStatus.OK.value(), data);
  }

  /**
   * response json
   *
   * @param code response code
   * @param data data
   * @param <T> type
   */
  <T> void writeJson(int code, T data);

  /**
   * response plain string
   *
   * @param data strings
   */
  default void writeString(String data) {
    writeString(HttpStatus.OK.value(), data);
  }

  /**
   * response plain string
   *
   * @param code response code
   * @param data strings
   */
  void writeString(int code, String data);

  /**
   * redirect
   *
   * @param redirectUrl location for redirecting
   */
  void redirect(String redirectUrl);

  /**
   * redirect
   *
   * @param code http code of redirecting
   * @param redirectUrl location for redirecting
   */
  void redirect(int code, String redirectUrl);
}
