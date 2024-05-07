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

import java.util.concurrent.atomic.AtomicBoolean;
import org.flmelody.core.context.support.SseWindwardContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author esotericman
 */
public class SseEjector {
  private static final Logger logger = LoggerFactory.getLogger(SseEjector.class);
  private final SseWindwardContext sseWindwardContext;
  private final AtomicBoolean complete = new AtomicBoolean(false);
  private final EjectorCallback callback = new EjectorCallback();

  public SseEjector(SseWindwardContext sseWindwardContext) {
    this.sseWindwardContext = sseWindwardContext;
  }

  public void send(SseEventSource.SseEventSourceBuilder builder) {
    sseWindwardContext.send(builder.build());
  }

  public void complete() {
    if (complete.compareAndSet(false, true)) {
      sseWindwardContext.complete();
    } else {
      logger.atWarn().log("SseEjector already completed");
    }
  }

  private class EjectorCallback implements Runnable {

    @Override
    public void run() {
      SseEjector.this.complete.set(true);
    }
  }
}
