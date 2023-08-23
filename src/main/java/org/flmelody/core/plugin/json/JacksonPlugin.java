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

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flmelody.core.exception.JsonDeserializeException;
import org.flmelody.core.exception.JsonSerializeException;

/**
 * @author esotericman
 */
public class JacksonPlugin implements JsonPlugin {
  final ObjectMapper objectMapper = new ObjectMapper();

  {
    objectMapper
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(DeserializationFeature.FAIL_ON_TRAILING_TOKENS, true);
  }

  @Override
  public <I> String toJson(I data) {
    try {
      // whether string is json string already
      if (data instanceof String) {
        try {
          String result = String.valueOf(data);
          objectMapper.readTree(result);
          return result;
        } catch (JacksonException ignored) {
          // do nothing
        }
      }
      return objectMapper.writeValueAsString(data);
    } catch (JsonProcessingException e) {
      throw new JsonSerializeException(e);
    }
  }

  @Override
  public <O> O toObject(String json, Class<O> clazz) {
    try {
      return objectMapper.readValue(json, clazz);
    } catch (JsonProcessingException e) {
      throw new JsonDeserializeException(e);
    }
  }
}
