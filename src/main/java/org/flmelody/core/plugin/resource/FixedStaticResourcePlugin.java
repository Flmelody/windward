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
import java.util.StringTokenizer;
import org.flmelody.core.exception.HandlerNotFoundException;
import org.flmelody.core.exception.ResourceNotFoundException;
import org.flmelody.util.UrlUtil;

/**
 * @author esotericman
 */
public class FixedStaticResourcePlugin extends BaseStaticResourcePlugin {
  private final Set<String> fixedPages = new HashSet<>(Collections.singletonList("index.html"));
  // If false, always try to return fixed page if resource not found exception occurred
  private final boolean ignoredPattern;

  public FixedStaticResourcePlugin() {
    this(true);
  }

  public FixedStaticResourcePlugin(boolean ignoredPattern) {
    this.ignoredPattern = ignoredPattern;
  }

  // Add custom page yourself
  public void appendPages(String... pageNames) {
    if (pageNames != null) {
      this.fixedPages.addAll(Arrays.asList(pageNames));
    }
  }

  @Override
  protected String findResource(String staticResourceLocation, StaticResource staticResource) {
    try {
      return super.findResource(staticResourceLocation, staticResource);
    } catch (HandlerNotFoundException e) {
      String originalUri = staticResource.getFileUri();
      for (String fixedPage : fixedPages) {
        String fileUri = originalUri;
        // Wrong or lost resource
        if (ignoredPattern) {
          boolean ignoredResource = fileUri.matches(pattern);
          if (ignoredResource) {
            throw e;
          }
        }
        int index = fileUri.lastIndexOf(UrlUtil.SLASH);
        if (index < 0) {
          try {
            return super.findResource(
                staticResourceLocation,
                StaticResource.newBuilder(staticResource).fileUri(fixedPage).build());
          } catch (Exception ignored) {
            // Nothing here
          }
        }
        try {
          fileUri = UrlUtil.buildUrl(fileUri, fixedPage);
          return super.findResource(
              staticResourceLocation,
              StaticResource.newBuilder(staticResource).fileUri(fileUri).build());
        } catch (Exception ignored) {
          StringTokenizer stringTokenizer = new StringTokenizer(originalUri, UrlUtil.SLASH);
          String possibleUri = UrlUtil.SLASH;
          while (stringTokenizer.hasMoreTokens()) {
            possibleUri = UrlUtil.buildUrl(possibleUri, stringTokenizer.nextToken());
            // Like /static/endpoint is okay
            if (possibleUri.chars().filter(i -> i == '/').count() > 1
                && !possibleUri.equals(originalUri)) {
              try {
                return super.findResource(
                    staticResourceLocation,
                    StaticResource.newBuilder(staticResource)
                        .fileUri(UrlUtil.buildUrl(possibleUri, fixedPage))
                        .build());
              } catch (Exception ignoredException) {
                // Do nothing
              }
            }
          }
        }
      }
    }
    throw new ResourceNotFoundException("No matched resource!");
  }
}
