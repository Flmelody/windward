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

import org.flmelody.core.WindwardRequest;
import org.flmelody.core.WindwardResponse;
import org.flmelody.core.context.EnhancedWindwardContext;
import org.flmelody.core.exception.ResourceNotFoundException;

/**
 * @author esotericman
 */
public class ViewWindwardContext extends EnhancedWindwardContext implements HttpKind {
  public ViewWindwardContext(WindwardRequest windwardRequest, WindwardResponse windwardResponse) {
    super(windwardRequest, windwardResponse);
  }

  @Override
  public <R> void doOnResponse(R r) {
    if (!(r instanceof String)) {
      throw new ResourceNotFoundException("No suitable resource!");
    }
    html(String.valueOf(r), null);
  }
}
