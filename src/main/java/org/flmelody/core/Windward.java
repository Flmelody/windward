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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.flmelody.core.context.EnhancedWindwardContext;
import org.flmelody.core.context.SimpleWindwardContext;
import org.flmelody.core.exception.PluginMissException;
import org.flmelody.core.exception.ServerException;
import org.flmelody.core.netty.NettyHttpServer;
import org.flmelody.core.plugin.Plugin;
import org.flmelody.core.plugin.json.AutoJsonBinder;
import org.flmelody.core.plugin.json.JsonPlugin;
import org.flmelody.core.plugin.resolver.CompositePluginResolver;
import org.flmelody.core.plugin.resolver.PluginResolver;
import org.flmelody.core.plugin.view.ViewEngineDetector;
import org.flmelody.core.plugin.view.groovy.GroovyView;
import org.flmelody.core.plugin.view.thymeleaf.ThymeleafView;
import org.flmelody.core.ws.WebSocketWindwardContext;
import org.flmelody.util.UrlUtil;

/**
 * @author esotericman
 */
public class Windward implements Router {
  // registered function
  private static final List<AbstractRouterGroup> routerGroups = new ArrayList<>();
  // filters
  private static final List<Filter> globalFilters = new ArrayList<>();
  // handlers for exception
  private static final List<ExceptionHandler> globalExceptionHandlers = new ArrayList<>();
  // plugins
  private static final Map<Class<?>, Plugin> globalPlugins = new HashMap<>();
  private final String contextPath;
  private final String resourceRoot;
  private final PluginResolver pluginResolver = new CompositePluginResolver();
  private HttpServer httpServer;

  private Windward(String contextPath, String resourceRoot) {
    this.contextPath = contextPath;
    this.resourceRoot = resourceRoot;
  }

  public static Windward setup() {
    return setup(8080, new LoggingFilter());
  }

  /**
   * prepare core engine of Windward
   *
   * @param port server port
   * @param filters request filters
   * @return core engine of Windward
   */
  public static Windward setup(int port, Filter... filters) {
    return setup(port, UrlUtil.SLASH, filters);
  }

  /**
   * prepare core engine of Windward
   *
   * @param port server port
   * @param contextPath path of root
   * @param filters request filters
   * @return core engine of Windward
   */
  public static Windward setup(int port, String contextPath, Filter... filters) {
    return setup(port, contextPath, "/templates", filters);
  }

  /**
   * prepare core engine of Windward
   *
   * @param port server port
   * @param contextPath path of root
   * @param resourceRoot root for resource
   * @param filters request filters
   * @return core engine of Windward
   */
  public static Windward setup(
      int port, String contextPath, String resourceRoot, Filter... filters) {
    Windward windward = new Windward(contextPath, resourceRoot);
    windward.httpServer = new NettyHttpServer(port);
    windward.registerFilter(filters).registerPlugin(JsonPlugin.class, AutoJsonBinder.jsonPlugin);
    return prepareDefault(windward);
  }

  // prepare template engine
  private static Windward prepareDefault(Windward windward) {
    if (ViewEngineDetector.AVAILABLE_GROOVY_ENGINE) {
      windward.registerPlugin(GroovyView.class, new GroovyView());
    }
    if (ViewEngineDetector.AVAILABLE_THYMELEAF_ENGINE) {
      windward.registerPlugin(ThymeleafView.class, new ThymeleafView());
    }
    return windward;
  }

  /**
   * run server
   *
   * @throws ServerException exception
   */
  public void run() throws ServerException {
    httpServer.run();
  }

  /**
   * define new router with specific relativePath
   *
   * @param relativePath relativePath
   * @return routerGroup
   */
  public RouterGroup group(String relativePath) {
    DefaultRouterGroup defaultRouterGroup =
        new DefaultRouterGroup(UrlUtil.buildUrl(contextPath, relativePath));
    routerGroups.add(defaultRouterGroup);
    return defaultRouterGroup;
  }

  /**
   * register filter
   *
   * @param filters filter
   * @return current windward
   */
  public Windward registerFilter(Filter... filters) {
    if (filters == null || filters.length == 0) {
      return this;
    }
    globalFilters.addAll(Arrays.asList(filters));
    return this;
  }

  /**
   * register exception handler
   *
   * @param exceptionHandlers exception handler
   * @return current windward
   */
  public Windward registerExceptionHandler(ExceptionHandler... exceptionHandlers) {
    if (exceptionHandlers == null || exceptionHandlers.length == 0) {
      return this;
    }
    globalExceptionHandlers.addAll(Arrays.asList(exceptionHandlers));
    return this;
  }

  /**
   * register plugin or overwrite existed
   *
   * @param clazz plugin class
   * @param plugin plugin
   * @return current windward
   */
  public Windward registerPlugin(Class<? extends Plugin> clazz, Plugin plugin) {
    // resolve plugin
    pluginResolver.resolve(this, plugin);
    // bind plugin
    globalPlugins.put(clazz, plugin);
    return this;
  }

  /**
   * find out registered function by specific path
   *
   * @param relativePath relativePath
   * @return registered function
   */
  public static <I> FunctionMetaInfo<I> findRouter(String relativePath, String method) {
    for (AbstractRouterGroup routerGroup : routerGroups) {
      FunctionMetaInfo<I> functionMetaInfo = routerGroup.matchRouter(relativePath, method);
      if (functionMetaInfo != null) {
        return functionMetaInfo;
      }
    }
    return null;
  }

  /**
   * get filters
   *
   * @return filters
   */
  public static List<Filter> filters() {
    return globalFilters;
  }

  /**
   * get exception handlers
   *
   * @return exception handlers
   */
  public static List<ExceptionHandler> exceptionHandlers() {
    return globalExceptionHandlers;
  }

  /**
   * get plugin
   *
   * @param clazz class of plugin
   * @param <T> plugin type
   * @return plugin
   */
  public static <T extends Plugin> T plugin(Class<T> clazz) {
    if (!globalPlugins.containsKey(clazz)) {
      throw new PluginMissException(String.format("Plugin [%s] not found", clazz.getName()));
    }
    //noinspection unchecked
    return (T) globalPlugins.get(clazz);
  }

  /**
   * get plugins by super or self
   *
   * @param clazz super or self class
   * @param <T> class type
   * @return plugins
   */
  public static <T extends Plugin> List<T> plugins(Class<T> clazz) {
    return globalPlugins.values().stream()
        .filter(plugin -> clazz.isAssignableFrom(plugin.getClass()))
        .map(
            plugin -> {
              //noinspection unchecked
              return (T) plugin;
            })
        .collect(Collectors.toList());
  }

  public String getContextPath() {
    return contextPath;
  }

  public String getResourceRoot() {
    return resourceRoot;
  }

  public Windward then() {
    return this;
  }

  /** {@inheritDoc} */
  public <R> Windward get(String relativePath, Supplier<R> supplier) {
    group(UrlUtil.SLASH).get(relativePath, supplier);
    return this;
  }

  /** {@inheritDoc} */
  public Windward get(String relativePath, Consumer<SimpleWindwardContext> consumer) {
    group(UrlUtil.SLASH).get(relativePath, consumer);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public Windward get(String relativePath, Function<EnhancedWindwardContext, ?> function) {
    group(UrlUtil.SLASH).get(relativePath, function);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public <R> Windward put(String relativePath, Supplier<R> supplier) {
    group(UrlUtil.SLASH).put(relativePath, supplier);
    return this;
  }

  /** {@inheritDoc} */
  public Windward put(String relativePath, Consumer<SimpleWindwardContext> consumer) {
    group(UrlUtil.SLASH).put(relativePath, consumer);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public Windward put(String relativePath, Function<EnhancedWindwardContext, ?> function) {
    group(UrlUtil.SLASH).put(relativePath, function);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public <R> Windward post(String relativePath, Supplier<R> supplier) {
    group(UrlUtil.SLASH).post(relativePath, supplier);
    return this;
  }

  /** {@inheritDoc} */
  public Windward post(String relativePath, Consumer<SimpleWindwardContext> consumer) {
    group(UrlUtil.SLASH).post(relativePath, consumer);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public Windward post(String relativePath, Function<EnhancedWindwardContext, ?> function) {
    group(UrlUtil.SLASH).post(relativePath, function);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public <R> Windward delete(String relativePath, Supplier<R> supplier) {
    group(UrlUtil.SLASH).delete(relativePath, supplier);
    return this;
  }

  /** {@inheritDoc} */
  public Windward delete(String relativePath, Consumer<SimpleWindwardContext> consumer) {
    group(UrlUtil.SLASH).delete(relativePath, consumer);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public Windward delete(String relativePath, Function<EnhancedWindwardContext, ?> function) {
    group(UrlUtil.SLASH).delete(relativePath, function);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public Windward ws(String relativePath, Consumer<WebSocketWindwardContext> consumer) {
    group(UrlUtil.SLASH).ws(relativePath, consumer);
    return this;
  }
}
