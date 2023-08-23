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

package org.flmelody.view.thymeleaf;

import java.util.Map;
import org.flmelody.view.View;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * @author esotericman
 */
public class ThymeleafView implements View {
  private final TemplateEngine templateEngine;

  public ThymeleafView() {
    this.templateEngine = new TemplateEngine();
    this.templateEngine.setTemplateResolver(new ClassLoaderTemplateResolver());
  }

  @Override
  public String render(String viewUrl, Map<String, Object> model) throws Exception {
    Context context = new Context();
    context.setVariables(model);
    return templateEngine.process(viewUrl, context);
  }
}
