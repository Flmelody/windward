package org.flmelody.core;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * @author esotericman
 */
public class DefaultRouterGroupTest {

  @Test
  public void getTest() {
    DefaultRouterGroup defaultRouterGroup = new DefaultRouterGroup("/");
    FunctionMetaInfo<?> functionMetaInfo =
        defaultRouterGroup
            .get("/dev/v1/{name}", () -> "hello world")
            .matchRouter("/dev/v1/esotericman", HttpMethod.GET.name());
    assertEquals(functionMetaInfo.getPathVariables().get("{name}"), "esotericman");
  }
}
