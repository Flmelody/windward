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

/**
 * @author esotericman
 */
public final class SseEventSource implements SseEvent {
  private StringBuilder sb;

  private SseEventSource() {}

  private SseEventSource append(String text) {
    if (this.sb == null) {
      this.sb = new StringBuilder();
    }
    this.sb.append(text);
    return this;
  }

  public static SseEventSourceBuilder builder() {
    return new SseEventSourceBuilder(new SseEventSource());
  }

  /** {@inheritDoc} */
  @Override
  public SseEventSource id(String id) {
    append("id: ").append(id != null ? id : "").append("\n");
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public SseEventSource name(String eventName) {
    append("event: ").append(eventName != null ? eventName : "").append("\n");
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public SseEventSource reconnectTime(long reconnectTimeMillis) {
    append("retry: ").append(String.valueOf(reconnectTimeMillis)).append("\n");
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public SseEventSource comment(String comment) {
    append(": ").append(comment != null ? comment : "").append("\n");
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public SseEventSource data(String object) {
    if (object != null) {
      append("data: ").append(object).append("\n");
    }
    return this;
  }

  /** Builder for SseEventSource */
  public static class SseEventSourceBuilder implements SseEvent {
    private final SseEventSource sseEventSource;

    private SseEventSourceBuilder(SseEventSource sseEventSource) {
      this.sseEventSource = sseEventSource;
    }

    @Override
    public SseEventSourceBuilder id(String id) {
      sseEventSource.id(id);
      return this;
    }

    @Override
    public SseEventSourceBuilder name(String eventName) {
      sseEventSource.name(eventName);
      return this;
    }

    @Override
    public SseEventSourceBuilder reconnectTime(long reconnectTimeMillis) {
      sseEventSource.reconnectTime(reconnectTimeMillis);
      return this;
    }

    @Override
    public SseEventSourceBuilder comment(String comment) {
      sseEventSource.comment(comment);
      return this;
    }

    @Override
    public SseEventSourceBuilder data(String object) {
      sseEventSource.data(object);
      return this;
    }

    public String build() {
      StringBuilder stringBuilder = sseEventSource.sb;
      if (stringBuilder != null) {
        String res = stringBuilder.append("\n").toString();
        sseEventSource.sb = null;
        return res;
      }
      return null;
    }
  }
}
