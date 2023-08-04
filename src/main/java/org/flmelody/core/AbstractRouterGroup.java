package org.flmelody.core;

import org.flmelody.core.context.EmptyWindwardContext;
import org.flmelody.core.context.EnhancedWindwardContext;
import org.flmelody.core.context.SimpleWindwardContext;
import org.flmelody.core.context.WindwardContext;
import org.flmelody.core.exception.RouterMappingException;
import org.flmelody.util.UrlUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author esotericman
 */
public abstract class AbstractRouterGroup implements RouterGroup {
  private String groupPath;
  private final Map<String, Map<String, ? super Object>> routers = new HashMap<>(2 << 3);

  protected AbstractRouterGroup() {
    this("/");
  }

  protected AbstractRouterGroup(String groupPath) {
    setGroupPath(groupPath);
  }

  protected void setGroupPath(String groupPath) {
    if (!groupPath.startsWith(UrlUtil.SLASH)) {
      groupPath = UrlUtil.SLASH + groupPath;
    }
    this.groupPath = groupPath;
  }

  @Override
  public <R> Router get(String relativePath, Supplier<R> supplier) {
    registerRouter(relativePath, HttpMethod.GET.name(), supplier, EmptyWindwardContext.class);
    return this;
  }

  @Override
  public Router get(String relativePath, Consumer<SimpleWindwardContext> consumer) {
    registerRouter(relativePath, HttpMethod.GET.name(), consumer, SimpleWindwardContext.class);
    return this;
  }

  @Override
  public Router get(String relativePath, Function<EnhancedWindwardContext, ?> function) {
    registerRouter(relativePath, HttpMethod.GET.name(), function, EnhancedWindwardContext.class);
    return this;
  }

  @Override
  public <R> Router put(String relativePath, Supplier<R> supplier) {
    registerRouter(relativePath, HttpMethod.PUT.name(), supplier, EmptyWindwardContext.class);
    return this;
  }

  @Override
  public Router put(String relativePath, Consumer<SimpleWindwardContext> consumer) {
    registerRouter(relativePath, HttpMethod.PUT.name(), consumer, SimpleWindwardContext.class);
    return this;
  }

  @Override
  public Router put(String relativePath, Function<EnhancedWindwardContext, ?> function) {
    registerRouter(relativePath, HttpMethod.PUT.name(), function, EnhancedWindwardContext.class);
    return this;
  }

  @Override
  public <R> Router post(String relativePath, Supplier<R> supplier) {
    registerRouter(relativePath, HttpMethod.POST.name(), supplier, EmptyWindwardContext.class);
    return this;
  }

  @Override
  public Router post(String relativePath, Consumer<SimpleWindwardContext> consumer) {
    registerRouter(relativePath, HttpMethod.POST.name(), consumer, SimpleWindwardContext.class);
    return this;
  }

  @Override
  public Router post(String relativePath, Function<EnhancedWindwardContext, ?> function) {
    registerRouter(relativePath, HttpMethod.POST.name(), function, EnhancedWindwardContext.class);
    return this;
  }

  @Override
  public <R> Router delete(String relativePath, Supplier<R> supplier) {
    registerRouter(relativePath, HttpMethod.DELETE.name(), supplier, EmptyWindwardContext.class);
    return this;
  }

  @Override
  public Router delete(String relativePath, Consumer<SimpleWindwardContext> consumer) {
    registerRouter(relativePath, HttpMethod.DELETE.name(), consumer, SimpleWindwardContext.class);
    return this;
  }

  @Override
  public Router delete(String relativePath, Function<EnhancedWindwardContext, ?> function) {
    registerRouter(relativePath, HttpMethod.DELETE.name(), function, EnhancedWindwardContext.class);
    return this;
  }

  @Override
  public <R> R matchRouter(String relativePath, String method) {
    if (!relativePath.startsWith(groupPath)) {
      return null;
    }
    if (relativePath.endsWith(UrlUtil.SLASH)) {
      relativePath = relativePath.replaceFirst("/$", "");
    }
    relativePath = relativePath.replaceAll("[{}]", "");
    if (!routers.containsKey(relativePath)) {
      Set<String> routerKeys = routers.keySet();
      for (String routerKey : routerKeys) {
        if (!routerKey.matches("\\{.*}")) {
          continue;
        }
        String routerRegex = routerKey.replaceAll("\\{(.*?)}", "(.+)");
        if (relativePath.matches(routerRegex)) {
          FunctionMetaInfo<?> functionMetaInfo =
              (FunctionMetaInfo<?>) routers.get(routerKey).get(method);
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
      String variable = matcher.group(1);
      if (variable.trim().isEmpty()) {
        throw new RouterMappingException("Path variable name is empty!");
      }
      pathVariables.put(variable, null);
    }
    return pathVariables;
  }
}
