/*
 * Copyright (C) 2023 Flmelody.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.flmelody.core.plugin.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author esotericman
 */
public class ViewEngineDetector {
  public static boolean AVAILABLE_GROOVY_ENGINE;
  public static boolean AVAILABLE_THYMELEAF_ENGINE;
  public static boolean AVAILABLE_FREEMARKER_ENGINE;

  private static final Logger logger = LoggerFactory.getLogger(ViewEngineDetector.class);

  static {
    try {
      Class.forName("groovy.text.markup.MarkupTemplateEngine");
      AVAILABLE_GROOVY_ENGINE = true;
    } catch (ClassNotFoundException e) {
      AVAILABLE_GROOVY_ENGINE = false;
      logger.atInfo().log(
          "Missing Groovy template engine dependency, Groovy templates not available");
    }
    try {
      Class.forName("org.thymeleaf.TemplateEngine");
      AVAILABLE_THYMELEAF_ENGINE = true;
    } catch (ClassNotFoundException e) {
      AVAILABLE_THYMELEAF_ENGINE = false;
      logger.atInfo().log(
          "Missing Thymeleaf template engine dependency, Thymeleaf templates not available");
    }
    try {
      Class.forName("freemarker.template.Configuration");
      AVAILABLE_FREEMARKER_ENGINE = true;
    } catch (ClassNotFoundException e) {
      AVAILABLE_FREEMARKER_ENGINE = false;
      logger.atInfo().log(
          "Missing Freemarker template engine dependency, Freemarker templates not available");
    }
  }
}
