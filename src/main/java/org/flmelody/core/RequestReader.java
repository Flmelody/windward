package org.flmelody.core;

/**
 * @author esotericman
 */
public interface RequestReader {
  /**
   * read request body into new object possibly
   *
   * @param clazz objects class
   * @param <I> objects type
   * @return object
   */
  <I> I readJson(String body, Class<I> clazz);

  /**
   * bind request body to specific class. and return instance of the class
   *
   * @param clazz objects class
   * @param groups validate group
   * @param <I> objects type
   * @return object
   */
  <I> I bindJson(String body, Class<I> clazz, Class<?>... groups);
}
