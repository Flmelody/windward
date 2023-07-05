package org.flmelody.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.flmelody.core.netty.NettyHttpServer;

/**
 * @author esotericman
 */
public class Windward implements Router {
  // predefined router group
  private static AbstractRouterGroup baseRouterGroup;
  // registered function
  private static final List<AbstractRouterGroup> routerGroups = new ArrayList<>();
  // handlers
  private static final List<Handler> handlers = new ArrayList<>();
  private HttpServer httpServer;

  private Windward(String relativePath) {
    baseRouterGroup = new DefaultRouterGroup(relativePath);
  }

  public static Windward setup() {
    return setup(8080);
  }

  /**
   * prepare core engine of Windward
   *
   * @param port server port
   * @return core engine of Windward
   */
  public static Windward setup(int port) {
    return setup(port, "/");
  }

  /**
   * prepare core engine of Windward
   *
   * @param port server port
   * @param relativePath path of default router group
   * @return core engine of Windward
   */
  public static Windward setup(int port, String relativePath) {
    Windward windward = new Windward(relativePath);
    windward.httpServer = new NettyHttpServer(port);
    windward.registerHandler(new LoggingHandler());
    return windward;
  }

  /**
   * run server
   *
   * @throws Exception exception
   */
  public void run() throws Exception {
    httpServer.run();
  }

  /**
   * define new router with specific relativePath
   *
   * @param relativePath relativePath
   * @return routerGroup
   */
  public RouterGroup group(String relativePath) {
    DefaultRouterGroup defaultRouterGroup = new DefaultRouterGroup(relativePath);
    routerGroups.add(defaultRouterGroup);
    return defaultRouterGroup;
  }

  /**
   * register handler
   *
   * @param handler handler
   * @return current windward
   */
  public Windward registerHandler(Handler... handler) {
    if (handler == null) {
      return this;
    }
    handlers.addAll(Arrays.asList(handler));
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
    return baseRouterGroup.matchRouter(relativePath, method);
  }

  /**
   * get handlers
   *
   * @return handlers
   */
  public static List<Handler> handlers() {
    return handlers;
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
    baseRouterGroup.get(relativePath, supplier);
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
    baseRouterGroup.get(relativePath, consumer);
    return this;
  }

  @Override
  public <R> Router put(String relativePath, Supplier<R> supplier) {
    baseRouterGroup.put(relativePath, supplier);
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
    baseRouterGroup.get(relativePath, consumer);
    return this;
  }

  @Override
  public <R> Router post(String relativePath, Supplier<R> supplier) {
    baseRouterGroup.post(relativePath, supplier);
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
    baseRouterGroup.get(relativePath, consumer);
    return this;
  }

  @Override
  public <R> Router delete(String relativePath, Supplier<R> supplier) {
    baseRouterGroup.delete(relativePath, supplier);
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
    baseRouterGroup.get(relativePath, consumer);
    return this;
  }
}
