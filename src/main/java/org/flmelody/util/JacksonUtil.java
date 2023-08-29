/*
 * Copyright 2023 Flmelody.org
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

package org.flmelody.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import org.flmelody.core.exception.JsonDeserializeException;
import org.flmelody.core.exception.JsonSerializeException;

/**
 * @author esotericman
 */
public class JacksonUtil {
  static final ObjectMapper objectMapper = new ObjectMapper();

  static {
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  private JacksonUtil() {}

  /**
   * convert data into json string
   *
   * @param data data
   * @param <I> type of data
   * @return json string
   */
  public static <I> String toJson(I data) {
    try {
      return objectMapper.writeValueAsString(data);
    } catch (JsonProcessingException e) {
      throw new JsonSerializeException(e);
    }
  }

  /**
   * convert json string into specific class
   *
   * @param json json string
   * @param clazz class
   * @param <O> type of class
   * @return converted object
   */
  public static <O> O toObject(String json, Class<O> clazz) {
    try {
      return objectMapper.readValue(json, clazz);
    } catch (JsonProcessingException e) {
      throw new JsonDeserializeException(e);
    }
  }

  public static <I> HashMap<String, Object> toMap(I data) {
    //noinspection unchecked
    return objectMapper.convertValue(data, HashMap.class);
  }
}
