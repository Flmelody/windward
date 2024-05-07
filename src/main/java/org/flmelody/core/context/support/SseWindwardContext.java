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
import org.flmelody.core.HttpHeader;
import org.flmelody.core.HttpHeaderValue;
import org.flmelody.core.HttpStatus;
import org.flmelody.core.MediaType;
import org.flmelody.core.WindwardRequest;
import org.flmelody.core.WindwardResponse;
import org.flmelody.core.context.EnhancedWindwardContext;
import org.flmelody.core.sse.SseChunkTail;

/**
 * @author esotericman
 */
public class SseWindwardContext extends EnhancedWindwardContext implements HttpKind {
  private static final Map<String, Object> headers = new HashMap<>();

  static {
    headers.put(HttpHeader.CACHE_CONTROL, HttpHeaderValue.NO_CACHE);
    headers.put(HttpHeader.CONNECTION, HttpHeaderValue.KEEP_ALIVE);
    headers.put(HttpHeader.TRANSFER_ENCODING, HttpHeaderValue.CHUNKED);
  }

  public SseWindwardContext(WindwardRequest windwardRequest, WindwardResponse windwardResponse) {
    super(windwardRequest, windwardResponse);
  }

  @Override
  protected void doOnRequest() {}

  @Override
  protected <R> void doOnResponse(R r) {
    // No response here?
  }

  public <T> void send(T data) {
    windwardResponse.write(
        HttpStatus.OK.value(), MediaType.TEXT_EVENT_STREAM_VALUE.value, headers, data);
  }

  public void complete() {
    windwardResponse.write(
        HttpStatus.OK.value(),
        MediaType.TEXT_EVENT_STREAM_VALUE.value,
        headers,
        SseChunkTail.SSE_CHUNK_TAIL);
  }
}
