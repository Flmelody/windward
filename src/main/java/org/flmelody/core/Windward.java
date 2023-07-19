package org.flmelody.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.flmelody.core.context.EnhancedWindwardContext;
import org.flmelody.core.context.SimpleWindwardContext;
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
  // filters
  private static final List<Filter> globalFilters = new ArrayList<>();
  // handlers for exception
  private static final List<ExceptionHandler> globalExceptionHandlers = new ArrayList<>();
  private HttpServer httpServer;

  private Windward(String contextPath) {
    this.contextPath = contextPath;
  }

  public static Windward setup() {
    return setup(8080, new LoggingFilter());
  }

  /**
   * prepare core engine of Windward
   *
   * @param port server port
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
   * @return core engine of Windward
   */
  public static Windward setup(int port, String contextPath, Filter... filters) {
    Windward windward = new Windward(contextPath);
    windward.httpServer = new NettyHttpServer(port);
    return windward.registerFilter(filters);
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

  public Windward then() {
    return this;
  }

  public <R> Windward get(String relativePath, Supplier<R> supplier) {
    group(UrlUtil.SLASH).get(relativePath, supplier);
    return this;
  }

  public Windward get(String relativePath, Consumer<SimpleWindwardContext> consumer) {
    group(UrlUtil.SLASH).get(relativePath, consumer);
    return this;
  }

  @Override
  public Router get(String relativePath, Function<EnhancedWindwardContext, ?> function) {
    group(UrlUtil.SLASH).get(relativePath, function);
    return this;
  }

  @Override
  public <R> Router put(String relativePath, Supplier<R> supplier) {
    group(UrlUtil.SLASH).put(relativePath, supplier);
    return this;
  }

  public Windward put(String relativePath, Consumer<SimpleWindwardContext> consumer) {
    group(UrlUtil.SLASH).put(relativePath, consumer);
    return this;
  }

  @Override
  public Router put(String relativePath, Function<EnhancedWindwardContext, ?> function) {
    group(UrlUtil.SLASH).put(relativePath, function);
    return this;
  }

  @Override
  public <R> Router post(String relativePath, Supplier<R> supplier) {
    group(UrlUtil.SLASH).post(relativePath, supplier);
    return this;
  }

  public Windward post(String relativePath, Consumer<SimpleWindwardContext> consumer) {
    group(UrlUtil.SLASH).post(relativePath, consumer);
    return this;
  }

  @Override
  public Router post(String relativePath, Function<EnhancedWindwardContext, ?> function) {
    group(UrlUtil.SLASH).post(relativePath, function);
    return this;
  }

  @Override
  public <R> Router delete(String relativePath, Supplier<R> supplier) {
    group(UrlUtil.SLASH).delete(relativePath, supplier);
    return this;
  }

  public Windward delete(String relativePath, Consumer<SimpleWindwardContext> consumer) {
    group(UrlUtil.SLASH).delete(relativePath, consumer);
    return this;
  }

  @Override
  public Router delete(String relativePath, Function<EnhancedWindwardContext, ?> function) {
    group(UrlUtil.SLASH).delete(relativePath, function);
    return this;
  }
}
