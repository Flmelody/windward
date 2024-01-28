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
import java.util.function.Consumer;
import java.util.stream.Collectors;
import org.flmelody.core.HttpStatus;
import org.flmelody.core.MediaType;
import org.flmelody.core.WindwardRequest;
import org.flmelody.core.context.WindwardContext;
import org.flmelody.core.exception.HandlerNotFoundException;
import org.flmelody.core.exception.WindwardException;
import org.flmelody.util.UrlUtil;

/**
 * @author esotericman
 */
public class BaseStaticResourcePlugin implements ResourcePlugin, Consumer<WindwardContext> {
  protected final String[] staticResourceLocations;

  public BaseStaticResourcePlugin(String[] staticResourceLocations) {
    this.staticResourceLocations = staticResourceLocations;
  }

  @Override
  public final void accept(WindwardContext windwardContext) {
    WindwardRequest windwardRequest = windwardContext.windwardRequest();
    String fileUri = windwardRequest.getUri();
    StaticResource staticResource = StaticResource.newBuilder().fileUri(fileUri).build();
    String resource = findResource(staticResource);
    Path path = new File(staticResource.getFileUri()).toPath();
    String mimeType;
    try {
      mimeType = Files.probeContentType(path);
    } catch (IOException e) {
      throw new WindwardException(e);
    }
    windwardContext.write(
        HttpStatus.OK.value(), MediaType.detectMediaType(mimeType).value, resource);
  }

  protected String findResource(StaticResource staticResource) {
    for (String staticResourceLocation : staticResourceLocations) {
      String fileUri = staticResource.getFileUri();
      if (!fileUri.startsWith(staticResourceLocation)) {
        StaticResource.newBuilder(staticResource)
            .fileUri(UrlUtil.buildUrl(staticResourceLocation, fileUri));
      }
      try (InputStream in = this.getClass().getResourceAsStream(staticResource.getFileUri())) {
        // NULL or directory, we don't return it.
        if (in == null || in instanceof ByteArrayInputStream) {
          continue;
        }
        return new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))
            .lines()
            .collect(Collectors.joining("\n"));
      } catch (IOException e) {
        throw new WindwardException(e);
      }
    }
    throw new HandlerNotFoundException("No matched resource!");
  }
}
