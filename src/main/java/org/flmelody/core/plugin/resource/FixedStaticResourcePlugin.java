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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.flmelody.core.exception.HandlerNotFoundException;
import org.flmelody.util.UrlUtil;

/**
 * @author esotericman
 */
public class FixedStaticResourcePlugin extends BaseStaticResourcePlugin {
  private final Set<String> fixedPages = new HashSet<>(Collections.singletonList("index.html"));

  public FixedStaticResourcePlugin(String[] staticResourceLocations) {
    super(staticResourceLocations);
  }

  // Add custom page yourself
  public void appendPages(String... pageNames) {
    if (pageNames != null) {
      this.fixedPages.addAll(Arrays.asList(pageNames));
    }
  }

  @Override
  protected String findResource(StaticResource staticResource) {
    try {
      return super.findResource(staticResource);
    } catch (HandlerNotFoundException e) {
      for (String fixedPage : fixedPages) {
        String fileUri = staticResource.getFileUri();
        // Wrong or lost resource
        boolean ignoredResource = fileUri.matches("\\.[a-z|A-Z]+$");
        if (ignoredResource) {
          throw e;
        }
        int index = fileUri.lastIndexOf(UrlUtil.SLASH);
        if (index < 0) {
          try {
            return super.findResource(
                StaticResource.newBuilder(staticResource).fileUri(fixedPage).build());
          } catch (Exception ignored) {
            // Nothing here
          }
        }
        try {
          fileUri = UrlUtil.buildUrl(fileUri, fixedPage);
          return super.findResource(
              StaticResource.newBuilder(staticResource).fileUri(fileUri).build());
        } catch (Exception ignored) {
          // Nothing here
        }
      }
    }
    throw new HandlerNotFoundException("No matched resource!");
  }
}
