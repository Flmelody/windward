package org.flmelody.core.exception;

/**
 * @author esotericman
 */
public class JsonException extends RuntimeException {

  public JsonException(Throwable cause) {
    super(cause);
  }

  public JsonException(String reason) {
    super(reason);
  }
}
