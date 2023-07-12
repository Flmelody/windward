package org.flmelody.core.exception;

/**
 * @author esotericman
 */
public class ValidationException extends Throwable {

  public ValidationException(String reason) {
    super(reason);
  }
}
