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

package org.flmelody.core.plugin.resolver;

import org.flmelody.core.Windward;
import org.flmelody.core.plugin.Plugin;
import org.flmelody.core.plugin.view.AbstractViewPlugin;

/**
 * @author esotericman
 */
public class ViewPluginResolver implements PluginResolver {
  @Override
  public void resolve(Windward windward, Plugin plugin) {
    if (plugin instanceof AbstractViewPlugin) {
      ((AbstractViewPlugin) plugin).setTemplateLocationPrefix(windward.getTemplateRoot());
    }
  }
}
