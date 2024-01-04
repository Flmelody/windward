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

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.flmelody.core.context.EnhancedWindwardContext;
import org.flmelody.core.context.SimpleWindwardContext;
import org.flmelody.core.ws.WebSocketWindwardContext;

/**
 * @author flmelody
 */
public interface Router {
  /**
   * Register function with specific method
   *
   * @param httpMethod http method
   * @param relativePath relative path
   * @param supplier supplier
   * @return this
   * @param <R> response data
   */
  <R> Router http(HttpMethod httpMethod, String relativePath, Supplier<R> supplier);

  /**
   * Register function with specific method
   *
   * @param httpMethod http method
   * @param relativePath relative path
   * @param consumer function to consume
   * @return this
   */
  Router http(HttpMethod httpMethod, String relativePath, Consumer<SimpleWindwardContext> consumer);

  /**
   * Register function with specific method
   *
   * @param httpMethod method of http
   * @param relativePath relative path
   * @param function function to consume
   * @return this
   */
  Router http(
      HttpMethod httpMethod, String relativePath, Function<EnhancedWindwardContext, ?> function);

  /**
   * Register function with get method
   *
   * @param relativePath relative path
   * @param supplier supplier
   * @param <R> response data
   * @return this
   */
  <R> Router get(String relativePath, Supplier<R> supplier);

  /**
   * Register function with get method
   *
   * @param relativePath relativePath
   * @param consumer function to consume
   * @return this
   */
  Router get(String relativePath, Consumer<SimpleWindwardContext> consumer);

  /**
   * Register function with get method
   *
   * @param relativePath relative path
   * @param function function to consume
   * @return this
   */
  Router get(String relativePath, Function<EnhancedWindwardContext, ?> function);

  /**
   * Register function with put method
   *
   * @param relativePath relative path
   * @param supplier supplier
   * @param <R> response data
   * @return this
   */
  <R> Router put(String relativePath, Supplier<R> supplier);

  /**
   * Register function with put method
   *
   * @param relativePath relative path
   * @param consumer function to consume
   * @return this
   */
  Router put(String relativePath, Consumer<SimpleWindwardContext> consumer);

  /**
   * Register function with put method
   *
   * @param relativePath relative path
   * @param function function to consume
   * @return this
   */
  Router put(String relativePath, Function<EnhancedWindwardContext, ?> function);

  /**
   * Register function with post method
   *
   * @param relativePath relative path
   * @param supplier supplier
   * @param <R> response data
   * @return this
   */
  <R> Router post(String relativePath, Supplier<R> supplier);

  /**
   * Register function with post method
   *
   * @param relativePath relative path
   * @param consumer function to consume
   * @return this
   */
  Router post(String relativePath, Consumer<SimpleWindwardContext> consumer);

  /**
   * Register function with post method
   *
   * @param relativePath relative path
   * @param function function to consume
   * @return this
   */
  Router post(String relativePath, Function<EnhancedWindwardContext, ?> function);

  /**
   * Register function with delete method
   *
   * @param relativePath relative path
   * @param supplier supplier
   * @param <R> response data
   * @return this
   */
  <R> Router delete(String relativePath, Supplier<R> supplier);

  /**
   * Register function with delete method
   *
   * @param relativePath relative path
   * @param consumer function to consume
   * @return this
   */
  Router delete(String relativePath, Consumer<SimpleWindwardContext> consumer);

  /**
   * Register function with delete method
   *
   * @param relativePath relative path
   * @param function function to consume
   * @return this
   */
  Router delete(String relativePath, Function<EnhancedWindwardContext, ?> function);

  /**
   * Register websocket function
   *
   * @param relativePath relative path
   * @param consumer function to consume
   * @return this
   */
  Router ws(String relativePath, Consumer<WebSocketWindwardContext> consumer);
}
