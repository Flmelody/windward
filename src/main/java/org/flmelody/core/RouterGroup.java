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

package org.flmelody.core;

/**
 * @author esotericman
 */
public interface RouterGroup<M> extends Router<RouterGroup<M>> {
  /**
   * Find out matched router function
   *
   * @param relativePath relativePath
   * @param method method
   * @return router function
   * @param <R> router type
   */
  <R> R matchRouter(String relativePath, String method);

  /**
   * Return this groups manager, so that we can bind more routers of root
   *
   * @return groups manager
   */
  M end();
}
