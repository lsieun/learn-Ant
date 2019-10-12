# Resolve task

<!-- TOC -->

- [1. Intro](#1-intro)
- [2. Example](#2-example)
- [3. Apache Ivy Cache](#3-apache-ivy-cache)

<!-- /TOC -->

## 1. Intro

The `resolve` task actually resolve dependencies described in **an ivy file**, and put the resolved dependencies in **the ivy cache**.

With no attributes,  the `resolve` task will use default settings and look for a file named `ivy.xml` for the dependency definitions.

The `resolve` task **resolves dependencies** and **downloads** them to a **cache**.

## 2. Example

Resolve task, resolve dependencies described in `ivy.xml` and places **the resolved dependencies** in **the Apache Ivy cache**.

File: `example/HelloWorld.java`

```java
package example;

import org.apache.commons.lang.StringUtils;

public class HelloWorld {
    public static void main(String[] args) {
        String string = StringUtils.upperCase("Ivy Beginner Guide");
        System.out.println(string);
    }
}
```

File: `ivy.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<ivy-module version="2.0"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:noNamespaceSchemaLocation="http://ant.apache.org/ivy/schemas/ivy.xsd">
    <info organisation="cn.lsieun" module="ivy-example" status="integration"/>
    <dependencies>
        <dependency org="commons-lang" name="commons-lang" rev="2.6"/>
    </dependencies>
</ivy-module>
```

Ivy reads this file and manage the dependencies. It provides info about the sample project and its dependencies and has 3 sections

- (1) `ivy-module`: this is a standard root element with version,schema etc.
- (2) `info`: this section is information about the project.
  - `organisation`: organisation or company.
  - `module`: name of the project
  - `status`: release status – milestone, integration or release.
- (3) `dependencies`: one or more dependencies for this project.
  - `dependency`:
    - `org`: organisation which provides the module
    - `name`: module name
    - `rev`: revision or version of the module

To sum up, in `ivy.xml` we are indicating that our project is dependent on **commons-lang** revision `2.6`.

File: `build.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project name="ivy example" default="resolve" xmlns:ivy="antlib:org.apache.ivy.ant">
    <target name="resolve" description="==>  resolve dependencies with ivy">
        <ivy:resolve />
    </target>
</project>
```

Resolve task is defined through `<ivy:resolve/>` within `<target>` element. When Ant encounters this element, it calls Apache Ivy Resolve task which **resolves the dependencies** described in `ivy.xml` and places **the resolved dependencies** in **its cache**.

That completes the Apache Ivy and Ant configuration. Next, run Ant from work directory.

```bash
ant
```

Output:

```txt
---------------------------------------------------------------------
|                  |            modules            ||   artifacts   |
|       conf       | number| search|dwnlded|evicted|| number|dwnlded|
---------------------------------------------------------------------
|      default     |   1   |   0   |   0   |   0   ||   3   |   0   |
---------------------------------------------------------------------
```

- `conf`: ivy is using `default` configuration
- `modules`: indicated by `number` and `dwnlded` columns, one module is resolved and downloaded.
- `artifacts`: indicated by the columns under artifacts, module contains 3 artifacts, i.e. `jar`, `source` and `javadoc` and Ivy has downloaded those three artifacts.

In this example, we have not specified the location where Ivy has to look for the modules. But how come Ivy is able to complete the task. In the absence of specific settings, Apache Ivy falls back on default settings that comes bundled with it. For a repository location, it uses a concept known as **Resolver** and **default public resolver** points to http://repo1.maven.org/maven2. This default setting enabled Ivy to resolve and fetch the module.

In case a module itself has other dependencies, then Ivy will fetch all required dependencies. These are known as **transitive dependencies**. For example, `log4j` depends on other modules like `javax.mail`, `junit`, `jermanio.spec` and `oro` etc., Dependency line `<dependency org=“log4j” name=“log4j” rev=“1.2.16”/>` will download `log4j` plus four dependent modules. By adding `transitive="false"` to dependency line, we can force the Ivy not to download the other transitive dependencies. Dependency line `<dependency org=“log4j” name=“log4j” rev=“1.2.16” transitive=“false”/>` downloads just `log4j`.

## 3. Apache Ivy Cache

On first run, `<ivy:resolve>` fetches the artifacts from **the public repository** and place them in **the cache**. During subsequent calls to `<ivy:resolve>` either from this project or any other project, Ivy finds the module in cache and will not download it from public repository. Through the cache, Ivy speeds up the time required for resolve and also saves bandwidth usage. By default, Apache Ivy cache is at `$HOME/.ivy2`. Beyond that, we need not bother much about the internals of the cache.

With the `<ivy:resolve>`, we are able to **resolve the dependencies** and **place them in the cache**.

