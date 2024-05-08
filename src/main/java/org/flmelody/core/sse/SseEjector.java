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
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SSE emitter, It's very simple to use it like bellow
 *
 * <pre>{@code
 * SseEjector sseEjector = new SseEjector(context);
 * sseEjector.send(SseEventSource.builder().data("{\"code\":200}"));
 * return sseEjector;
 * }</pre>
 *
 * <p>if you prefer to keep emitter for a while, you should use
 *
 * <pre>{@code
 * sseEjector.keepAlive(3600); // retain emitter for 1 hour / 3600 seconds.
 * }</pre>
 *
 * <p>if emitter is still alive, and you want to stop it directly, try
 *
 * <pre>{@code
 * sseEjector.complete();
 * }</pre>
 *
 * @author esotericman
 */
public class SseEjector {
  private static final Logger logger = LoggerFactory.getLogger(SseEjector.class);
  private final SseWindwardContext sseWindwardContext;
  protected final AtomicBoolean complete = new AtomicBoolean(false);
  protected final AtomicLong timeout = new AtomicLong(0);
  protected final EjectorCallback callback = new EjectorCallback();

  public SseEjector(SseWindwardContext sseWindwardContext) {
    this.sseWindwardContext = sseWindwardContext;
  }

  public void send(SseEventSource.SseEventSourceBuilder builder) {
    sseWindwardContext.send(builder.build());
  }

  /**
   * Retain Ejector for the specified number of seconds, if you do not set the time the default will
   * end directly.
   *
   * @param seconds time for ejector to live
   * @return ejector
   */
  public SseEjector keepAlive(Long seconds) {
    if (seconds < 0) {
      throw new IllegalArgumentException(
          "SseEjector survival time must be greater than or equal to 0 seconds! ");
    } else {
      timeout.compareAndSet(0, seconds);
    }
    return this;
  }

  protected long getTimeout() {
    return timeout.get();
  }

  protected EjectorCallback getCallback() {
    return callback;
  }

  /** Complete ejector, after this, you can't send data to client anymore */
  public void complete() {
    if (complete.compareAndSet(false, true)) {
      sseWindwardContext.complete();
    } else {
      logger.atWarn().log("SseEjector already completed! ");
    }
  }

  private class EjectorCallback implements Runnable {

    @Override
    public void run() {
      complete();
    }
  }
}
