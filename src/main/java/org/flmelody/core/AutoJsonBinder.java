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

package org.flmelody.core;

import org.flmelody.core.exception.JsonException;
import org.flmelody.core.plugin.json.GsonPlugin;
import org.flmelody.core.plugin.json.JacksonPlugin;
import org.flmelody.core.plugin.json.JsonPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author esotericman
 */
public class AutoJsonBinder {
  protected static final Logger LOGGER = LoggerFactory.getLogger(AutoJsonBinder.class);
  public static JsonPlugin jsonPlugin;

  static {
    try {
      Class.forName("com.fasterxml.jackson.databind.ObjectMapper");
      jsonPlugin = new JacksonPlugin();
    } catch (ClassNotFoundException e) {
      LOGGER.info("Jackson lib not exist, will try gson instead");
    }
    if (jsonPlugin == null) {
      try {
        Class.forName("com.google.gson.Gson");
        jsonPlugin = new GsonPlugin();
      } catch (ClassNotFoundException e) {
        LOGGER.info("Gson lib not find, error throw");
      }
    }
    if (jsonPlugin == null) {
      throw new JsonException("No suitable json library");
    }
  }
}
