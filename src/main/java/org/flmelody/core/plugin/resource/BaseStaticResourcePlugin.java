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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.flmelody.core.HttpStatus;
import org.flmelody.core.MediaType;
import org.flmelody.core.WindwardRequest;
import org.flmelody.core.context.ResourceWindwardContext;
import org.flmelody.core.context.WindwardContext;
import org.flmelody.core.exception.ResourceNotFoundException;
import org.flmelody.core.exception.WindwardException;
import org.flmelody.util.UrlUtil;

/**
 * @author esotericman
 */
public class BaseStaticResourcePlugin implements ResourcePlugin, Consumer<WindwardContext> {

  protected final Map<String, String> mappedResources = new LinkedHashMap<>();

  @Override
  public void accept(WindwardContext windwardContext) {
    if (!(windwardContext instanceof ResourceWindwardContext)) {
      windwardContext.write(
          HttpStatus.INTERNAL_SERVER_ERROR.value(), MediaType.APPLICATION_JSON_VALUE.value, null);
      return;
    }
    ResourceWindwardContext resourceWindwardContext = (ResourceWindwardContext) windwardContext;
    WindwardRequest windwardRequest = resourceWindwardContext.windwardRequest();
    String fileUri = windwardRequest.getUri();
    StaticResource staticResource = StaticResource.newBuilder().fileUri(fileUri).build();
    String resource =
        findResource(mappedResources.get(resourceWindwardContext.getMatchedPath()), staticResource);
    Path path = new File(staticResource.getFileUri()).toPath();
    String mimeType;
    try {
      mimeType = Files.probeContentType(path);
    } catch (IOException e) {
      throw new WindwardException(e);
    }
    resourceWindwardContext.write(
        HttpStatus.OK.value(), MediaType.detectMediaType(mimeType).value, resource);
  }

  protected String findResource(String staticResourceLocation, StaticResource staticResource) {
    String fileUri = staticResource.getFileUri();
    if (!fileUri.startsWith(staticResourceLocation)) {
      StaticResource.newBuilder(staticResource)
          .fileUri(UrlUtil.buildUrl(staticResourceLocation, fileUri));
    }
    try (InputStream in = this.getClass().getResourceAsStream(staticResource.getFileUri())) {
      // NULL or directory, we don't return it.
      if (in == null || in instanceof ByteArrayInputStream) {
        throw new ResourceNotFoundException("No matched resource!");
      }
      boolean validResource = fileUri.matches(".*\\.[a-z|A-Z]+$");
      if (!validResource && in.available() <= 0) {
        throw new ResourceNotFoundException("No matched resource!");
      }
      return new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))
          .lines()
          .collect(Collectors.joining("\n"));
    } catch (IOException e) {
      throw new WindwardException(e);
    }
  }

  @Override
  public final ResourcePlugin mappingResource(
      String staticResourceLocation, String... pathPatterns) {
    if (pathPatterns != null) {
      for (String pathPattern : pathPatterns) {
        mappedResources.put(pathPattern, staticResourceLocation);
      }
    }
    return this;
  }
}
