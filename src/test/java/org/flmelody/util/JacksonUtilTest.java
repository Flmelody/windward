package org.flmelody.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.flmelody.core.exception.JsonDeserializeException;
import org.flmelody.core.exception.JsonSerializeException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.*;

/**
 * @author esotericman
 */
public class JacksonUtilTest {
  private User user;

  @Before
  public void setUp() {
    user = mock(User.class);
  }

  @Test
  public void toJsonTest() {
    try (MockedStatic<JacksonUtil> jacksonUtilMockedStatic = mockStatic(JacksonUtil.class)) {
      jacksonUtilMockedStatic
          .when(() -> JacksonUtil.toJson(user))
          .thenReturn("{\"userId\":1,\"userName\":\"username\"}");
      assertEquals(JacksonUtil.toJson(user), "{\"userId\":1,\"userName\":\"username\"}");
    }
  }

  @Test(expected = JsonSerializeException.class)
  public void toJsonTestException() {
    try (MockedStatic<JacksonUtil> jacksonUtilMockedStatic = mockStatic(JacksonUtil.class)) {
      jacksonUtilMockedStatic
          .when(() -> JacksonUtil.toJson(""))
          .thenThrow(JsonSerializeException.class);
      verify(JacksonUtil.toJson(""));
    }
  }

  @Test
  public void toObjectTest() {
    try (MockedStatic<JacksonUtil> jacksonUtilMockedStatic = mockStatic(JacksonUtil.class)) {
      jacksonUtilMockedStatic
          .when(() -> JacksonUtil.toObject("{\"userId\":1,\"userName\":\"username\"}", User.class))
          .thenReturn(user);
      User verify =
          verify(JacksonUtil.toObject("{\"userId\":1,\"userName\":\"username\"}", User.class));
      assertEquals(verify, user);
    }
  }

  @Test(expected = JsonDeserializeException.class)
  public void toObjectTestException() {
    try (MockedStatic<JacksonUtil> jacksonUtilMockedStatic = mockStatic(JacksonUtil.class)) {
      jacksonUtilMockedStatic
          .when(() -> JacksonUtil.toObject("", User.class))
          .thenThrow(JsonDeserializeException.class);
      verify(JacksonUtil.toObject("", User.class));
    }
  }

  @Data
  @AllArgsConstructor
  static class User {
    private Integer userId;
    private String userName;
  }
}
