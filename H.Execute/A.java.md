# java

The central task for running Java applications is called, not surprisingly, `<java>`. It has many options and is well worth studying.

## Setting the classpath

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

## Arguments

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

## Defining system properties










