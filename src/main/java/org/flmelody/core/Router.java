package org.flmelody.core;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author flmelody
 */
public interface Router {
  /**
   * register function with get method
   *
   * @param relativePath relativePath
   * @param supplier supplier
   * @return this
   * @param <R> response data
   */
  <R> Router get(String relativePath, Supplier<R> supplier);

  /**
   * register function with get method
   *
   * @param relativePath relativePath
   * @param consumer function to consume
   * @return this
   */
  Router get(String relativePath, Consumer<? extends WindwardContext> consumer);

  /**
   * register function with put method
   *
   * @param relativePath relativePath
   * @param supplier supplier
   * @return this
   * @param <R> response data
   */
  <R> Router put(String relativePath, Supplier<R> supplier);

  /**
   * register function with put method
   *
   * @param relativePath relativePath
   * @param consumer function to consume
   * @return this
   */
  Router put(String relativePath, Consumer<? extends WindwardContext> consumer);

  /**
   * register function with post method
   *
   * @param relativePath relativePath
   * @param supplier supplier
   * @return this
   * @param <R> response data
   */
  <R> Router post(String relativePath, Supplier<R> supplier);

  /**
   * register function with post method
   *
   * @param relativePath relativePath
   * @param consumer function to consume
   * @return this
   */
  Router post(String relativePath, Consumer<? extends WindwardContext> consumer);

  /**
   * register function with delete method
   *
   * @param relativePath relativePath
   * @param supplier supplier
   * @return this
   * @param <R> response data
   */
  <R> Router delete(String relativePath, Supplier<R> supplier);

  /**
   * register function with delete method
   *
   * @param relativePath relativePath
   * @param consumer function to consume
   * @return this
   */
  Router delete(String relativePath, Consumer<? extends WindwardContext> consumer);
}
