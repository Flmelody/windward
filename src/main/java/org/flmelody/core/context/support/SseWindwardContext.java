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

import java.util.HashMap;
import java.util.Map;
import org.flmelody.core.HttpStatus;
import org.flmelody.core.MediaType;
import org.flmelody.core.SseEjector;
import org.flmelody.core.SseEventSource;
import org.flmelody.core.WindwardRequest;
import org.flmelody.core.WindwardResponse;
import org.flmelody.core.context.EnhancedWindwardContext;

/**
 * @author esotericman
 */
public class SseWindwardContext extends EnhancedWindwardContext implements HttpKind {
  private static final Map<String, Object> headers = new HashMap<>();

  static {
    headers.put("Cache-Control", "no-cache");
    headers.put("Connection", "keep-alive");
  }

  public SseWindwardContext(WindwardRequest windwardRequest, WindwardResponse windwardResponse) {
    super(windwardRequest, windwardResponse);
  }

  @Override
  protected void doOnRequest() {}

  @Override
  protected <R> void doOnResponse(R r) {
    if (r instanceof SseEjector) {
      windwardResponse.write(
          HttpStatus.OK.value(),
          MediaType.TEXT_EVENT_STREAM_VALUE.value,
          headers,
          SseEventSource.builder().comment("").build());
    } else {
      windwardResponse.write(
          HttpStatus.OK.value(), MediaType.TEXT_EVENT_STREAM_VALUE.value, headers, r);
    }
  }

  public <T> void send(T data) {
    doOnResponse(data);
  }
}
