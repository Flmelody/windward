package org.flmelody.core;

import org.flmelody.core.context.WindwardContext;

/**
 * @author esotericman
 */
public interface Filter {
  void filter(WindwardContext windwardContext);
}
