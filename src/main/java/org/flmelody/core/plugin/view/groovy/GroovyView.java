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

package org.flmelody.core.plugin.view.groovy;

import groovy.text.Template;
import groovy.text.markup.MarkupTemplateEngine;
import java.io.StringWriter;
import java.util.Map;
import org.flmelody.core.plugin.view.AbstractViewPlugin;

/**
 * @author esotericman
 */
public class GroovyView extends AbstractViewPlugin {
  protected final MarkupTemplateEngine templateEngine;
  private static final String viewExtension = "tpl";

  public GroovyView() {
    this(null, viewExtension);
  }

  public GroovyView(String templateLocation, String defaultExtension) {
    super(templateLocation, defaultExtension);
    this.templateEngine = new MarkupTemplateEngine();
  }

  @Override
  public String render(String viewUrl, Map<String, Object> model) throws Exception {
    Template template = this.templateEngine.createTemplate(this.getClass().getResource(viewUrl));
    StringWriter stringWriter = new StringWriter();
    template.make(model).writeTo(stringWriter);
    return stringWriter.toString();
  }
}
