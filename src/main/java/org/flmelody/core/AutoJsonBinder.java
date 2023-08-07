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
    try {
      Class.forName("com.google.gson.Gson");
      jsonPlugin = new GsonPlugin();
    } catch (ClassNotFoundException e) {
      LOGGER.info("Gson lib not find, error throw");
    }
    if (jsonPlugin == null) {
      throw new JsonException("No suitable json library");
    }
  }

}
