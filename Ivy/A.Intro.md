# Intro

<!-- TOC -->

- [1. 问题](#1-%e9%97%ae%e9%a2%98)
- [2. How does it work?](#2-how-does-it-work)
  - [2.1. Configure](#21-configure)
  - [2.2. Resolve](#22-resolve)
  - [2.3. Retrieve](#23-retrieve)
  - [2.4. Building a path from the cache](#24-building-a-path-from-the-cache)
  - [2.5. Reports](#25-reports)
  - [2.6. Publish](#26-publish)
- [3. ivy.xml](#3-ivyxml)
- [4. build.xml](#4-buildxml)
- [5. Maven 2 repository](#5-maven-2-repository)
- [6. Cache](#6-cache)

<!-- /TOC -->

Apache Ivy is highly configurable and a powerful piece of software. The Ivy official site has some excellent tutorials and detailed reference documentation, but multiple configurable options and coupled with some of the difficult concepts overwhelms the beginners, who usually find it difficult to get them right in the first go.

## 1. 问题

- 配置：如何配置maven仓库的呢？
- 解析（下载）：下载到哪里了？

## 2. How does it work?

Usual cycle of modules between different locations:

![Usual cycle of modules between different locations](images/main-tasks.png)

### 2.1. Configure

Ivy needs to be configured to be able to resolve your dependencies. This configuration is usually done with a settings file, which defines a set of **dependency resolvers**. Each resolver is able to find Ivy files and/or artifacts, given simple information such as organisation, module, revision, artifact name, artifact type and artifact extension.

```txt
Ivy --> configure(file) --> dependencies
configure file --> dependency resolvers --> artifacts
```

**The configuration** is also responsible for indicating **which resolver** should be used to resolve **which module**. This configuration is dependent only on your environment, i.e. where the modules and artifacts can be found.

**A default configuration** is used by Ivy when none is given. This configuration uses an [ibiblio resolver](http://ant.apache.org/ivy/history/2.5.0-rc1/resolver/ibiblio.html) pointing to [https://repo1.maven.org/maven2/](https://repo1.maven.org/maven2/) to resolve all modules.

### 2.2. Resolve

The resolve time is the moment when Ivy actually resolves the dependencies of one module. It first needs to access the Ivy file of the module for which it resolves the dependencies.

Then, for each dependency declared in this file, it asks the appropriate resolver (according to settings) to find the module (i.e. either an Ivy file for it, or its artifacts if no Ivy file can be found). It also uses a filesystem based cache to avoid asking for a dependency if it is already in cache (at least if possible, which is not the case with latest revisions).

If the resolver is a composite one (i.e. a chain or a dual resolver), several resolvers may actually be called to find the module.

When **the dependency module** has been found, its Ivy file is downloaded to **the Ivy cache**. Then Ivy checks if the dependency module has dependencies, in which case it recursively traverses the graph of dependencies.

All over this traversal, **conflict management** is done to prevent access to a module as soon as possible.

When Ivy has traversed the whole graph, it asks the **resolvers** to download **the artifacts** corresponding to each of the dependencies which are not already in** the cache** and which have not been evicted by **conflict managers**. All downloads are made to the Ivy cache.

Finally, **an XML report** is generated in **the cache**, which allows Ivy to easily know what are all the dependencies of a module, without traversing the graph again.

After this resolve step, two main steps are possible: either build a path with artifacts in the cache, or copy them to another directory structure.

### 2.3. Retrieve

What is called retrieve in Ivy is **the act of copying artifacts** from **the cache** to **another directory structure**. This is done using a pattern, which indicates to Ivy where the files should be copied.

For this, Ivy uses the XML report in the cache corresponding to the module it should retrieve to know which artifacts should be copied.

It also checks if the files are not already copied to maximize performances.

### 2.4. Building a path from the cache

In some cases, it is preferable to use artifacts directly from the cache. Ivy is able to use the XML report generated at resolve time to build a path of all artifacts required.

This can be particularly useful when building plug-ins for IDEs.

### 2.5. Reports

Ivy is also able to generate readable reports describing the dependencies resolution.

This is done with a simple XSL transformation of the XML report generated at resolve time.

### 2.6. Publish

Finally, Ivy can be used to publish a particular version of a module in your repository, so that it becomes available for future resolving. This task is usually called either manually or from a continuous integration server.

## 3. ivy.xml

## 4. build.xml




## 5. Maven 2 repository

With no specific settings, Ivy uses the **Maven 2 repository** to resolve the dependencies you declare in an Ivy file.

Ivy uses the **Maven 2 central repository** by default, so we recommend you use [mvnrepository.com](https://mvnrepository.com/) to look for the module you want.

## 6. Cache

The Ivy download all corresponding artifacts in its cache (by default in your user home, in a `.ivy2/cache` directory).

Finally, the `<retrieve>` task copies the resolved jars from **the Ivy cache** to **the default library directory of the project**: the `lib` dir (you can change this easily by setting the `pattern` attribute on the `retrieve` task).


