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
  /**
   * Add an SSE "id" line.
   *
   * @param id SSE "id" line
   * @return event
   */
  SseEvent id(String id);

  /**
   * Add an SSE "event" line.
   *
   * @param eventName SSE "event" line
   * @return event
   */
  SseEvent name(String eventName);

  /**
   * Add an SSE "retry" line.
   *
   * @param reconnectTimeMillis SSE "retry" line
   * @return event
   */
  SseEvent reconnectTime(long reconnectTimeMillis);

  /**
   * Add an SSE "comment" line.
   *
   * @param comment SSE "comment" line
   * @return event
   */
  SseEvent comment(String comment);

  /**
   * Add an SSE "data" line.
   *
   * @param object SSE "data" line
   * @return event
   */
  SseEvent data(String object);
}
