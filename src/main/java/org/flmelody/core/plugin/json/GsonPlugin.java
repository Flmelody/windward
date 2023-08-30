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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import org.flmelody.core.exception.JsonDeserializeException;
import org.flmelody.core.exception.JsonSerializeException;

/**
 * @author esotericman
 */
public class GsonPlugin implements JsonPlugin {
  final Gson gson;

  {
    gson = new GsonBuilder().serializeNulls().create();
  }

  /** {@inheritDoc} */
  @Override
  public <I> String toJson(I data) {
    try {
      // whether string is json string already
      if (data instanceof String) {
        String result = String.valueOf(data);
        try {
          gson.getAdapter(JsonElement.class).fromJson(result);
          return result;
        } catch (Exception ignored) {
          // do nothing
        }
      }
      return gson.toJson(data);
    } catch (Exception e) {
      throw new JsonSerializeException(e);
    }
  }

  /** {@inheritDoc} */
  @Override
  public <O> O toObject(String json, Class<O> clazz) {
    try {
      return gson.fromJson(json, clazz);
    } catch (Exception e) {
      throw new JsonDeserializeException(e);
    }
  }

  /** {@inheritDoc} */
  @Override
  public <I, O> O toObject(I data, Type type) {
    //noinspection unchecked
    return (O) gson.fromJson(toJson(data), TypeToken.get(type));
  }
}
