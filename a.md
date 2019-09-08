# Introducing Ant

## WHAT IS ANT?

Ant is a build tool, a small program designed to help software teams develop big programs by automating all the drudge-work tasks of compiling code, running tests, and packaging the results for redistribution.

Ant is written in Java<sub>注：使用的语言</sub> and is designed to be cross-platform, easy to use, extensible, and scalable<sub>注：设计的目标</sub>. It can be used in a small personal project, or it can be used in a large, multiteam software project<sub>注：可适用的项目范围</sub>. It aims to automate your entire build process.<sub>注：总而言之，就是自动化program的构建过程</sub>

Ant has an XML syntax.

### The core concepts of Ant

The first is the design goal: Ant was designed to be an extensible tool to automate the build process of a Java development project.

A **software build process** is a means of going from your source—code and documents—to the product you actually deliver. If you have a software project, you have a build process, whether or not you know it.

Here are the core concepts of Ant as seen by a user of the tool.

#### Build Files

Ant uses XML files called **build files** to describe how to build a project. In the build file developers list the high-level various goals of the build—the **targets**—and actions to take to achieve each goal—the **tasks**.

#### A build file contains one project

Each build file describes how to build one project. Very large projects may be composed of multiple smaller projects, each with its own build file. A higher-level build file can coordinate the builds of the subprojects.

#### Each project contains multiple targets

Within the build file’s single project, you declare different targets. These targets may represent actual outputs of the build, such as a redistributable file, or activities, such as compiling the source or running the tests.

#### Targets can depend on other targets

When declaring a target, you can declare which targets have to be built first. This can ensure that the source gets compiled before the tests are run and built, and that the application is not uploaded until the tests have passed. When Ant builds a project, it executes targets in the order implied by their dependencies.

#### Targets contain tasks

Inside targets, you declare what work is needed to complete that stage of the build process. You do this by listing the tasks that constitute each stage. When Ant executes a target, it executes the tasks inside, one after the other.

#### Tasks do the work

Ant tasks are XML elements, elements that the Ant runtime turns into actions. Behind each task is a Java class that performs the work described by the task’s attributes and nested data. These tasks are expected to be smart—to handle much of their own argument validation, dependency checking, and error reporting.

#### New tasks extend Ant

The fact that it’s easy to extend Ant with new classes is one of its core strengths. Often, someone will have encountered the same build step that you have and will have written the task to perform it, so you can just use their work. If not, you can extend it in Java, producing another reusable Ant task or datatype.

To summarize, Ant reads in a build file containing a project. In the project are targets that describe different things the project can do. Inside the targets are the tasks, tasks that do the individual steps of the build. Ant executes targets in the order implied by their declared dependencies, and the tasks inside them, thereby building the application. That’s the theory.

When the project is built, Ant determines which targets need to be executed, and in what order. Then it runs the tasks inside each target. If a task somehow fails, Ant halts the build.

```xml
<?xml version="1.0" ?>
<project name="ourproject" default="deploy">

<!-- Create two output directories for generated files -->
<target name="init">
    <mkdir dir="build/classes" />
    <mkdir dir="dist" />
</target>

<!-- Compile the Java source -->
<target name="compile" depends="init">
    <javac srcdir="src" destdir="build/classes"/>
</target>

<!-- Create the javadocs of all org.* source files -->
<target name="doc" depends="init" >
    <javadoc destdir="build/classes" sourcepath="src" packagenames="org.*" />
</target>

<!-- Create a JAR file of everything in build/classes -->
<target name="package" depends="compile,doc" >
    <jar destfile="dist/project.jar" basedir="build/classes"/>
</target>

<!-- Upload all files in the dist directory to the ftp server -->
<target name="deploy" depends="package" >
    <ftp server="${server.name}" userid="${ftp.username}" password="${ftp.password}">
        <fileset dir="dist"/>
    </ftp>
</target>

</project>
```

ftp.properties

```txt
server.name=
ftp.username=
ftp.password=
```

```bash
ant -propertyfile ftp.properties
```

This example shows Ant’s basics well: **target dependencies**, **use of properties**, **compiling**, **documenting**, **packaging**, and, finally, **distribution**.

Because Ant tasks are Java classes, the overhead of invoking each task is quite small. For each task, Ant creates a Java object, configures it, then calls its `execute()` method. **A simple task** such as `<mkdir>` would call a Java library method to create a directory. **A more complex task** such as `<ftp>` would invoke a third-party FTP library to talk to the remote server, and, optionally, perform dependency checking to upload only files that were newer than those at the destination. **A very complex task** such as `<javac>` not only uses dependency checking to decide which files to compile, it supports multiple compiler back ends, calling Sun’s Java compiler in the same Java Virtual Machine (JVM), or executing a different compiler as an external executable.






