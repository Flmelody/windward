package org.flmelody.core;

/**
 * @author esotericman
 */
@FunctionalInterface
public interface HttpServer {
  void run() throws Exception;
}
