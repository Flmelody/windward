/*
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
package org.flmelody.core;

import org.flmelody.core.context.WindwardContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author esotericman
 */
public class LoggingFilter implements Filter {
  private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

  @Override
  public void filter(WindwardContext windwardContext) {
    logger.info("Accepted request {}", windwardContext.windwardRequest().getUri());
  }
}
