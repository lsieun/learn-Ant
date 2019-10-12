# Third-party Tasks

<!-- TOC -->

- [1. Finding the tasks](#1-finding-the-tasks)
- [2. Adding the libraries to Ant’s classpath](#2-adding-the-libraries-to-ants-classpath)
- [3. Telling Ant about the tasks](#3-telling-ant-about-the-tasks)
  - [3.1. Defining tasks with `<taskdef>`](#31-defining-tasks-with-taskdef)
  - [3.2. Declaring tasks defined in property files](#32-declaring-tasks-defined-in-property-files)
  - [3.3. Defining tasks into a unique namespace](#33-defining-tasks-into-a-unique-namespace)
  - [3.4. Defining tasks from an Antlib](#34-defining-tasks-from-an-antlib)

<!-- /TOC -->

The Ant team maintains the Ant runtime and the many **core** and **optional tasks**, but it lacks the time and skills to maintain all the other tasks that projects may need. **Third-party tasks** are a common addition to many Ant-based projects.

**Third-party tasks**, like optional tasks, are distributed in JAR files. Traditionally, you’ve had to modify Ant build files to tell Ant about any new types in the JAR by using the `<tasksdef>` and `<typedef>` tasks.

Since Ant 1.6, a new task-loading mechanism, **Antlib**, makes this process much simpler. An Antlib is a JAR file whose type and task declarations are picked up by Ant when you declare the appropriate package URL in an XML namespace declaration. The mechanism also lets library developers declare more than just the tasks; they can declare datatypes and define tasks in scripting language or XML, rather than just Java. The Ant project itself is starting to distribute extension libraries as Antlibs, blurring the distinction between optional and third-party tasks.

Three steps are all it takes to make third-party tasks usable in a build file:

- (1) Finding the tasks
- (2) Adding the libraries to Ant’s classpath
- (3) Telling Ant about the tasks

## 1. Finding the tasks

The first step is finding the tasks you want.

[Ant External Tools and Tasks](http://ant.apache.org/external)

## 2. Adding the libraries to Ant’s classpath

The Ant tasks should come in a JAR file, perhaps with some other library dependencies, and they need to be on the classpath. The standard ways to make this happen are

- Drop the JAR files into `ANT_HOME/lib`. Do this only if the library is to be used by all users of a machine on all projects. Otherwise, it may lead to library version conflicts.
- Drop the JAR files into `${user.home}/.ant/lib`. This is per-user, but it still applies across all projects and is still dangerous.
- Keep the files in a private directory and add the directory’s contents to the path via Ant’s `-lib` option. You can include this directory in the `ANT_ARGS` environment variable for automatic inclusion.

Adding the files to Ant’s classpath doesn’t make the tasks automatically available inside the build file. Every build file that uses a third-party task must tell Ant to load the tasks, providing the names and/or XML namespaces to use. This is called **defining a task**, which can be done in several ways.

## 3. Telling Ant about the tasks

### 3.1. Defining tasks with `<taskdef>`

Ant is preconfigured with the knowledge of which Java class implements each of the core and optional tasks. To use a new third-party task in a build file, you need to tell Ant about it. This is what the core tasks `<taskdef>` and `<typedef>` do. To define a single task, you must specify **the task’s name** and **the class behind it**. The task name can be arbitrary, but it must be unique within the build file.

Given the name of the implementing class, we can tell Ant to bind `<if>` to this class using `<taskdef>`:

```xml
<taskdef name="if"
    classname="net.sf.antcontrib.logic.IfTask"
    classpath="${ant-contrib.jar}"/>
```

To use the other tasks in the library, we could add other `<taskdef>` declarations, but this is needless work. It’s better if the task library lists all its tasks in a properties file, which can be loaded in bulk.

### 3.2. Declaring tasks defined in property files

The Ant-contrib tasks are all listed in a properties file stored as a resource in the JAR: `net/sf/antcontrib/antcontrib.properties`. It lists the tasks in the JAR and the names by which they should be used, including the `<if>` task we declared earlier. Here’s a fragment of the file:

```txt
# Logic tasks
if=net.sf.antcontrib.logic.IfTask
foreach=net.sf.antcontrib.logic.ForEach
throw=net.sf.antcontrib.logic.Throw
trycatch=net.sf.antcontrib.logic.TryCatchTask
switch=net.sf.antcontrib.logic.Switch
```

The `<taskdef>` command can do the bulk of this entire file, defining all the tasks in one go:

```xml
<taskdef resource="net/sf/antcontrib/antcontrib.properties"/>
    <classpath refid="tasks.classpath"/>
</taskdef>
```

Some projects also define datatypes in a separate file, which are declared with a `<typedef>` statement:

```xml
<typedef resource="org/example/types.properties">
    <classpath refid="tasks.classpath"/>
</typedef>
```

This statement will declare the Ant datatypes, but do this in a new classloader, not the one used for the tasks, even though the same classpath was used in both declarations. **Having the classes loaded in separate classloaders can stop the tasks from using the types properly**. To load them in the same classloader, set the `loaderref` attribute of both `<typedef>` and `<taskdef>` declarations to the same string. It doesn’t matter what the string is, only that it’s a unique name for the shared classloader.

Bulk loading of tasks through a properties file is efficient, and most third-party libraries come with a properties file for this purpose. **One risk with this mechanism** is that as the properties files name the tasks, there is no way to prevent clashes between the names of tasks defined in different libraries. This may seem unlikely, but as Ant continually adds new tasks and types, over time it’s almost inevitable—unless we do something to avoid the problem. We need a way of telling Ant about third-party tasks yet keeping them separate from Ant’s own tasks. **This is where XML namespaces can help**.

### 3.3. Defining tasks into a unique namespace

**The basic rules of XML namespaces**: **XML elements and attributes** can be declared in a **namespace**, by first defining a **prefix** mapping to a **URI** , then qualifying the element or attribute declaration with the **prefix**.

```txt
Task or Type --> URI(namespace) --> prefix --> <prefix:element_name>
```

To declare a task or type in a namespace, set the `uri` attribute of the `<taskdef>` or `<typedef>` to **the namespace** you want the task to be defined in. One complication with XML namespaces is that **the namespace** needs to be declared, either in **the element using the namespace**, or in **an XML element that contains it**.

If you want to load an **antlib** into **a special XML namespace**, the `uri` attribute is important:

```xml
<project xmlns:antcontrib="antlib:net.sf.antcontrib">
     <taskdef uri="antlib:net.sf.antcontrib"
              resource="net/sf/antcontrib/antlib.xml"
              classpath="path/to/ant-contrib.jar"/>
</project>
```

Here the namespace declaration `xmlns:antcontrib="antlib:net.sf.antcontrib"` allows tasks and types of the Ant-Contrib Antlib to be used with the `antcontrib` prefix like `<antcontrib:if>`.

### 3.4. Defining tasks from an Antlib

Prior to Ant 1.6, the main way for **declaring third-party tasks** was via **property files**. This has now been supplemented with **Antlibs**, which are JARs containing **Ant tasks and types**—tasks and types that are listed in an XML file.

Antlib task declarations are much better because

- A single Antlib XML file can contain `<taskdef>`, and `<typedef>` declarations of tasks, types, and conditions, and `<macrodef>` and `<presetdef>` declarations that build up tasks from other Ant tasks.
- Everything in the library is loaded in **the same classloader**.
- Ant can automatically load Antlib declarations from special `antlib:URIs`.

```xml
<project name="structured" default="identify-SCM" basedir="." xmlns:ac="antlib:net.sf.antcontrib">

    <property name="ant-contrib.jar" location="${user.home}/lib/ant/ant-contrib.jar"/>

    <target name="load-ant-contrib">
        <taskdef resource="net/sf/antcontrib/antlib.xml"
                 uri="antlib:net.sf.antcontrib"
                 onerror="failall">
            <classpath path="${ant-contrib.jar}"/>
        </taskdef>
    </target>

</project>
```

The `onerror` attribute of `<typedef>` or `<taskdef>` has four values: "**ignore**", "**report**" , "**fail**" , and "**failall**". The **ignore** option does nothing if the tasks cannot be defined for any reason. The **report** option prints errors but continues the build, while the final two will halt the build if trouble is detected. The default is "**fail**". It will raise an exception if the task or type does not load<sub>注：task或type不能加载</sub> but do nothing if the `resource` itself isn’t found<sub>注：resource找不到</sub>. The strictest option, **failall**, raises an immediate error if the identified `resource` URI isn’t resolvable, and is the best choice for immediately finding a problem. The other options are relevant only for backwards compatibility or for skipping a premature `<typedef>` in projects in which custom tasks are actually compiled.


http://checkstyle.sourceforge.net




