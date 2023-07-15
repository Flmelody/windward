package org.flmelody.core;

import org.flmelody.core.exception.ServerException;

/**
 * @author esotericman
 */
@FunctionalInterface
public interface HttpServer {
  void run() throws ServerException;
}
