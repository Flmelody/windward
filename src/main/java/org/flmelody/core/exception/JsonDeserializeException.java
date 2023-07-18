package org.flmelody.core.exception;

/**
 * @author esotericman
 */
public class JsonDeserializeException extends RuntimeException {
  public JsonDeserializeException(Throwable cause) {
    super(cause);
  }

  public JsonDeserializeException(String reason) {
    super(reason);
  }
}
