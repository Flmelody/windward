# Windward

![GitHub](https://img.shields.io/github/license/Flmelody/windward)
![Maven Central](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Frepo1.maven.org%2Fmaven2%2Forg%2Fflmelody%2Fwindward%2Fmaven-metadata.xml&query=%2F%2Fmetadata%2Fversioning%2Flatest&label=maven-central)

Light web function framework for Java
<div>
    <img src="https://github.com/Flmelody/windward-guide/blob/main/docs/.vuepress/public/windward.png" alt="windward" width="600" height="400">
</div>

## Quick start

### Add maven dependency

```xml

<dependency>
    <groupId>org.flmelody</groupId>
    <artifactId>windward</artifactId>
    <version>1.4.3-RELEASE</version>
</dependency>
```

use jackson

```xml

<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>${jackson-databind.version}</version>
</dependency>
```

or gson

```xml

<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>${gson.version}</version>
</dependency>
```

### Coding

We need to define our functions at first

```java
public class Controller {
  public void simpleFunction(SimpleWindwardContext simpleWindwardContext) {
    simpleWindwardContext.writeString("Simple Hello World!");
  }

  public static void staticFunction(SimpleWindwardContext simpleWindwardContext) {
    simpleWindwardContext.writeString("static Hello World!");
  }

  public void dynamicFunction(SimpleWindwardContext simpleWindwardContext) {
    Map<String, Object> pathVariables = simpleWindwardContext.windwardRequest().getPathVariables();
    pathVariables.keySet().forEach(key -> System.out.println(key + "->" + pathVariables.get(key)));
    simpleWindwardContext.writeString("Dynamic Hello World!");
  }

  public void redirectFunction(SimpleWindwardContext simpleWindwardContext) {
    simpleWindwardContext.redirect("https://github.com/Flmelody/windward");
  }

  public String enhancedFunction(EnhancedWindwardContext enhancedWindwardContext) {
    enhancedWindwardContext.writeString("Enhanced Hello World!");
    return "";
  }
}

```

Now, We just start it

```java
public class WindwardMain {
  public static void main(String[] args) {
    Windward windward = Windward.setup();
    Controller controller = new Controller();
    windward
        .get("/simple", controller::simpleFunction)
        .get("/static", Controller::staticFunction)
        .get("/dynamic/{name}/{age}", controller::dynamicFunction)
        .get("/redirect", controller::redirectFunction)
        .get("/enhanced", controller::enhancedFunction);
    windward.run();
  }
}

```

Everything is fine!😇
