package org.flmelody.core;

import org.flmelody.core.context.WindwardContext;

import java.util.Map;

/**
 * @author esotericman
 */
public class FunctionMetaInfo<I> {
  private final I function;
  private final Class<? extends WindwardContext> clazz;
  private final Map<String, Object> pathVariables;

  public FunctionMetaInfo(
      I function, Class<? extends WindwardContext> clazz, Map<String, Object> pathVariables) {
    this.function = function;
    this.clazz = clazz;
    this.pathVariables = pathVariables;
  }

  public I getFunction() {
    return function;
  }

  public Class<? extends WindwardContext> getClazz() {
    return clazz;
  }

  public Map<String, Object> getPathVariables() {
    return pathVariables;
  }
}
