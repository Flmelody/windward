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

package org.flmelody.core.plugin.view.freemarker;

import freemarker.template.Configuration;
import java.io.StringWriter;
import java.util.Map;
import org.flmelody.core.plugin.view.AbstractViewPlugin;

/**
 * @author esotericman
 */
public class FreemarkerView extends AbstractViewPlugin {
  private final Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);

  {
    configuration.setClassForTemplateLoading(FreemarkerView.class, "/");
  }

  private static final String viewExtension = "ftl";

  public FreemarkerView() {
    this(null, viewExtension);
  }

  public FreemarkerView(String templateLocationPrefix, String defaultExtension) {
    super(templateLocationPrefix, defaultExtension);
  }

  @Override
  public String render(String viewUrl, Map<String, Object> model) throws Exception {
    StringWriter stringWriter = new StringWriter();
    configuration.getTemplate(viewUrl).process(model, stringWriter);
    return stringWriter.toString();
  }
}
