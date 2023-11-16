package org.flmelody.util;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * @author esotericman
 */
public class UrlUtilTest {

  @Test
  public void buildUrlTest() {
    String exceptedResult = "/a/b/c";
    String result0 = UrlUtil.buildUrl("/a", "/b/", "c");
    assertEquals(exceptedResult, result0);

    String result1 = UrlUtil.buildUrl("/a", "b/", "c");
    assertEquals(exceptedResult, result1);

    String result2 = UrlUtil.buildUrl("a", "b", "c");
    assertEquals(exceptedResult, result2);

    String result3 = UrlUtil.buildUrl("/a/", "b", "/c/");
    assertEquals(exceptedResult, result3);

    String result4 = UrlUtil.buildUrl("/a/", "/b/", "/c/");
    assertEquals(exceptedResult, result4);

    String result5 = UrlUtil.buildUrl("/", "/");
    assertEquals("/", result5);
  }
}
