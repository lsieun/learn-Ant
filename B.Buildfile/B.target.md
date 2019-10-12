# target

<!-- TOC -->

- [1. Description](#1-description)
- [2. Target Dependencies](#2-target-dependencies)
- [3. Circular Dependencies](#3-circular-dependencies)
- [4. Running the build file](#4-running-the-build-file)
  - [4.1. Incremental builds](#41-incremental-builds)
  - [4.2. Running multiple targets on the command line](#42-running-multiple-targets-on-the-command-line)

<!-- /TOC -->

The `<target>` element is a child of `<project>`.

## 1. Description

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project name="hello-ant" default="patch" basedir=".">
    <target name="init" description="==&gt;  Create init Directory">
        <mkdir dir="lib"/>
        <mkdir dir="build/classes"/>
    </target>
</project>
```

## 2. Target Dependencies

```xml
<?xml version="1.0"  encoding="UTF-8"?>
<project name="HelloWorld" default="run" basedir=".">

    <!-- Creates the output directories -->
    <target name="init">
        <mkdir dir="build/classes" />
        <mkdir dir="dist" />
    </target>

    <!-- Compiles into the output directories -->
    <target name="compile" depends="init">
        <javac srcdir="src" destdir="build/classes"/>
    </target>

    <!-- Creates the archive -->
    <target name="archive" depends="compile" >
        <jar destfile="dist/app.jar" basedir="build/classes" />
    </target>

    <!-- Run the Program -->
    <target name="run" depends="archive">
        <java classname="HelloWorld" classpath="dist/app.jar"/>
    </target>

    <!-- Deletes the output directories -->
    <target name="clean" depends="init">
        <delete dir="build" />
        <delete dir="dist" />
    </target>

</project>
```

Ant needs to know in what order it should execute targets.

These are dependencies that we need to communicate to Ant. We do so by listing the direct dependencies in the `depends` attributes of the targets:

```xml
<target name="compile" depends="init" >
<target name="archive" depends="compile" >
<target name="clean" depends="init">
```

If a target directly depends on more than one target, then we list both dependencies, such as `depends="compile,test"`.

In our project, the `archive` target depends upon both `init` and `compile` , but we don’t bother to state the dependency upon `init` because the `compile` target already depends upon it. Put formally: **dependencies are transitive**.<sub>注：依赖的传递性</sub>

What isn’t important is the order of targets inside the build file<sub>注：书写顺序不重要</sub>. Ant reads the whole file before it builds the dependency tree and executes targets. There’s no need to worry about forward references to targets.

Ant builds what is known in computer science circles as a **Directed Acyclic Graph (DAG)**. A **DAG** is a graph in which the link between nodes has a specific direction—here the `depends` relationship—and in which there are no circular dependencies.<sub>注：如果将这种依赖性画成“图”，将会是DAG。</sub>

```txt
xml书写顺序不重要（编辑状态，非运行）  -->  生成target依赖关系生成DAG图（执行前的准备，运行中）  --> 执行相应的target（开始执行，运行中）
```

## 3. Circular Dependencies

What happens if a target directly or indirectly depends on itself? Does Ant loop? Let’s see with a target that depends upon itself:

```xml
<?xml version="1.0" ?>
<project name="loop" default="loop" basedir=".">

    <echo>loop test</echo>

    <target name="loop" depends="loop">
        <echo>looping</echo>
    </target>

</project>
```

Run this and you get informed of an error:

```txt
Buildfile: build.xml
     [echo] loop test

BUILD FAILED
Circular dependency: loop <- loop
```

Any tasks placed in the build files outside of any target will be executed before the target graph is created and analyzed. In our experiment, we had an `<echo>` command outside a target. Ant executes all tasks outside of any target in the order they appear in the build file, before any target processing begins.

## 4. Running the build file

You can simply list **one or more targets** on the command line, so all of the following are valid:

```xml
ant
ant init
ant clean
ant compile
ant archive
```

Calling Ant with no target is the same as calling the target named in the `default` attribute of the `<project>`.

```xml
<project name="structured" default="archive" >

</project>
```

### 4.1. Incremental builds

All of these tasks in the build file check their dependencies, and do nothing if they do not see a need. The `<mkdir>` task doesn’t create directories that already exist, `<javac>` compiles source files when they’re newer than the corresponding `.class` file, and the `<jar>` task compares the time of all files to be added to the archive with the time of the archive itself. No files have been compiled, and the JAR is untouched. This is called an **incremental build**.

If you add the `-verbose` flag to the command line, you’ll get more detail on what did or did not take place. The verbose run provides a lot of information, much of which may seem distracting. When a build is working well, you don’t need it, but it’s invaluable while developing that file.

### 4.2. Running multiple targets on the command line

Developers can run **multiple targets** in a single build, by listing the targets one after the other on the command line. But what happens when you type `ant compile archive` at the command line? Many people would expect Ant to pick an order that executes each target and its dependencies once only: `[init, compile, archive]`. Unix Make would certainly do that, but Ant does not. Instead, it executes each target and dependents in turn, so the actual sequence is `init, compile`, then `init, compile, archive`.

```bash
$ ant compile archive
Buildfile: example/build.xml

init:
    [mkdir] Created dir: example/build/classes
    [mkdir] Created dir: example/dist

compile:
    [javac] Compiling 1 source file to example/build/classes

init:

compile:

archive:
      [jar] Building jar: example/dist/project.jar

BUILD SUCCESSFUL
Total time: 0 seconds
```

Being able to run multiple targets on the command line lets developers type a sequence of operations such as `ant clean execute` to clean the output directory, rebuild everything, and run the program.
