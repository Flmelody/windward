package org.flmelody.core;

import org.flmelody.util.UrlUtil;

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
    registerRouter(relativePath, HttpMethod.GET.name(), supplier);
    return this;
  }

  @Override
  public Router get(String relativePath, Consumer<? extends WindwardContext> consumer) {
    registerRouter(relativePath, HttpMethod.GET.name(), consumer);
    return this;
  }

  @Override
  public <R> Router put(String relativePath, Supplier<R> supplier) {
    registerRouter(relativePath, HttpMethod.PUT.name(), supplier);
    return this;
  }

  @Override
  public Router put(String relativePath, Consumer<? extends WindwardContext> consumer) {
    registerRouter(relativePath, HttpMethod.PUT.name(), consumer);
    return this;
  }

  @Override
  public <R> Router post(String relativePath, Supplier<R> supplier) {
    registerRouter(relativePath, HttpMethod.POST.name(), supplier);
    return this;
  }

  @Override
  public Router post(String relativePath, Consumer<? extends WindwardContext> consumer) {
    registerRouter(relativePath, HttpMethod.POST.name(), consumer);
    return this;
  }

  @Override
  public <R> Router delete(String relativePath, Supplier<R> supplier) {
    registerRouter(relativePath, HttpMethod.DELETE.name(), supplier);
    return this;
  }

  @Override
  public Router delete(String relativePath, Consumer<? extends WindwardContext> consumer) {
    registerRouter(relativePath, HttpMethod.DELETE.name(), consumer);
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

  private <I> void registerRouter(String relativePath, String method, I i) {
    String path = UrlUtil.buildUrl(groupPath, relativePath);
    if (routers.containsKey(path)) {
      routers.get(path).put(method, i);
    } else {
      Map<String, Object> routerMap = new HashMap<>(2 << 3);
      routerMap.put(method, i);
      routers.put(path, routerMap);
    }
  }
}
