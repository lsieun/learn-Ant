# javac Task

<!-- TOC -->

- [1. attribute](#1-attribute)
  - [1.1. srcdir, destdir and classpath](#11-srcdir-destdir-and-classpath)
  - [1.2. debug](#12-debug)
- [2. nested element](#2-nested-element)
  - [2.1. src element](#21-src-element)
  - [2.2. classpath element](#22-classpath-element)
  - [2.3. compilerarg element](#23-compilerarg-element)
- [3. implicit fileset](#3-implicit-fileset)
- [4. dependency-checking](#4-dependency-checking)

<!-- /TOC -->

## 1. attribute

### 1.1. srcdir, destdir and classpath

| Attribute   | Description                        |
| ----------- | ---------------------------------- |
| `srcdir`    | Location of the java files.        |
| `destdir`   | Location to store the class files. |
| `classpath` | The classpath to use.              |

```xml
<javac srcdir="${src}" destdir="${build}" classpath="xyz.jar" debug="on" source="1.4"/>
```

### 1.2. debug

| Attribute    | Description                                                  |
| ------------ | ------------------------------------------------------------ |
| `debug`      | Indicates whether source should be compiled with debug information. If set to `off`, `-g:none` will be passed on the command line for compilers that support it (for other compilers, no command line argument will be used). If set to `true`, the value of the `debuglevel` attribute determines the command line argument. |
| `debuglevel` | Keyword list to be appended to the `-g` command-line switch. Legal values are none or a comma-separated list of the following keywords: `lines`, `vars`, and `source`. |

## 2. nested element

### 2.1. src element

When using the task, we could declare two `<src>` tags to compile two separate directory trees of source code into a single output directory:

```xml
<javac destdir="build/classes">
    <src path="src"/>
    <src path="test"/>
</javac>
```

### 2.2. classpath element

```xml
<path id="compile.classpath">
    <fileset dir="lib">
        <include name="*.jar"/>
    </fileset>
</path>

<target name="compile" >
    <javac
        destdir="${build.classes.dir}"
        debug="${build.debug}"
        srcdir="${src}">
    <classpath refid="compile.classpath"/>
    </javac>
</target>
```

### 2.3. compilerarg element

```xml
<target name="compile" depends="init" description="Compiles the source code">
    <javac srcdir="${src}" destdir="${build.class}">
        <compilerarg value="-XDignore.symbol.file"/>
        <compilerarg value="-g:lines,vars,source"/>
    </javac>
</target>
```

## 3. implicit fileset

The `<javac>` task is one of the many **implicit fileset** tasks. Rather than requiring you to add a `<fileset>` of source files as a nested element, the task itself supports many of the **attributes** and **elements** of a `fileset`:

```xml
<javac srcdir="src" destdir="build/classes">
    <include name="org/antbook/**/*.java"/>
    <exclude name="org/antbook/broken/*.java"/>
</javac>
```

This task has `<javac>` acting as a `fileset`, including some files and excluding some others.

Note: you can’t reliably use excludes patterns to tell `<javac>` which files not to compile. If a Java file you include needs another file, Sun’s javac compiler will search the source tree for it, even if it’s been excluded from the fileset. This is a feature of the compiler, and not Ant.

`<javac>` is also a task with an **implicit fileset**: it has the **attributes** `includes`, `excludes`, `includesfile`, and `excludesfile` as well as nested `<include>`, `<exclude>`, `<includesfile>`, and `<excludesfile>` elements. Normally, a `<fileset>` has a mandatory root `dir` attribute, but in the case of `<javac>` this is specified with the `srcdir` attribute. Confusing? Yes. However, it was done this way in order to remove ambiguity for build file writers. Would a `dir` attribute on `<javac>` have represented a source directory or a destination directory?

## 4. dependency-checking

The dependency-checking code in `<javac>` relies on the source files being laid out this way. When the Java compiler compiles the files, it always places the **output files in a directory tree** that matches the **package declaration**. The next time the `<javac>` task runs, its dependency-checking code looks at **the tree of generated class files** and compares it to the source files. It doesn’t look inside the source files to find their package declarations; it relies on **the source tree** being laid out to match **the destination tree**.

> 疑问：这段我不能很好的理解，

Note: For Java source file dependency checking to work, you must lay out **source in a directory tree** that matches **the package declarations in the source**.

Be aware that dependency checking of `<javac>` is simply limited to comparing the dates on the source and destination files. A regular clean build is a good practice — do so once a day or after refactoring classes and packages.
