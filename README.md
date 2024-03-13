# Windward

![GitHub](https://img.shields.io/github/license/Flmelody/windward)
![Maven Central](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Frepo1.maven.org%2Fmaven2%2Forg%2Fflmelody%2Fwindward%2Fmaven-metadata.xml&query=%2F%2Fmetadata%2Fversioning%2Flatest&label=maven-central)

Light web framework for Java
<div>
    <img src="https://github.com/Flmelody/windward-guide/blob/main/docs/.vuepress/public/windward.png" alt="windward" width="360" height="240">
</div>

## Quick start

### Add maven dependency

```xml

<dependency>
    <groupId>org.flmelody</groupId>
    <artifactId>windward</artifactId>
    <version>1.5.0-RELEASE</version>
</dependency>
```

use jackson

```xml

<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.16.1</version>
</dependency>
```

or gson

```xml

<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.10.1</version>
</dependency>
```

### Coding

```java
public class Application {
    public static void main(String[] args) {
        // started at 8080 default
        Windward.setup().get("/", () -> "Hello World!").run();
    }
}

```

Everything is fine!😇

### Benchmark

![rps.png](rps.png)
