package org.flmelody;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.flmelody.core.Windward;
import org.flmelody.core.context.EnhancedWindwardContext;
import org.flmelody.core.context.SimpleWindwardContext;
import org.flmelody.core.exception.ServerException;
import org.junit.Test;

/**
 * @author esotericman
 */
public class FunctionTestContext {
  @Test
  public void test() throws ServerException {
    Windward windward = Windward.setup();
    // register static  function
    windward.get("/ee", Function::function0);
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
    public static String function0(EnhancedWindwardContext windwardContext)
        throws IllegalArgumentException {
      User user = windwardContext.bindJson(User.class);
      windwardContext.writeString("hello world! function1!");
      return "";
    }

    public static void function1(SimpleWindwardContext windwardContext) {
      User user = windwardContext.bindJson(User.class);
      windwardContext.writeString("hello world! function1!");
    }

    public void function2(SimpleWindwardContext windwardContext) {
      windwardContext.writeString("hello world! function2!");
    }

    public void function3(SimpleWindwardContext windwardContext) {
      windwardContext.writeJson(new User(1, "esotericman"));
    }
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  static class User {
    @NotNull private Integer userId;
    private String userName;
  }
}
