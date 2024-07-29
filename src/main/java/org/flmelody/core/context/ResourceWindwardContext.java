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

/**
 * Context responsible for handling static resources.
 *
 * @author esotericman
 */
public class ResourceWindwardContext extends AbstractHttpWindwardContext {
  private final String matchedPath;

  public ResourceWindwardContext(
      WindwardRequest windwardRequest, WindwardResponse windwardResponse, String matchedPath) {
    super(windwardRequest, windwardResponse);
    this.matchedPath = matchedPath;
  }

  public String getMatchedPath() {
    return matchedPath;
  }
}
