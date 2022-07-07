<div align="center">
<h1>RatOverNATS</h1>
<h4>Fast as rat in server room solution to implement NATS solution</h4>
<img src="https://forthebadge.com/images/badges/made-with-java.svg" alt="Build with java">
<img src="https://forthebadge.com/images/badges/you-didnt-ask-for-this.svg" alt="Who asked">
</div>

## Status:

| Branch  | Tests                                                                                             | Code Quality |
|--------|---------------------------------------------------------------------------------------------------|--------------|
| master  | ![CircleCI](https://img.shields.io/circleci/build/github/Szczurowsky/RatOverNATS/master?style=for-the-badge) | ![CodeFactor Grade](https://img.shields.io/codefactor/grade/github/Szczurowsky/RatOverNATS/master?style=for-the-badge) |

## Usefull links
Helpful links:
- [GitHub issues](https://github.com/Szczurowsky/RatOverNATS/issues)
- [Docs (Beta)](https://docs.szczurowsky.pl/v/ratovernats-wiki/)
- [Javadocs](https://szczurowsky.github.io/RatOverNATS/)

## MineCodes Repository (Maven or Gradle) ️
```xml
<repository>
  <id>minecodes-repository</id>
  <url>https://repository.minecodes.pl/releases</url>
</repository>
```
```groovy
maven { url "https://repository.minecodes.pl/releases" }
```

### Dependencies (Maven or Gradle)
Framework Core
```xml
<dependency>
    <groupId>pl.szczurowsky</groupId>
    <artifactId>rat-over-nats</artifactId>
    <version>1.0.0</version>
</dependency>
```
```groovy
implementation 'pl.szczurowsky:RatOverNats:1.0.0'
```

## Usage
More complex examples in docs

### Connect

<details>
<summary>With credentials</summary>

```java
public class Example {
    
    public Example() {
        RatOverNats ratOverNats = new RatOverNatsBuilder()
                .options(
                        new Options.Builder()
                                .server("nats://localhost:4222")
                                .userInfo("login", "password")
                                .build()
                )
                .build();
    }
    
}

```

</details>

<details>
<summary>With URI</summary>

```java
public class Example {

    public Example() {
        RatOverNats ratOverNats = new RatOverNatsBuilder()
                .uri("nats://localhost:4222")
                .build();
    }
    
}

```

</details>

### Registering subscribe handler

<details>
<summary>Class with instance</summary>

```java
public class Example {

    public Example() {
        RatOverNats ratOverNats = new RatOverNatsBuilder()
                .options(
                        new Options.Builder()
                                .server("nats://localhost:4222")
                                .userInfo("login", "password")
                                .build()
                )
                .registerHandler(new TestMessageHandler())
                .build();
    }
    
}

```

</details>

<details>
<summary>Handler</summary>

```java
public class TestMessageHandler extends RatMessageHandler<String> {

    protected TestMessageHandler() {
        super(1, "test");
    }

    @Override
    protected void onReceive(String s, Message message) {
        System.out.println(s);
    }
}

```

</details>

### Publish to channel

<details>
<summary>Class with instance</summary>

```java
public class Example {

    public Example() {
        RatOverNats ratOverNats = new RatOverNatsBuilder()
                .options(
                        new Options.Builder()
                                .server("nats://localhost:4222")
                                .userInfo("login", "password")
                                .build()
                )
                .registerHandler(new TestMessageHandler())
                .build();
        ratOverNats.publish("test", new Packet<>("test").setPacketId(1));
    }
    
}

```

</details>