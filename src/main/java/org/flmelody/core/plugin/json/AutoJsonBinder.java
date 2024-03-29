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

import org.flmelody.core.exception.JsonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author esotericman
 */
public class AutoJsonBinder {
  protected static final Logger logger = LoggerFactory.getLogger(AutoJsonBinder.class);
  public static JsonPlugin jsonPlugin;

  static {
    try {
      Class.forName("com.fasterxml.jackson.databind.ObjectMapper");
      jsonPlugin = new JacksonPlugin();
    } catch (ClassNotFoundException e) {
      logger.atInfo().log("Jackson lib not exist, will try gson instead");
    }
    if (jsonPlugin == null) {
      try {
        Class.forName("com.google.gson.Gson");
        jsonPlugin = new GsonPlugin();
      } catch (ClassNotFoundException e) {
        logger.atInfo().log("Gson lib not found");
      }
    }
    if (jsonPlugin == null) {
      throw new JsonException("No suitable json library");
    }
  }
}
