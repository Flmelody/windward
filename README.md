# windward
![GitHub](https://img.shields.io/github/license/Flmelody/windward)
![Maven Central](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Frepo1.maven.org%2Fmaven2%2Forg%2Fflmelody%2Fwindward%2Fmaven-metadata.xml&query=%2F%2Fmetadata%2Fversioning%2Flatest&label=maven-central)

Light web function framework for Java
<div>
    <img src="https://github.com/Flmelody/windward-guide/blob/main/docs/.vuepress/public/windward.png" alt="windward" width="800" height="600">
</div>

## quick start

### maven dependency

```xml

<dependency>
    <groupId>org.flmelody</groupId>
    <artifactId>windward</artifactId>
    <version>1.3-RELEASE</version>
</dependency>
```

### define function

```shell
  static class Function {
    public static void function1(SimpleWindwardContext windwardContext) {
      windwardContext.writeString("hello world! function1!");
    }

    public void function2(SimpleWindwardContext windwardContext) {
      windwardContext.writeString("hello world! function2!");
    }

    public void function3(SimpleWindwardContext windwardContext) {
      windwardContext.writeJson(new User(1, "esotericman"));
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
```

### register function

```shell
Windward windward = Windward.setup();
// register static  function
windward.get("/function1", Function::function1);
windward.put("/function1", Function::function1);
windward.post("/function1", Function::function1);
windward.delete("/function1", Function::function1);
// register nonstatic  function
// register group function
windward
    .group("/v1")
    .get("/function1", Function::function1)
    .post("/function2", () -> "hi!")
    .delete("/function3", new Function()::function3);
// start on 8080 default
windward.run();
```

### test function

```shell
curl http://127.0.0.1:8080/function1
```

```shell
curl http://127.0.0.1:8080/v1/function2 -X POST
```

```shell
curl http://127.0.0.1:8080/v1/function3 -X DELETE
```

## todo

...
so much and so on
