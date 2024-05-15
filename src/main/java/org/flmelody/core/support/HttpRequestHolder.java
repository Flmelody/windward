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

package org.flmelody.core.support;

import org.flmelody.core.context.WindwardContext;

/**
 *  A useful holder, contains the request context for the current thread binding.
 *
 * @author esotericamn
 */
public final class HttpRequestHolder {
  private static final ThreadLocal<WindwardContext> CONTEXT = new ThreadLocal<>();

  public static void setContext(WindwardContext windwardContext) {
    CONTEXT.set(windwardContext);
  }

  public static WindwardContext getContext() {
    return CONTEXT.get();
  }

  public static void resetContext() {
    CONTEXT.remove();
  }
}
