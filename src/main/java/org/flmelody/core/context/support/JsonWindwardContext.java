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

package org.flmelody.core.context.support;

import org.flmelody.core.HttpHeader;
import org.flmelody.core.MediaType;
import org.flmelody.core.WindwardRequest;
import org.flmelody.core.WindwardResponse;
import org.flmelody.core.context.EnhancedWindwardContext;
import org.flmelody.core.exception.ValidationException;

/**
 * @author esotericman
 */
public class JsonWindwardContext extends EnhancedWindwardContext implements HttpKind {
  public JsonWindwardContext(WindwardRequest windwardRequest, WindwardResponse windwardResponse) {
    super(windwardRequest, windwardResponse);
  }

  @Override
  public void doOnRequest() {
    // Check the request header
    for (String header : this.windwardRequest.getHeader(HttpHeader.CONTENT_TYPE)) {
      MediaType mediaType = MediaType.detectMediaType(header);
      if (MediaType.APPLICATION_JSON_VALUE.equals(mediaType)) {
        return;
      }
    }
    throw new ValidationException("Content type must be JSON!");
  }

  @Override
  public <R> void doOnResponse(R r) {
    this.writeJson(r);
  }
}
