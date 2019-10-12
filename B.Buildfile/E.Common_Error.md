# Ant Common Error

<!-- TOC -->

- [1. Right](#1-right)
- [2. Wrong One: xml tag](#2-wrong-one-xml-tag)
- [3. Wrong Two: task](#3-wrong-two-task)
- [4. Wrong Three: attribute](#4-wrong-three-attribute)
- [5. Wrong Four: source code](#5-wrong-four-source-code)

<!-- /TOC -->

The key point to note is that **failure of a task** will usually result in the build itself failing. This is essential for a successful build process: there’s no point packaging or delivering a project if it didn’t compile. In Ant, the build fails if a task fails.

## 1. Right

File: `Main.java`

```java
public class Main {
    public static void main(String args[]) {
        for(int i=0;i<args.length;i++) {
            System.out.println(args[i]);
        }
    }
}
```

File: `build.xml`

```xml
<?xml version="1.0"?>
<project name="firstbuild" default="compile" >

<target name="compile">
    <javac srcdir="." />
    <echo>compilation complete!</echo>
</target>

</project>
```

## 2. Wrong One: xml tag

```xml
<?xml version="1.0"?>
<project name="firstbuild" default="compile" >

<target name="compile">
    <javac srcdir="." />
    <echo>compilation complete!</echo>

<!-- 注意：这里缺少了target对应的结束标签 -->

</project>
```

The error here would come from the XML parser:

```txt
Buildfile: build.xml

BUILD FAILED
build.xml:9: The element type "target" must be terminated by the matching end-tag "</target>".
```

## 3. Wrong Two: task

Imagine if somehow the XML was mistyped so that the `<javac>` task was misspelled, as in

```xml
<!-- 正确为：javac -->
<javaac srcdir="." />
```

With this task in the target, the output would look something like

```txt
Buildfile: build.xml

compile:

BUILD FAILED
build.xml:5: Problem: failed to create task or type javaac
Cause: The name is undefined.
Action: Check the spelling.
Action: Check that any custom tasks/types have been declared.
Action: Check that any <presetdef>/<macrodef> declarations have taken place.
```

## 4. Wrong Three: attribute

One error we still encounter regularly comes from having an attribute that isn’t valid for that task. Spelling the `srcdir` attribute as `sourcedir` is an example of this:

```xml
<!-- 正确为：srcdir -->
<javac sourcedir="." />
```

If the build file contains that line, you would see this error message:

```txt
Buildfile: build.xml

compile:

BUILD FAILED
build.xml:5: javac doesn't support the "sourcedir" attribute
```

## 5. Wrong Four: source code

The error you’re likely to see most often in Ant is the build halting after the compiler failed to compile your code. If, for example, someone forgot the semicolon after the `println` call, the compiler error message would appear, followed by the build failure:

```java
public class Main {
    public static void main(String args[]) {
        for(int i=0;i<args.length;i++) {
            // 注意：缺少了";"
            System.out.println(args[i])
        }
    }
}
```

```txt
Buildfile: build.xml

compile:
    [javac] build.xml:5: warning: 'includeantruntime' was not set, defaulting to build.sysclasspath=last; set to false for repeatable builds
    [javac] Compiling 1 source file
    [javac] Main.java:4: error: ';' expected
    [javac]             System.out.println(args[i])
    [javac]                                        ^
    [javac] 1 error

BUILD FAILED
build.xml:5: Compile failed; see the compiler error output for details.
```
