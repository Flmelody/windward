package org.flmelody;

import org.flmelody.core.Windward;
import org.flmelody.core.WindwardContext;
import org.junit.Test;

/**
 * @author esotericman
 */
public class FunctionTest {
  @Test
  public void test() throws Exception {
    Windward windward = Windward.setup();
    // register static  function
    windward.get("/function1", Function::function1);
    windward.put("/function1", Function::function1);
    windward.post("/function1", Function::function1);
    windward.delete("/function1", Function::function1);
    // register nonstatic  function
    windward.get("/function2", new Function()::function2);
    // register group function
    windward
        .group("/v1")
        .get("/function1", Function::function1)
        .post("/function2", () -> "hi!")
        .delete("/function3", new Function()::function3);
    // start on 8080 default
    windward.run();
  }

  static class Function {
    public static void function1(WindwardContext windwardContext) {
      windwardContext.string("hello world! function1!");
    }

    public void function2(WindwardContext windwardContext) {
      windwardContext.string("hello world! function2!");
    }

    public void function3(WindwardContext windwardContext) {
      windwardContext.json(new User(1, "esotericman"));
    }
  }

  static class User {
    private final Integer userId;
    private final String userName;

    public Integer getUserId() {
      return userId;
    }

    public String getUserName() {
      return userName;
    }

    public User(Integer userId, String userName) {
      this.userId = userId;
      this.userName = userName;
    }
  }
}
