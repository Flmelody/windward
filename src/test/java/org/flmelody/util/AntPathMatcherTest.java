package org.flmelody.util;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author esotericman
 */
public class AntPathMatcherTest {

  @Test
  public void routerTest() {
    AntPathMatcher.Builder builder = new AntPathMatcher.Builder();
    AntPathMatcher antPathMatcher =
        builder.withIgnoreCase().withPathSeparator('/').withTrimTokens().build();
    Assert.assertTrue(antPathMatcher.isMatch("/api/**", "/api/test"));
    Assert.assertTrue(antPathMatcher.isMatch("/**", "/api/test"));
    Assert.assertTrue(antPathMatcher.isMatch("/**/test", "/api/test"));

    Assert.assertTrue(antPathMatcher.isMatch("/api/**.js", "/api/test.js"));
    Assert.assertFalse(antPathMatcher.isMatch("/api/**.js", "/api/test"));
    Assert.assertTrue(antPathMatcher.isMatch("/api/**.js", "/api/test/a.js"));

    Assert.assertFalse(antPathMatcher.isMatch("/static/**.js", "/api/test/a.js"));
  }
}
