package org.flmelody.core.plugin.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author esotericman
 */
public class ViewEngineDetector {
  public static boolean AVAILABLE_GROOVY_ENGINE;
  public static boolean AVAILABLE_THYMELEAF_ENGINE;

  private static final Logger logger = LoggerFactory.getLogger(ViewEngineDetector.class);

  static {
    try {
      Class.forName("groovy.text.markup.MarkupTemplateEngine");
      AVAILABLE_GROOVY_ENGINE = true;
    } catch (ClassNotFoundException e) {
      AVAILABLE_GROOVY_ENGINE = false;
      logger.info("Unavailable groovy template due to missed groovy engine");
    }
    try {
      Class.forName("org.thymeleaf.TemplateEngine");
      AVAILABLE_THYMELEAF_ENGINE = true;
    } catch (ClassNotFoundException e) {
      AVAILABLE_THYMELEAF_ENGINE = false;
      logger.info("Unavailable thymeleaf template due to missed thymeleaf engine");
    }
  }
}
