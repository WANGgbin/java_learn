描述 maven 相关内容。

# 解决什么问题

# setting.xml

# plugin

maven 的一大功能就是负责 java 项目的自动构建、测试、部署等流程。maven 定义了 3 个核心的概念，来构建这个流程。

## lifecycle

maven 预定义的一组有序的构建阶段。主要的生命周期就两个：default、clean.

- default
  - verify
  - compile
  - test
  - package
  - install
  - deploy

- clean
  - pre-clean
  - clean
  - post-clean

## phase

每个生命周期包含若干个阶段。通常我们在 mvn 中指定的就是 phase。这里需要注意：mvn phase 并不是执行某个 phase，而是执行该 phase 之前的所有 phase。

## goal

phase 是一个抽象的概念，每个 phase 包含若干个 goal，真正的逻辑由 goal 执行。

每个 goal 由具体的插件(也是一个 java 项目)实现。 maven 通过调用对应的插件来完成 goal 的执行。

我们可以在 pom.xml 的 <plugin> 自定义 maven phase 执行的 goal。比如：

```xml
<build>
    <!-- 声明插件 -->
  <plugins>
    <!-- 插件 A -->
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-jar-plugin</artifactId>
      <executions>
        <execution>
          <phase>package</phase>
          <goals>
            <goal>jar</goal>
          </goals>
        </execution>
      </executions>
    </plugin>

    <!-- 插件 B -->
    <plugin>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-maven-plugin</artifactId>
      <executions>
        <execution>
          <phase>package</phase>
          <goals>
            <goal>repackage</goal>
          </goals>
        </execution>
      </executions>
    </plugin>

    <!-- 插件 C -->
    <plugin>
      <groupId>com.example</groupId>
      <artifactId>my-custom-plugin</artifactId>
      <executions>
        <execution>
          <phase>package</phase>
          <goals>
            <goal>do-something</goal>
          </goals>
        </execution>
      </executions>
    </plugin>
  </plugins>
</build>
```

当我们在 pom.xml 中加入以上配置后，当我们执行 `mvn package` 的时候，maven 会按照上述插件声明的顺序，来
执行各个 goal，比如：jar、repackage、do-something.

我们也可以通过 `mvn pluginPrefix:goal` 的方式来执行某个具体的 goal。maven 是如何通过 pluginPrefix 推导出对应的插件的呢？

核心就是 maven 的约定大于配置，约定是：对应插件的 artifactId 是 pluginPrefix-maven-plugin，maven 首先会尝试在 pom.xml 的 plugin
中找对应的插件，如果找不到，则会根据 setting.xml 中 pluginGroup 的配置找插件，如果还是找不到就报错。

本质来说，pluginPrefix 就是一种方便指定插件的方式，当然我们也可以完全指定插件的 groupID:artifactID:version 来执行 goal。

TODO: 可以自己从 0 实现一个 plugin。

# module

当一个 java 项目比较大的时候，我们可以按功能拆成若干个子项目，这样各个模块低耦合、高内聚，方便后续的维护、迭代。

在 maven 中，子项目就是 module，每个 module 有单独的 pom.xml。我们在项目的根目录下，创建一个聚合 pom.xml，内部通过 <modules>
指定各个子项目。当我们执行 mvn 命令时，maven 就会按照声明的 module 顺序，来处理每一个 module。

其实同一个项目很多信息都是相同的，比如 java 版本、plugin 等信息。如果在每个 module 的 pom.xml 中都声明一次，就显得有点繁琐。我们可以将
pom.xml 中的公共信息放到根目录的 pom.xml 中，然后各个子模块的 pom.xml 通过 <parent> 来引用该 parent pom.xml.

parent 声明父 pom.xml 语法如下：
```xml
<parent>
    <groupId>com.example</groupId>
    <artifactId>my-parent</artifactId>
    <version>1.0.0</version>
    <relativePath>../pom.xml</relativePath> <!-- 可选，用于本地查找 -->
  </parent>
```

# properties

## 解决什么问题

重复出现的值，提取为 property，一处更改，处处生效。

## 语法

- 声明

```xml
<properties>
  <my.custom.property>some-value</my.custom.property>
</properties>
```

- 引用

通过 `${my.custom.property}` 的方式引用。

## 注意事项

在 mvn 的命令行中，可以通过 -Dmy.custom.property=value 的方式覆盖 pom.xml 中指定的值。
