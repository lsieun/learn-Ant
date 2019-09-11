# Property

**Properties** are the heart of Ant’s extensibility and flexibility. They provide a mechanism to store variables and load them from external resources including the environment. Unlike Java variables, they’re **immutable**.

An Ant **property** represents **any string-specified item**. Ant properties are essential not just to **share information around the build**, but to **control build files from the outside**. For example, changing a build to use a different version of a third-party library, perhaps for testing purposes, can be made as trivial as this:

```bash
ant -Dhost=localhost
```

We could set the property inside the file using

```xml
<property name="host" value="localhost" />
```

In either case, the Ant property `host` is now bound to the value `localhost` . To use this value in a build file, we can use it inside any string

```xml
<echo>host=${host}</echo>
```

If the property is defined, the `${host}` is replaced with its value; if it isn’t, it stays as is.<sub>注：如果有定义，就取值；如果没有定义，就用原来的字符串</sub>

File: `build.xml`

```xml
<?xml version="1.0"?>
<project name="helloworld" default="test">
    <target name="test" description="This is my Test">
        <echo message="current user is ${user.name}"/>
        <echo message="host = ${host}"/>
    </target>
</project>
```

Output:

```txt
$ ant
Buildfile: build.xml

test:
     [echo] current user is liusen
     [echo] host = ${host}

BUILD SUCCESSFUL
Total time: 0 seconds
```

Unlike Java variables, Ant properties are **immutable**: you cannot change them<sub>注：这是一个很重要的特性</sub>. The first task, project, or person to set a property fixes it for that build. This rule is the opposite of most languages, but it’s a rule that lets you control build files from the outside, from tools such as IDEs, or from automated build systems. It’s also the key to letting different users customize a build file to work on their system, without editing the build file itself. Simply by defining the appropriate property on the command line, you can change the behavior of your own or someone else’s build file. Inside the build file, properties let you define a piece of information once and share it across many tasks. This makes maintenance easier and reduces errors.

