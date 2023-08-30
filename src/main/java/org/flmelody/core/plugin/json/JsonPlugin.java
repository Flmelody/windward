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

package org.flmelody.core.plugin.json;

import java.lang.reflect.Type;
import org.flmelody.core.plugin.Plugin;

/**
 * base interface for serializing or deserializing json
 *
 * @author esotericman
 */
public interface JsonPlugin extends Plugin {

  /**
   * convert data into json string
   *
   * @param data data
   * @param <I> type of data
   * @return json string
   */
  <I> String toJson(I data);

  /**
   * convert json string into specific class
   *
   * @param json json string
   * @param clazz class
   * @param <O> type of class
   * @return converted object
   */
  <O> O toObject(String json, Class<O> clazz);

  /**
   * convert json string into specific type
   *
   * @param data data
   * @param type type
   * @param <O> type
   * @return converted object
   */
  <I, O> O toObject(I data, Type type);
}
