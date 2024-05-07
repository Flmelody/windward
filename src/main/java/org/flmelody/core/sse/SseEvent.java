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
public interface SseEvent {
  /** Add an SSE "id" line. */
  SseEvent id(String id);

  /** Add an SSE "event" line. */
  SseEvent name(String eventName);

  /** Add an SSE "retry" line. */
  SseEvent reconnectTime(long reconnectTimeMillis);

  /** Add an SSE "comment" line. */
  SseEvent comment(String comment);

  /** Add an SSE "data" line. */
  SseEvent data(String object);
}
