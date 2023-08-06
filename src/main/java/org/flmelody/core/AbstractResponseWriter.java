package org.flmelody.core;

import org.flmelody.core.exception.JsonException;
import org.flmelody.json.GsonPlugin;
import org.flmelody.json.JacksonPlugin;
import org.flmelody.json.JsonPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author esotericman
 */
public abstract class AbstractResponseWriter implements ResponseWriter {
  protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
  protected JsonPlugin jsonPlugin;

  {
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
