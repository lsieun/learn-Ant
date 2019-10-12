# Cachepath and retrieve

<!-- TOC -->

- [1. Apache Ivy Cachepath Task](#1-apache-ivy-cachepath-task)
  - [1.1. Artifacts location](#11-artifacts-location)
- [2. Apache Ivy Retrieve Task](#2-apache-ivy-retrieve-task)
  - [2.1. Type](#21-type)
  - [2.2. Ivy Retrieve Pattern](#22-ivy-retrieve-pattern)

<!-- /TOC -->

The `<ivy:resolve>` task resolves the dependencies indicated in `ivy.xml` file and places the resolved modules in the cache. But it does not link **the dependencies** to **the project build path**.

To expose the dependencies to the Java build path, Apache Ivy provides two Ant tasks:

- `<ivy:cachepath>` task
- `<ivy:retrieve>` task

## 1. Apache Ivy Cachepath Task

The `<ivy:cachepath>` task constructs an Ant classpath consisting of resolved artifacts in the cache.

Ant executed `<ivy:resolve>` which resolved `commons-langs 2.6` module and placed the artifacts in the cache. To compile Java class files, Ant require a classpath that refers to the artifacts in the cache, and Ivy `Cachepath` task constructs the required classpath.

File: `build.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project name="ivy example" default="compile" xmlns:ivy="antlib:org.apache.ivy.ant">
    <target name="resolve" description="==>  resolve dependencies with ivy">
        <ivy:resolve/>
        <ivy:cachepath pathid="default.classpath"/>
    </target>

    <target name="compile" depends="resolve" description="==> Compile">
        <mkdir dir="build/classes"/>
        <javac srcdir="src" destdir="build/classes" includeantruntime="false">
            <classpath refid="default.classpath"/>
        </javac>
    </target>
</project>
```

In the `resolve` target, we have added `<ivy:cachepath pathid=“default.classpath”/>` after `<ivy:resolve>`. After a successful resolve, Ivy constructs Ant classpath named `default.classpath`. This classpath points to the resolved artifacts in the cache and in sample project it points `commons-lang.jar` in cache. Next in `<javac>` task, we refer `default.classpath` through attribute `refid`. When Ant executes `<javac>` task it finds resolved artifacts in the classpath and compile goes through.

### 1.1. Artifacts location

It is important to note that, neither `<ivy:cachepath>` nor `<ivy:resolve>` copy the artifacts from **cache** to **the project workspace**. Artifacts continue to be in the cache. and `<ivy:cachepath>` simply constructs a classpath that points to artifacts in the cache.

In case, task `<ivy:cachepath>` is called directly without a `<ivy:resolve>` then `<ivy:resolve>` is internally called before executing `<ivy:cachepath>`.

## 2. Apache Ivy Retrieve Task

Instead of `<ivy:cachepath>` task, better approach is to copy **the dependencies** to **the project workspace** and use the standard Ant classpath mechanism to build the project. Apache Ivy Retrieve task comes handy to do that.

Task `<ivy:retrieve>` copies **resolved dependencies** to **a specified location** in the project workspace.

File: `build.xml`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project name="ivy example" default="compile" xmlns:ivy="antlib:org.apache.ivy.ant">
    <path id="compile.classpath">
        <fileset dir="lib" includes="*.jar"/>
    </path>

    <target name="retrieve" description="==>  Resolve and Retrieve with ivy">
        <ivy:resolve />
        <ivy:retrieve sync="true" type="jar" />
    </target>

    <target name="compile" depends="retrieve" description="==>  Compile">
        <mkdir dir="build/classes"/>
        <javac srcdir="src" destdir="build/classes" includeantruntime="false">
            <classpath refid="compile.classpath"/>
        </javac>
    </target>
</project>
```

We have added `<ivy:retrieve sync="true" type="jar"/>` after `<ivy:resolve>` which instructs Ivy to copy the resolved artifacts from cache to `lib` directory within the project workspace. Ivy will create the `lib` directory if it is not there.

Important attributes that are used in this task are

- `sync`: set to `true` will ensure that any extra files in the `lib` directory is deleted.
- `type`: set to `jar` tells ivy to copy only **jar artifacts**. **Source and javadoc artifacts** are ignored.

### 2.1. Type

Frequently used types are `jar` for jar artifact, `src` or `source` for Java source artifact and `doc` for javadoc artifact. But some modules like `log4j`, refer jar artifacts as **bundle** instead of **jar**. To include `log4j` artifacts, you need to change the `type` attribute to `<ivy:retrieve sync="true" type="jar,bundle"/>`

In case `<ivy:retrieve>` is called directly without a `<ivy:resolve>`, then `<ivy:resolve>` is internally called before retrieving the artifacts.

Once the dependencies are in project workspace, use standard ant path creation to build the project.

### 2.2. Ivy Retrieve Pattern

By default `<ivy:retrieve>` places the retrieved artifacts in `lib` directory of the project and may change this by using Ivy Retrieve `pattern` attribute.

```xml
<ivy:retrieve sync="true" type="jar" pattern="myfolder/[artifact]-[revision].[ext]"/>
```

Attribute pattern tells Ivy Retrieve to copy the artifacts to `myfolder` directory.
