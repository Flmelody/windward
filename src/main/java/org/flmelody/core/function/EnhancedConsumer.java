package org.flmelody.core.function;

/**
 * @author esotericman
 */
@FunctionalInterface
public interface EnhancedConsumer<T, R> {

  /**
   * Performs this operation on the given argument.
   *
   * @param t the input argument
   */
  R accept(T t) throws Exception;
}
