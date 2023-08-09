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
package org.flmelody.core.plugin.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

  @Override
  public <I> String toJson(I data) {
    try {
      return gson.toJson(data);
    } catch (Exception e) {
      throw new JsonSerializeException(e);
    }
  }

  @Override
  public <O> O toObject(String json, Class<O> clazz) {
    try {
      return gson.fromJson(json, clazz);
    } catch (Exception e) {
      throw new JsonDeserializeException(e);
    }
  }
}
