package org.flmelody.core;

import org.flmelody.core.context.WindwardContext;

/**
 * @author esotericman
 */
public class FunctionMetaInfo<I> {
  private final I function;
  private final Class<? extends WindwardContext> clazz;

  public FunctionMetaInfo(I function, Class<? extends WindwardContext> clazz) {
    this.function = function;
    this.clazz = clazz;
  }

  public I getFunction() {
    return function;
  }

  public Class<? extends WindwardContext> getClazz() {
    return clazz;
  }
}
