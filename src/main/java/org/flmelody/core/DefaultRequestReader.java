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
