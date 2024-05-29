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

package org.flmelody.core.ws.authentication;

import org.flmelody.core.context.WindwardContext;

/**
 * WebSocket authenticator to authenticate requests prior to protocol upgrade.
 *
 * @author esotericman
 */
public interface AuthorizationProvider {

  /**
   * Authenticate the request, if successful, the protocol will be upgraded, otherwise close the
   * current http connection.
   *
   * @param windwardContext context
   * @return certified or not
   */
  boolean authenticate(WindwardContext windwardContext);
}
