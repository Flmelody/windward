package org.flmelody.core.exception;

/**
 * @author esotericman
 */
public class ValidationException extends RuntimeException {

  public ValidationException(String reason) {
    super(reason);
  }
}
