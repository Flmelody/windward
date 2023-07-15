package org.flmelody.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.flmelody.core.exception.ServerException;
import org.flmelody.core.netty.NettyHttpServer;
import org.flmelody.util.UrlUtil;

/**
 * @author esotericman
 */
public class Windward implements Router {
  private final String contextPath;
  // registered function
  private static final List<AbstractRouterGroup> routerGroups = new ArrayList<>();
  // handlers
  private static final List<Handler> globalHandlers = new ArrayList<>();
  private HttpServer httpServer;

  private Windward(String contextPath) {
    this.contextPath = contextPath;
  }

  public static Windward setup() {
    return setup(8080, new LoggingHandler());
  }

  /**
   * prepare core engine of Windward
   *
   * @param port server port
   * @return core engine of Windward
   */
  public static Windward setup(int port, Handler... handlers) {
    return setup(port, UrlUtil.SLASH, handlers);
  }

  /**
   * prepare core engine of Windward
   *
   * @param port server port
   * @param contextPath path of root
   * @return core engine of Windward
   */
  public static Windward setup(int port, String contextPath, Handler... handlers) {
    Windward windward = new Windward(contextPath);
    windward.httpServer = new NettyHttpServer(port);
    return windward.registerHandler(handlers);
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
   * register handler
   *
   * @param handlers handler
   * @return current windward
   */
  public Windward registerHandler(Handler... handlers) {
    if (handlers == null || handlers.length == 0) {
      return this;
    }
    globalHandlers.addAll(Arrays.asList(handlers));
    return this;
  }

  /**
   * find out registered function by specific path
   *
   * @param relativePath relativePath
   * @return registered function
   */
  public static Object findRouter(String relativePath, String method) {
    for (AbstractRouterGroup routerGroup : routerGroups) {
      Object o = routerGroup.matchRouter(relativePath, method);
      if (o != null) {
        return o;
      }
    }
    return null;
  }

  /**
   * get handlers
   *
   * @return handlers
   */
  public static List<Handler> handlers() {
    return globalHandlers;
  }

  public Windward then() {
    return this;
  }

  /**
   * register function with get method
   *
   * @param relativePath relativePath
   * @param supplier supplier
   * @param <R> response data
   * @return this
   */
  public <R> Windward get(String relativePath, Supplier<R> supplier) {
    group(UrlUtil.SLASH).get(relativePath, supplier);
    return this;
  }

  /**
   * register function with get method
   *
   * @param relativePath relativePath
   * @param consumer function to consume
   * @return this
   */
  public Windward get(String relativePath, Consumer<? extends WindwardContext> consumer) {
    group(UrlUtil.SLASH).get(relativePath, consumer);
    return this;
  }

  @Override
  public <R> Router put(String relativePath, Supplier<R> supplier) {
    group(UrlUtil.SLASH).put(relativePath, supplier);
    return this;
  }

  /**
   * register function with put method
   *
   * @param relativePath relativePath
   * @param consumer function to consume
   * @return this
   */
  public Windward put(String relativePath, Consumer<? extends WindwardContext> consumer) {
    group(UrlUtil.SLASH).put(relativePath, consumer);
    return this;
  }

  @Override
  public <R> Router post(String relativePath, Supplier<R> supplier) {
    group(UrlUtil.SLASH).post(relativePath, supplier);
    return this;
  }

  /**
   * register function with post method
   *
   * @param relativePath relativePath
   * @param consumer function to consume
   * @return this
   */
  public Windward post(String relativePath, Consumer<? extends WindwardContext> consumer) {
    group(UrlUtil.SLASH).post(relativePath, consumer);
    return this;
  }

  @Override
  public <R> Router delete(String relativePath, Supplier<R> supplier) {
    group(UrlUtil.SLASH).delete(relativePath, supplier);
    return this;
  }

  /**
   * register function with delete method
   *
   * @param relativePath relativePath
   * @param consumer function to consume
   * @return this
   */
  public Windward delete(String relativePath, Consumer<? extends WindwardContext> consumer) {
    group(UrlUtil.SLASH).delete(relativePath, consumer);
    return this;
  }
}
