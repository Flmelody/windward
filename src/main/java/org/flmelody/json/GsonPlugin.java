package org.flmelody.json;

/**
 * @author esotericman
 */
public class GsonPlugin implements JsonPlugin {
  @Override
  public <I> String toJson(I data) {
    return null;
  }

  @Override
  public <O> O toObject(String json, Class<O> clazz) {
    return null;
  }
}
