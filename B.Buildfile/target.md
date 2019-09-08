# target

<!-- TOC -->

- [1. attributes](#1-attributes)
  - [1.1. depends](#11-depends)
  - [1.2. Target dependencies](#12-target-dependencies)
  - [1.3. Interlude: circular dependencies](#13-interlude-circular-dependencies)
- [2. sub elements](#2-sub-elements)

<!-- /TOC -->

## 1. attributes

### 1.1. depends

### 1.2. Target dependencies

```xml
<?xml version="1.0"?>
<project name="structured" default="archive" >

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
    <jar destfile="dist/project.jar" basedir="build/classes" />
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

In our project, the `archive` target depends upon both `init` and `compile` , but we don’t bother to state the dependency upon `init` because the `compile` target already depends upon it. If Ant must execute `init` before `compile` and `archive` depends upon `compile` , then Ant must run `init` before `archive` . Put formally: **dependencies are transitive**.

What isn’t important is the order of targets inside the build file. Ant reads the whole file before it builds the dependency tree and executes targets. There’s no need to worry about forward references to targets.

Before Ant executes any target, it executes all its predecessor targets. If these predecessors depend on targets themselves, Ant considers those and produces an order that satisfies all dependencies. If two targets in this execution order share a common dependency, then that predecessor will execute only once.

Ant builds what is known in computer science circles as a **Directed Acyclic Graph (DAG)**. A **DAG** is a graph in which the link between nodes has a specific direction—here the `depends` relationship—and in which there are no circular dependencies.

### 1.3. Interlude: circular dependencies

What happens if a target directly or indirectly depends on itself? Does Ant loop? Let’s see with a target that depends upon itself:

```xml
<?xml version="1.0" ?>
<project name="loop" default="loop" >

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

With a loop-free build file written, Ant is ready to run it.

## 2. sub elements





