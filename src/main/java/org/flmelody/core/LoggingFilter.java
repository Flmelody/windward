package org.flmelody.core;

import org.flmelody.core.context.WindwardContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author esotericman
 */
public class LoggingFilter implements Filter {
  private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

  @Override
  public void filter(WindwardContext windwardContext) {
    logger.info("Accepted request {}", windwardContext.windwardRequest().getUri());
  }
}
