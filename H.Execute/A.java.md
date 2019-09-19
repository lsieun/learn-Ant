# java

<!-- TOC -->

- [1. Application](#1-application)
  - [1.1. Setting the classpath](#11-setting-the-classpath)
  - [1.2. Arguments](#12-arguments)
- [2. JRE](#2-jre)
  - [2.1. Defining system properties](#21-defining-system-properties)
- [3. JVM](#3-jvm)
  - [3.1. Running the program in a new JVM](#31-running-the-program-in-a-new-jvm)
  - [3.2. JVM tuning](#32-jvm-tuning)
- [4. Capturing the return code](#4-capturing-the-return-code)
- [5. Executing JAR files](#5-executing-jar-files)

<!-- /TOC -->

The central task for running Java applications is called, not surprisingly, `<java>`. It has many options and is well worth studying.

## 1. Application

### 1.1. Setting the classpath

The task takes **the name of the entry point class** via its `classname` attribute.

**Adding classpaths** is easy: you just fill out the `<java>` task’s `<classpath>` element, or set its `classpath` attribute to a path as a string. If you’re going to use the same classpath in more than one place, declare the path and then refer to it using the `classpathref` attribute. This is simple and convenient to do.

One common practice is to extend **the compile-time classpath** with **a second classpath** that includes the newly built classes, either in archive form or as a directory tree of `.class` files. We do this by declaring **two classpaths**, **one for compilation** and **the other for execution**:

```xml
<path id="compile.classpath">
    <fileset dir="${lib.dir}">
        <include name="*.jar"/>
    </fileset>
</path>

<path id="run.classpath">
    <path refid="compile.classpath"/>
    <pathelement location="${target.jar}"/>
</path>

<target name="run" depends="sign-jar">
    <echo>running with classpath ${toString:run.classpath}</echo>
    <java classname="d1.DiaryMain" classpathref="run.classpath"/>
</target>
```

The **compile classpath** will include any libraries we depend upon to build; then **the run classpath** extends this with the JAR just built. This approach simplifies maintenance; any library used at compile time automatically propagates to the runtime classpath.

We also printed out the classpath by using the `${toString:run.classpath}` operation. When Ant expands this, it resolves the reference to `run.classpath`, calls the datatype’s `toString()` method, and passes on the result. For Ant paths, that value is the path in a **platform-specific path form**.

**Setting up the classpath** and **the program entry point** are the two activities that must be done for every `<java>` run.

### 1.2. Arguments

The argument list is the main way of passing data to a running application. You can name arguments by **a single value**, **a line of text**, **a filename**, or **a path**. These arguments are passed down in one or more `<arg>` elements, an element that supports the **four attributes**. Ant resolves the arguments and passes them on in the order they’re declared.

Title: The attributes of the `<arg>` element of `<java>`

| `<arg>` attribute | Meaning                                                      |
| ----------------- | ------------------------------------------------------------ |
| `value`           | String value                                                 |
| `file`            | File or directory to resolve to an absolute location before invocation |
| `path`            | A string containing files or directories separated by colons or semicolons |
| `pathref`         | A reference to a predefined path                             |
| `line`            | Complete line to pass to the program                         |

```xml
<path id="run.classpath">
    <path refid="compile.classpath"/>
    <pathelement location="${target.jar}"/>
</path>

<target name="run" depends="sign-jar">
    <echo>running with classpath ${toString:run.classpath}</echo>
    <property environment="env"/>
    <java classname="d1.DiaryMain" classpathref="run.classpath">
        <arg value="2019-09-17 22:00:00" />
        <arg file="." />
        <arg path="${user.home}:${env.ANT_HOME}" />
        <arg pathref="run.classpath"/>
    </java>
</target>
```

String arguments are the simplest. Any string can be passed to the invoked program. The only tricky spot is handling those symbols that XML does not like. **Angle brackets need to be escaped**, substituting `&lt;` for `<` and `&gt;` for `>`.

There is one other way to pass arguments down, using the `line` attribute. This takes a single line, which is split into separate arguments wherever there’s a space between values:

```xml
<java classname="d1.DiaryMain" classpathref="run.classpath">
    <arg line ="2019-09-17 . ${user.home};/" />
</java>
```

**Avoid using the `<arg line="">` option**. The only reason for using it is to support an unlimited number of arguments—perhaps from a property file or prompted input. Anyone who does this had better hope that spaces aren’t expected in individual arguments.

## 2. JRE

### 2.1. Defining system properties

Java system properties are set on the Java command line as `-Dproperty=value` arguments. The `<java>` task lets you use these properties via the `<sysproperty>` element. This is useful in configuring the JVM itself, such as controlling how long network addresses are cached:

```xml
<sysproperty key="networkaddress.cache.ttl" value="300"/>
```

There are two alternate options instead of the value parameter: `file` and `path`. Just as with `<arg>` elements, the `file` attribute lets you name a file. Ant will pass down an absolute filename in the platform-specific format. The `path` attribute is similar, except that you can list multiple files, and it will convert the path separator to whatever is appropriate for the operating system:

```xml
<sysproperty key="configuration.file" file="./config.properties"/>
<sysproperty key="searchpath" path="build/classes:lib/javaee.jar"/>
```

## 3. JVM

### 3.1. Running the program in a new JVM

The `<java>` task runs inside Ant’s JVM unless the `fork` attribute is set to `true`. Non-forked code can be faster and shares more of the JVM ’s state.

```xml
<target name="java-example-fork" depends="sign-jar">
    <property name="classpath-as-property" refid="run.classpath" />
    <java classname="d1.core.DiaryMain"
          classpathref="run.classpath"
          fork="true">
        <arg value="2005-06-31-08:30" />
        <arg file="." />
        <arg path="${user.home};/" />
    </java>
</target>
```

We prefer to **always fork our Java programs**, preferring isolation over possible performance gains. This reduces the time spent debugging obscure problems related to classloaders and JVM isolation. It also lets us tune JVM options.

### 3.2. JVM tuning

Once we fork `<java>`, we can change the JVM under which it runs and the options in the JVM . This gives us absolute control over what’s going on.

You can actually **choose a Java runtime** that’s different from the one hosting Ant by setting the `jvm` attribute to the command that starts the JVM. This lets you run a program under a different JVM .

In addition to **choosing the JVM** , you can **configure it**. The most commonly used option is **the amount of memory to use**, which is so common that it has its own attribute, the maxmemory attribute. The memory option, as per the java command, takes a string that lists the number of bytes, kilobytes (k), or megabytes (m) to use. Usually, the megabyte option is the one to supply, with `maxmemory="64m"` setting a limit of 64MB on the process.

Other JVM options are specific to individual JVM implementations. A call to java `-X` will list the ones on your local machine. Because these options can vary from system to system, they should be set via properties so that different developers can override them as needed. Here, for example, we set the default arguments for memory and the server JVM with incremental garbage collection, using properties to provide an override point for different users:

```xml
<target name="java-example-jvmargs" depends="build">
    <property name="jvm" value="java" />
    <property name="jvm.gc.args" value="-Xincgc"/>
    <property name="jvm.memory" value="64m"/>
    <property name="jvm.type" value="-server"/>
    <java
         classname="d1.core.DiaryMain"
         classpathref="run.classpath"
         fork="true"
         jvm="${jvm}"
         dir="${build.dir}"
         maxmemory="${jvm.memory}">
        <jvmarg value="${jvm.type}" />
        <jvmarg line="${jvm.gc.args}"/>
        <arg value="2005-06-31-08:30" />
        <arg file="."/>
    </java>
</target>
```

You supply generic JVM arguments using `<jvmarg>` elements nested inside the `<java>` task. The exact syntax of these arguments is the same as for the `<arg>` elements. We use the `line` attribute, despite the negative things we said about it earlier, as it permits a single property to contain a list of arguments. Developers can override the `jvm.gc.args` property in their `build.properties` file to something like

```txt
-XX:+UseConcMarkSweepGC -XX:NewSize=48m -XX:SurvivorRatio=16
```

This would transform their garbage collection policy, without affecting anyone else.

## 4. Capturing the return code

Sometimes, you want to know if the program returned **a status code** or what the value was without halting the build. Knowing this lets you do conditional actions on the return code or run programs that pass information back to Ant. To get the return code, set the `resultproperty` attribute to the name of a property and leave `failonerror="false"`:

```xml
<target name="java-resultproperty" depends="build">
    <java classname="d1.core.DiaryMain"
          classpathref="run.classpath"
          fork="true"
          resultproperty="result" >
        <arg value="2007-02-31-08:30" />
        <arg file="Event on February 31" />
    </java>
    <echo>result=${result}</echo>
</target>
```

This will print out the result. We could use a `<condition>` to test the return value and act on it, or pass the property to another task.

## 5. Executing JAR files

So far, we’ve been executing Java programs by declaring the entry point in the `classname` attribute of the `<java>` task. There’s another way: running a JAR file with an entry point declared in its manifest. This is equivalent to running `java -jar` on the command line. Ant can run JAR files similarly, but **only in a forked JVM**.

To tell it to run a JAR file, set the `jar` attribute to the location of the file:

```xml
<target name="java-jar" depends="build">
    <java
          jar="${target.jar}"
          fork="true"
          failonerror="true">
        <arg value="2007-06-21-05:00" />
        <arg file="Summer Solstice" />
    </java>
</target>
```

For this to work, the manifest must be set up correctly.






