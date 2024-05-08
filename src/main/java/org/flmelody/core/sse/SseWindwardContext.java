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

package org.flmelody.core.sse;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.flmelody.core.HttpHeader;
import org.flmelody.core.HttpHeaderValue;
import org.flmelody.core.HttpStatus;
import org.flmelody.core.MediaType;
import org.flmelody.core.WindwardRequest;
import org.flmelody.core.WindwardResponse;
import org.flmelody.core.context.EnhancedWindwardContext;
import org.flmelody.core.context.support.HttpKind;

/**
 * @author esotericman
 */
public final class SseWindwardContext extends EnhancedWindwardContext implements HttpKind {
  private static final Map<String, Object> headers = new HashMap<>();
  private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
  private ScheduledFuture<?> scheduledFuture;

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
    if (r instanceof SseEjector) {
      SseEjector sseEjector = (SseEjector) r;
      if (sseEjector.getTimeout() == 0) {
        complete();
      } else {
        scheduledFuture =
            scheduler.schedule(sseEjector.getCallback(), sseEjector.getTimeout(), TimeUnit.SECONDS);
      }
    }
  }

  /**
   * Send data to client, actually It's type {@link SseEventSource.SseEventSourceBuilder} always.
   *
   * @param data data
   * @param <T> data type
   */
  <T> void send(T data) {
    windwardResponse.write(
        HttpStatus.OK.value(), MediaType.TEXT_EVENT_STREAM_VALUE.value, headers, data);
  }

  /** Send last tail empty content for sse. */
  void complete() {
    if (scheduledFuture != null && !scheduledFuture.isDone()) {
      scheduledFuture.cancel(true);
    }
    windwardResponse.write(
        HttpStatus.OK.value(),
        MediaType.TEXT_EVENT_STREAM_VALUE.value,
        headers,
        SseChunkTail.SSE_CHUNK_TAIL);
  }
}
