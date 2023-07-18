package org.flmelody.core.exception;

/**
 * @author esotericman
 */
public class JsonSerializeException extends RuntimeException {

  public JsonSerializeException(Throwable cause) {
    super(cause);
  }

  public JsonSerializeException(String reason) {
    super(reason);
  }
}
