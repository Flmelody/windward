package org.flmelody.core;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author esotericman
 */
public abstract class AbstractRouterGroup implements RouterGroup {
  private final String groupPath;
  private static final String SLASH = "/";
  private final Map<String, Map<String, ? super Object>> routers = new HashMap<>();

  public AbstractRouterGroup() {
    this("/");
  }

  protected AbstractRouterGroup(String groupPath) {
    if (!groupPath.startsWith(SLASH)) {
      groupPath = SLASH + groupPath;
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
    if (relativePath.endsWith(SLASH)) {
      relativePath = relativePath.replaceFirst("/$", "");
    }
    if (!routers.containsKey(relativePath)) {
      return null;
    }
    //noinspection unchecked
    return (R) routers.get(relativePath).get(method);
  }

  private <I> void registerRouter(String relativePath, String method, I i) {
    String path = combinePath(groupPath, relativePath);
    if (routers.containsKey(path)) {
      routers.get(path).put(method, i);
    } else {
      Map<String, Object> routerMap = new HashMap<>(2 << 3);
      routerMap.put(method, i);
      routers.put(path, routerMap);
    }
  }

  private String combinePath(String... path) {
    if (path == null || path.length == 0) {
      throw new IllegalArgumentException();
    }
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < path.length; i++) {
      if (i == 0) {
        if (!path[i].startsWith(SLASH)) {
          path[i] = SLASH + path[i];
        }
        if (!path[i].endsWith(SLASH)) {
          path[i] += SLASH;
        }
      } else if (path[i].startsWith(SLASH)) {
        path[i] = path[i].replaceFirst("/", "");
      }
      stringBuilder.append(path[i]);
    }
    return stringBuilder.toString();
  }
}
