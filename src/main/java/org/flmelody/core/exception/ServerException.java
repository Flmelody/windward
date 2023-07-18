package org.flmelody.core.exception;

/**
 * @author esotericman
 */
public class ServerException extends RuntimeException {

  public ServerException(String reason) {
    super(reason);
  }
}
