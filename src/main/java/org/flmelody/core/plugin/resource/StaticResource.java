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

/**
 * @author esotericman
 */
public class StaticResource {
  private String fileUri;

  private StaticResource() {}

  public static StaticResourceBuilder newBuilder() {
    return newBuilder(new StaticResource());
  }

  public static StaticResourceBuilder newBuilder(StaticResource staticResource) {
    return new StaticResourceBuilder(staticResource);
  }

  public String getFileUri() {
    return fileUri;
  }

  /** Builder for StaticResource */
  public static class StaticResourceBuilder {
    private final StaticResource staticResource;

    private StaticResourceBuilder(StaticResource staticResource) {
      this.staticResource = staticResource;
    }

    public StaticResourceBuilder fileUri(String fileUri) {
      staticResource.fileUri = fileUri;
      return this;
    }

    public StaticResource build() {
      return staticResource;
    }
  }
}
