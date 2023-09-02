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

import java.util.ArrayList;
import java.util.List;
import org.flmelody.core.Windward;
import org.flmelody.core.plugin.Plugin;

/**
 * @author esotericman
 */
public class CompositePluginResolver implements PluginResolver {
  private final List<PluginResolver> pluginResolvers = new ArrayList<>();

  public CompositePluginResolver() {
    registerResolver(new ViewPluginResolver());
  }

  @Override
  public void resolve(Windward windward, Plugin plugin) {
    pluginResolvers.forEach(pluginResolver -> pluginResolver.resolve(windward, plugin));
  }

  public void registerResolver(PluginResolver pluginResolver) {
    pluginResolvers.add(pluginResolver);
  }
}
