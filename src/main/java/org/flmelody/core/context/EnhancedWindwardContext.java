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

package org.flmelody.core.context;

import org.flmelody.core.WindwardRequest;
import org.flmelody.core.WindwardResponse;
import org.flmelody.support.EnhancedFunction;

/**
 * @author esotericman
 */
public abstract class EnhancedWindwardContext extends AbstractHttpWindwardContext {

  public EnhancedWindwardContext(
      WindwardRequest windwardRequest, WindwardResponse windwardResponse) {
    super(windwardRequest, windwardResponse);
  }

  public final <C extends EnhancedWindwardContext> void execute(EnhancedFunction<C, ?> function) {
    doOnRequest();
    //noinspection unchecked
    doOnResponse(function.apply((C) this));
  }

  protected abstract void doOnRequest();

  protected abstract <R> void doOnResponse(R r);
}
