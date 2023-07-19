package org.flmelody.core;

import org.flmelody.core.context.WindwardContext;

/**
 * @author esotericman
 */
public interface ExceptionHandler {
  void handle(WindwardContext windwardContext);

  boolean supported(Exception e);
}
