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

package org.flmelody.core.plugin.resource;

import org.flmelody.core.Windward;
import org.flmelody.core.context.WindwardContext;

/**
 * @author esotericman
 */
public final class ResourcePluginProxy extends BaseStaticResourcePlugin {
  private static final ResourcePluginProxy RESOURCE_PROXY = new ResourcePluginProxy();
  private volatile BaseStaticResourcePlugin resourcePlugin;
  private final Object resourceLock = new Object();

  private ResourcePluginProxy() {}

  public static ResourcePluginProxy current() {
    return RESOURCE_PROXY;
  }

  @Override
  public void accept(WindwardContext windwardContext) {
    if (resourcePlugin == null) {
      synchronized (resourceLock) {
        if (resourcePlugin == null) {
          BaseStaticResourcePlugin plugin = Windward.plugin(BaseStaticResourcePlugin.class);
          plugin.mappedResources.putAll(mappedResources);
          resourcePlugin = plugin;
        }
      }
    }
    resourcePlugin.accept(windwardContext);
  }
}
