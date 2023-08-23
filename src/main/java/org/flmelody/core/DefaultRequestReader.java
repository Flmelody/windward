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

import org.flmelody.core.exception.ValidationException;
import org.flmelody.core.plugin.json.JsonPlugin;
import org.flmelody.util.ValidationUtil;

/**
 * @author esotericman
 */
public class DefaultRequestReader implements RequestReader {

  @Override
  public <I> I readJson(String body, Class<I> clazz) {
    if (body == null) {
      return Windward.plugin(JsonPlugin.class).toObject("{}", clazz);
    }
    return Windward.plugin(JsonPlugin.class).toObject(body, clazz);
  }

  @Override
  public <I> I bindJson(String body, Class<I> clazz, Class<?>... groups) {
    if (body == null) {
      throw new ValidationException("Body is empty");
    }
    return ValidationUtil.validate(Windward.plugin(JsonPlugin.class), body, clazz, groups);
  }
}
