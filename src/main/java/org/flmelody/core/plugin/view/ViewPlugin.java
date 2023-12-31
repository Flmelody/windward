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

import java.util.Map;
import org.flmelody.core.plugin.Plugin;

/**
 * @author esotericman
 */
interface ViewPlugin extends Plugin {

  /**
   * Whether the extension is supported
   *
   * @param extension resources extension
   * @return whether extension is supported
   */
  boolean supportedExtension(String extension);

  /**
   * Render this template
   *
   * @param viewUrl templates url
   * @param model templates data
   * @return rendered result
   * @throws Exception exception
   */
  String render(String viewUrl, Map<String, Object> model) throws Exception;
}
