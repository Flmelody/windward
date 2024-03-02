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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.flmelody.core.context.EnhancedWindwardContext;
import org.flmelody.core.context.SimpleWindwardContext;
import org.flmelody.core.context.support.HttpKind;
import org.flmelody.core.exception.PluginMissException;
import org.flmelody.core.exception.ServerException;
import org.flmelody.core.netty.NettyHttpServer;
import org.flmelody.core.plugin.Plugin;
import org.flmelody.core.plugin.PluginSlot;
import org.flmelody.core.plugin.json.AutoJsonBinder;
import org.flmelody.core.plugin.resolver.CompositePluginResolver;
import org.flmelody.core.plugin.resolver.PluginResolver;
import org.flmelody.core.plugin.resource.BaseStaticResourcePlugin;
import org.flmelody.core.plugin.view.ViewEngineDetector;
import org.flmelody.core.plugin.view.freemarker.FreemarkerView;
import org.flmelody.core.plugin.view.groovy.GroovyView;
import org.flmelody.core.plugin.view.thymeleaf.ThymeleafView;
import org.flmelody.core.wind.DefaultWindManager;
import org.flmelody.core.wind.WindManager;
import org.flmelody.core.wind.event.Event;
import org.flmelody.core.wind.listener.Listener;
import org.flmelody.core.ws.WebSocketWindwardContext;
import org.flmelody.support.EnhancedFunction;
import org.flmelody.util.UrlUtil;

/**
 * @author esotericman
 */
public final class Windward implements Router<Windward> {
  // Registered function
  private static final List<AbstractRouterGroup<Windward>> routerGroups = new ArrayList<>();
  // Registered resource router
  private static final List<AbstractRouterGroup<Windward>> resourceRouterGroups = new ArrayList<>();
  // Filters
  private static final List<Filter> globalFilters = new ArrayList<>();
  // Handlers for exception
  private static final List<ExceptionHandler> globalExceptionHandlers = new ArrayList<>();
  // Plugins
  private static final Map<Class<?>, Plugin> globalPlugins = new HashMap<>();
  // Root context of application
  private final String contextPath;
  // Template files location
  private final String templateRoot;
  // Static files locations
  private final String staticResourceLocation;
  private final PluginResolver pluginResolver = new CompositePluginResolver();
  private final WindManager windManager = new DefaultWindManager(this);
  private HttpServer httpServer;

  private Windward(String contextPath, String templateRoot, String staticResourceLocation) {
    this.contextPath = contextPath;
    this.templateRoot = templateRoot;
    this.staticResourceLocation = staticResourceLocation;
  }

  public static Windward setup() {
    return setup(8080, new LoggingFilter());
  }

  /**
   * Prepare core engine of Windward
   *
   * @param port server port
   * @param filters request filters
   * @return core engine of Windward
   */
  public static Windward setup(int port, Filter... filters) {
    return setup(port, UrlUtil.SLASH, filters);
  }

  /**
   * Prepare core engine of Windward
   *
   * @param port server port
   * @param contextPath path of root
   * @param filters request filters
   * @return core engine of Windward
   */
  public static Windward setup(int port, String contextPath, Filter... filters) {
    return setup(port, contextPath, "/templates", "/static", filters);
  }

  /**
   * Prepare core engine of Windward
   *
   * @param port server port
   * @param contextPath path of root
   * @param templateRoot root for template files
   * @param staticResourceLocation location of static resource
   * @param filters request filters
   * @return core engine of Windward
   */
  public static Windward setup(
      int port,
      String contextPath,
      String templateRoot,
      String staticResourceLocation,
      Filter... filters) {
    Windward windward = new Windward(contextPath, templateRoot, staticResourceLocation);
    windward.httpServer = new NettyHttpServer(port);
    windward
        .registerExceptionHandler(new DefaultNotFoundHandler())
        .registerFilter(filters)
        .registerPlugin(PluginSlot.JSON, AutoJsonBinder.jsonPlugin)
        .registerPlugin(PluginSlot.RESOURCE, new BaseStaticResourcePlugin());
    return prepareDefault(windward);
  }

  // Prepare template engine
  private static Windward prepareDefault(Windward windward) {
    if (ViewEngineDetector.AVAILABLE_GROOVY_ENGINE) {
      windward.registerPlugin(PluginSlot.GROOVY_VIEW, new GroovyView());
    }
    if (ViewEngineDetector.AVAILABLE_THYMELEAF_ENGINE) {
      windward.registerPlugin(PluginSlot.THYMELEAF_VIEW, new ThymeleafView());
    }
    if (ViewEngineDetector.AVAILABLE_FREEMARKER_ENGINE) {
      windward.registerPlugin(PluginSlot.FREEMARKER_VIEW, new FreemarkerView());
    }
    return windward;
  }

  /**
   * Run server
   *
   * @throws ServerException exception
   */
  public void run() throws ServerException {
    // start server
    httpServer.run();
  }

  /**
   * Define new router with specific relativePath
   *
   * @param relativePath relativePath
   * @return routerGroup
   */
  public RouterGroup<Windward> group(String relativePath) {
    DefaultRouterGroup defaultRouterGroup =
        new DefaultRouterGroup(this, UrlUtil.buildUrl(contextPath, relativePath));
    routerGroups.add(defaultRouterGroup);
    return defaultRouterGroup;
  }

  /**
   * New resource group
   *
   * @param relativePath root path
   * @return routerGroup
   */
  @SuppressWarnings("SameParameterValue")
  private RouterGroup<Windward> resourceGroup(String relativePath) {
    DefaultRouterGroup defaultRouterGroup =
        new DefaultRouterGroup(this, UrlUtil.buildUrl(contextPath, relativePath), true);
    resourceRouterGroups.add(defaultRouterGroup);
    return defaultRouterGroup;
  }

  /**
   * Register filter
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
   * Register exception handler
   *
   * @param exceptionHandlers exception handler
   * @return current windward
   */
  public Windward registerExceptionHandler(ExceptionHandler... exceptionHandlers) {
    if (exceptionHandlers == null || exceptionHandlers.length == 0) {
      return this;
    }
    globalExceptionHandlers.addAll(Arrays.asList(exceptionHandlers));
    globalExceptionHandlers.sort(Comparator.comparingInt(Order::getOrder));
    return this;
  }

  /**
   * Register listener
   *
   * @param listeners event listeners
   * @return current windward
   */
  public Windward registerListener(Listener... listeners) {
    if (listeners == null) {
      return this;
    }
    for (Listener listener : listeners) {
      windManager.join(listener);
    }
    return this;
  }

  /**
   * Register plugin or overwrite existed
   *
   * @param pluginSlot slot
   * @param plugin plugin
   * @return current windward
   */
  public Windward registerPlugin(PluginSlot pluginSlot, Plugin plugin) {
    return registerPlugin(pluginSlot.clazz, plugin);
  }

  /**
   * Register plugin or overwrite existed
   *
   * @param clazz plugin class
   * @param plugin plugin
   * @return current windward
   * @deprecated use {@link #registerPlugin(PluginSlot, Plugin)} instead
   */
  @SuppressWarnings("DeprecatedIsStillUsed")
  public Windward registerPlugin(Class<? extends Plugin> clazz, Plugin plugin) {
    // resolve plugin
    pluginResolver.resolve(this, plugin);
    // bind plugin
    PluginSlot slot = PluginSlot.slot(clazz);
    if (PluginSlot.ANY.equals(slot)) {
      globalPlugins.put(clazz, plugin);
    } else {
      globalPlugins.put(slot.clazz, plugin);
    }
    return this;
  }

  /**
   * Publish event
   *
   * @param events events
   * @return current windward
   */
  public Windward publishEvent(Event... events) {
    if (events == null) {
      return this;
    }
    for (Event event : events) {
      windManager.trigger(event);
    }
    return this;
  }

  /**
   * Find out registered function or resource by specific path
   *
   * @param relativePath relativePath
   * @param method http methods name
   * @param <I> routers metadata
   * @return registered function
   */
  public static <I> FunctionMetaInfo<I> findRouter(String relativePath, String method) {
    for (AbstractRouterGroup<Windward> routerGroup : routerGroups) {
      FunctionMetaInfo<I> functionMetaInfo = routerGroup.matchRouter(relativePath, method);
      if (functionMetaInfo != null) {
        return functionMetaInfo;
      }
    }
    for (AbstractRouterGroup<Windward> routerGroup : resourceRouterGroups) {
      FunctionMetaInfo<I> functionMetaInfo = routerGroup.matchRouter(relativePath, method);
      if (functionMetaInfo != null) {
        return functionMetaInfo;
      }
    }
    return null;
  }

  /**
   * Get filters
   *
   * @return filters
   */
  public static List<Filter> filters() {
    return Collections.unmodifiableList(globalFilters);
  }

  /**
   * Get exception handlers
   *
   * @return exception handlers
   */
  public static List<ExceptionHandler> exceptionHandlers() {
    return Collections.unmodifiableList(globalExceptionHandlers);
  }

  /**
   * Get plugin
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
   * Get plugins by super or self
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

  public String getTemplateRoot() {
    return templateRoot;
  }

  public String getStaticResourceLocation() {
    return staticResourceLocation;
  }

  public Windward then() {
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public <R> Windward http(HttpMethod httpMethod, String relativePath, Supplier<R> supplier) {
    group(UrlUtil.SLASH).http(httpMethod, relativePath, supplier);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public Windward http(
      HttpMethod httpMethod, String relativePath, Consumer<SimpleWindwardContext> consumer) {
    group(UrlUtil.SLASH).http(httpMethod, relativePath, consumer);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public <C extends EnhancedWindwardContext & HttpKind> Windward http(
      HttpMethod httpMethod, String relativePath, EnhancedFunction<C, ?> function) {
    group(UrlUtil.SLASH).http(httpMethod, relativePath, function);
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
  public <C extends EnhancedWindwardContext & HttpKind> Windward get(
      String relativePath, EnhancedFunction<C, ?> function) {
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
  public <C extends EnhancedWindwardContext & HttpKind> Windward put(
      String relativePath, EnhancedFunction<C, ?> function) {
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
  public <C extends EnhancedWindwardContext & HttpKind> Windward post(
      String relativePath, EnhancedFunction<C, ?> function) {
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
  public <C extends EnhancedWindwardContext & HttpKind> Windward delete(
      String relativePath, EnhancedFunction<C, ?> function) {
    group(UrlUtil.SLASH).delete(relativePath, function);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public Windward ws(String relativePath, Consumer<WebSocketWindwardContext> consumer) {
    group(UrlUtil.SLASH).ws(relativePath, consumer);
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public Windward resource(String... pathPatterns) {
    return resources(staticResourceLocation, pathPatterns);
  }

  /** {@inheritDoc} */
  @Override
  public Windward resources(String staticResourceLocation, String... pathPatterns) {
    resourceGroup(UrlUtil.SLASH).resources(staticResourceLocation, pathPatterns);
    return this;
  }
}
