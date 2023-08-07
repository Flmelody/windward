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
