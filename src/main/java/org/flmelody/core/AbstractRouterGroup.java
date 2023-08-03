package org.flmelody.core;

import org.flmelody.core.context.EmptyWindwardContext;
import org.flmelody.core.context.EnhancedWindwardContext;
import org.flmelody.core.context.SimpleWindwardContext;
import org.flmelody.core.context.WindwardContext;
import org.flmelody.core.exception.RouterMappingException;
import org.flmelody.util.UrlUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author esotericman
 */
public abstract class AbstractRouterGroup implements RouterGroup {
  private String groupPath;
  private final Map<String, Map<String, ? super Object>> routers = new HashMap<>(2 << 3);
  private final char leftBrace = '{';
  private final char rightBrace = '}';

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
        String routerRegex = routerKey.replaceAll("\\{.*}", "(.+)");
        if (relativePath.matches(routerRegex)) {
          //noinspection unchecked
          return (R) routers.get(routerKey).get(method);
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
    checkPlaceholder(path);
    FunctionMetaInfo<I> functionMetaInfo = new FunctionMetaInfo<>(i, clazz);
    if (routers.containsKey(path)) {
      routers.get(path).put(method, functionMetaInfo);
    } else {
      Map<String, Object> routerMap = new HashMap<>(2 << 3);
      routerMap.put(method, functionMetaInfo);
      routers.put(path, routerMap);
    }
  }

  private void checkPlaceholder(String path) {
    int countLeft = 0;
    int countRight = 0;
    for (int i = 0; i < path.length(); i++) {
      if (path.charAt(i) == leftBrace) {
        countLeft++;
      } else if (path.charAt(i) == rightBrace) {
        countRight++;
      }
    }
    if (countLeft != countRight) {
      throw new RouterMappingException(
          "Missed brace" + (countLeft > countRight ? rightBrace : leftBrace));
    }
  }
}
