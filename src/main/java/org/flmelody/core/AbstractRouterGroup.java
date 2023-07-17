package org.flmelody.core;

import org.flmelody.core.context.EmptyWindwardContext;
import org.flmelody.core.context.EnhancedWindwardContext;
import org.flmelody.core.context.SimpleWindwardContext;
import org.flmelody.core.context.WindwardContext;
import org.flmelody.core.function.EnhancedConsumer;
import org.flmelody.util.UrlUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author esotericman
 */
public abstract class AbstractRouterGroup implements RouterGroup {
  private String groupPath;
  private final Map<String, Map<String, ? super Object>> routers = new HashMap<>();

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
  public Router get(String relativePath, EnhancedConsumer<EnhancedWindwardContext, ?> consumer) {
    registerRouter(relativePath, HttpMethod.GET.name(), consumer, EnhancedWindwardContext.class);
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
  public <R> R matchRouter(String relativePath, String method) {
    if (!relativePath.startsWith(groupPath)) {
      return null;
    }
    if (relativePath.endsWith(UrlUtil.SLASH)) {
      relativePath = relativePath.replaceFirst("/$", "");
    }
    if (!routers.containsKey(relativePath)) {
      return null;
    }
    //noinspection unchecked
    return (R) routers.get(relativePath).get(method);
  }

  private <I> void registerRouter(
      String relativePath, String method, I i, Class<? extends WindwardContext> clazz) {
    String path = UrlUtil.buildUrl(groupPath, relativePath);
    FunctionMetaInfo<I> functionMetaInfo = new FunctionMetaInfo<>(i, clazz);
    if (routers.containsKey(path)) {
      routers.get(path).put(method, functionMetaInfo);
    } else {
      Map<String, Object> routerMap = new HashMap<>(2 << 3);
      routerMap.put(method, functionMetaInfo);
      routers.put(path, routerMap);
    }
  }
}
