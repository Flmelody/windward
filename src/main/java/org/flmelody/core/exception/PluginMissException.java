package org.flmelody.core.exception;

/**
 * @author esotericman
 */
public class PluginMissException extends RuntimeException {
  public PluginMissException(Throwable cause) {
    super(cause);
  }

  public PluginMissException(String reason) {
    super(reason);
  }
}
