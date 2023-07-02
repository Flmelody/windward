package org.flmelody.core;

/**
 * @author esotericman
 */
public interface RouterGroup extends Router {
  /**
   * find out matched router function
   *
   * @param relativePath relativePath
   * @param method method
   * @return router function
   * @param <R> router type
   */
  <R> R matchRouter(String relativePath, String method);
}
