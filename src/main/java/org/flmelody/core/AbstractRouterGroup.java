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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.flmelody.core.context.EnhancedWindwardContext;
import org.flmelody.core.context.SimpleWindwardContext;
import org.flmelody.core.context.WindwardContext;
import org.flmelody.core.exception.RouterMappingException;
import org.flmelody.core.plugin.resource.ResourcePlugin;
import org.flmelody.core.ws.WebSocketWindwardContext;
import org.flmelody.util.AntPathMatcher;
import org.flmelody.util.UrlUtil;

/**
 * @author esotericman
 */
public abstract class AbstractRouterGroup<M> implements RouterGroup<M> {
  private final M manager;
  private String groupPath;
  private final Map<String, Map<String, ? super Object>> routers =
      Collections.synchronizedMap(new LinkedHashMap<>(2 << 3));
  private final AntPathMatcher antPathMatcher = AntPathMatcher.newBuild().build();
  private final Map<String, Boolean> matchedRouter = new ConcurrentHashMap<>();
  private boolean resourceRouter;

  protected AbstractRouterGroup(M manager) {
    this(manager, "/");
  }

  protected AbstractRouterGroup(M manager, String groupPath) {
    this.manager = manager;
    setGroupPath(groupPath);
  }

  protected void setGroupPath(String groupPath) {
    if (!groupPath.startsWith(UrlUtil.SLASH)) {
      groupPath = UrlUtil.SLASH + groupPath;
    }
    this.groupPath = groupPath;
  }

  @Override
  public M end() {
    return manager;
  }

  @Override
  public <R> RouterGroup<M> get(String relativePath, Supplier<R> supplier) {
    return http(HttpMethod.GET, relativePath, supplier);
  }

  @Override
  public RouterGroup<M> get(String relativePath, Consumer<SimpleWindwardContext> consumer) {
    return http(HttpMethod.GET, relativePath, consumer);
  }

  @Override
  public RouterGroup<M> get(String relativePath, Function<EnhancedWindwardContext, ?> function) {
    return http(HttpMethod.GET, relativePath, function);
  }

  @Override
  public <R> RouterGroup<M> put(String relativePath, Supplier<R> supplier) {
    return http(HttpMethod.PUT, relativePath, supplier);
  }

  @Override
  public RouterGroup<M> put(String relativePath, Consumer<SimpleWindwardContext> consumer) {
    return http(HttpMethod.PUT, relativePath, consumer);
  }

  @Override
  public RouterGroup<M> put(String relativePath, Function<EnhancedWindwardContext, ?> function) {
    return http(HttpMethod.PUT, relativePath, function);
  }

  @Override
  public <R> RouterGroup<M> post(String relativePath, Supplier<R> supplier) {
    return http(HttpMethod.POST, relativePath, supplier);
  }

  @Override
  public RouterGroup<M> post(String relativePath, Consumer<SimpleWindwardContext> consumer) {
    return http(HttpMethod.POST, relativePath, consumer);
  }

  @Override
  public RouterGroup<M> post(String relativePath, Function<EnhancedWindwardContext, ?> function) {
    return http(HttpMethod.POST, relativePath, function);
  }

  @Override
  public <R> RouterGroup<M> delete(String relativePath, Supplier<R> supplier) {
    return http(HttpMethod.DELETE, relativePath, supplier);
  }

  @Override
  public RouterGroup<M> delete(String relativePath, Consumer<SimpleWindwardContext> consumer) {
    return http(HttpMethod.DELETE, relativePath, consumer);
  }

  @Override
  public RouterGroup<M> delete(String relativePath, Function<EnhancedWindwardContext, ?> function) {
    return http(HttpMethod.DELETE, relativePath, function);
  }

  @Override
  public RouterGroup<M> ws(String relativePath, Consumer<WebSocketWindwardContext> consumer) {
    registerRouter(relativePath, HttpMethod.GET.name(), consumer, WebSocketWindwardContext.class);
    return this;
  }

  @Override
  public RouterGroup<M> resource(String... pathPatterns) {
    if (pathPatterns != null) {
      ResourcePlugin resourcePlugin = Windward.plugin(ResourcePlugin.class);
      for (String pathPattern : pathPatterns) {
        registerRouter(
            pathPattern, HttpMethod.GET.name(), resourcePlugin, SimpleWindwardContext.class);
      }
      this.resourceRouter = true;
    }
    return this;
  }

  @Override
  public <R> RouterGroup<M> http(HttpMethod httpMethod, String relativePath, Supplier<R> supplier) {
    registerRouter(relativePath, httpMethod.name(), supplier, SimpleWindwardContext.class);
    return this;
  }

  @Override
  public RouterGroup<M> http(
      HttpMethod httpMethod, String relativePath, Consumer<SimpleWindwardContext> consumer) {
    registerRouter(relativePath, httpMethod.name(), consumer, SimpleWindwardContext.class);
    return this;
  }

  @Override
  public RouterGroup<M> http(
      HttpMethod httpMethod, String relativePath, Function<EnhancedWindwardContext, ?> function) {
    registerRouter(relativePath, httpMethod.name(), function, EnhancedWindwardContext.class);
    return this;
  }

  @Override
  public <R> R matchRouter(String relativePath, String method) {
    if (!relativePath.startsWith(groupPath)) {
      return null;
    }
    if (relativePath.endsWith(UrlUtil.SLASH) && !UrlUtil.SLASH.equals(relativePath)) {
      relativePath = relativePath.replaceFirst("/$", "");
    }
    relativePath = relativePath.replaceAll("[{}]", "");
    if (!routers.containsKey(relativePath)) {
      Set<String> routerKeys = routers.keySet();
      for (String routerKey : routerKeys) {
        Pattern compiledPattern = Pattern.compile("\\{(.*?)}");
        Matcher compiledMatcher = compiledPattern.matcher(routerKey);
        if (!compiledMatcher.find()) {
          // If it's a GET request, try to match it again using Ant-style path patterns
          if (HttpMethod.GET.name().equalsIgnoreCase(method) && resourceRouter) {
            Boolean result = matchedRouter.get(relativePath);
            if (Boolean.TRUE.equals(result)) {
              //noinspection unchecked
              return (R) routers.get(routerKey).get(method);
            } else {
              boolean matched = antPathMatcher.isMatch(routerKey, relativePath);
              matchedRouter.put(relativePath, matched);
              if (matched) {
                //noinspection unchecked
                return (R) routers.get(routerKey).get(method);
              }
            }
          } else {
            continue;
          }
        }
        String routerRegex = routerKey.replaceAll("\\{(.*?)}", "(.+)");
        if (relativePath.matches(routerRegex)) {
          FunctionMetaInfo<?> functionMetaInfo =
              (FunctionMetaInfo<?>) routers.get(routerKey).get(method);
          if (functionMetaInfo == null) {
            return null;
          }
          Map<String, Object> pathVariables = functionMetaInfo.getPathVariables();
          List<String> keys = new ArrayList<>(pathVariables.keySet());
          Pattern pattern = Pattern.compile(routerRegex);
          Matcher matcher = pattern.matcher(relativePath);
          int groupCount = matcher.groupCount();
          while (matcher.find()) {
            for (int j = 0; j < Math.min(groupCount, keys.size()); j++) {
              pathVariables.put(keys.get(j), matcher.group(j + 1));
            }
          }
          //noinspection unchecked
          return (R) functionMetaInfo;
        }
      }
      return null;
    }
    //noinspection unchecked
    return (R) routers.get(relativePath).get(method);
  }

  private <I> void registerRouter(
      String relativePath, String method, I i, Class<? extends WindwardContext> clazz) {
    String path = UrlUtil.buildUrl(groupPath, relativePath);
    Map<String, Object> pathVariables = checkPlaceholder(path);
    FunctionMetaInfo<I> functionMetaInfo = new FunctionMetaInfo<>(i, clazz, pathVariables);
    if (routers.containsKey(path)) {
      routers.get(path).put(method, functionMetaInfo);
    } else {
      Map<String, Object> routerMap = new HashMap<>(2 << 3);
      routerMap.put(method, functionMetaInfo);
      routers.put(path, routerMap);
    }
  }

  private Map<String, Object> checkPlaceholder(String path) {
    Map<String, Object> pathVariables = new LinkedHashMap<>(2 << 3);
    Pattern pattern = Pattern.compile("(\\{(.*?)})");
    Matcher matcher = pattern.matcher(path);
    while (matcher.find()) {
      String variable = matcher.group(2);
      if (variable.trim().isEmpty()) {
        throw new RouterMappingException("Path variable name is empty!");
      }
      pathVariables.put(variable, null);
    }
    return pathVariables;
  }
}
