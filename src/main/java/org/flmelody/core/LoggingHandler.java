package org.flmelody.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author esotericman
 */
public class LoggingHandler implements Handler {
  private static final Logger logger = LoggerFactory.getLogger(LoggingHandler.class);

  @Override
  public void invoke(WindwardContext windwardContext) {
    logger.info("Accepted request {}", windwardContext.windwardRequest().getUri());
  }
}
