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

package org.flmelody.core.context;

import java.lang.reflect.Type;

/**
 * @author esotericman
 */
public interface HttpBasicWindwardContext extends WindwardContext {
  /**
   * Read request body into new object possibly
   *
   * @param clazz objects class
   * @param <I> objects type
   * @return object
   */
  <I> I readJson(Class<I> clazz);

  /**
   * Read request body into new object possibly
   *
   * @param type objects type
   * @param <I> objects type
   * @return object
   */
  <I> I readJson(Type type);

  /**
   * Bind request body to specific class. and return instance of the class
   *
   * @param clazz objects class
   * @param groups validate group
   * @param <I> objects type
   * @return object
   */
  <I> I bindJson(Class<I> clazz, Class<?>... groups);

  /**
   * Bind request body to specific class. and return instance of the class
   *
   * @param type objects type
   * @param groups validate group
   * @param <I> objects type
   * @return object
   */
  <I> I bindJson(Type type, Class<?>... groups);

  /**
   * Redirect the client to a new address
   *
   * @param redirectUrl location for redirecting
   */
  void redirect(String redirectUrl);

  /**
   * Redirect the client to a new address
   *
   * @param code http code of redirecting
   * @param redirectUrl location for redirecting
   */
  void redirect(int code, String redirectUrl);

  /**
   * Response html directly
   *
   * @param viewUrl templates location
   * @param model templates data
   * @param <M> views model
   */
  <M> void html(String viewUrl, M model);
}
