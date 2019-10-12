# Ant command-line options

<!-- TOC -->

- [1. Ant run target](#1-ant-run-target)
- [2. Ant Option: software](#2-ant-option-software)
- [3. Ant Option: project](#3-ant-option-project)
  - [3.1. build file](#31-build-file)
  - [3.2. property](#32-property)
  - [3.3. keep-going](#33-keep-going)
  - [3.4. project info](#34-project-info)
- [4. Ant Option: runtime](#4-ant-option-runtime)
  - [4.1. runtime info](#41-runtime-info)
  - [4.2. output format: emacs](#42-output-format-emacs)
  - [4.3. output level: debug, verbose, quiet](#43-output-level-debug-verbose-quiet)

<!-- /TOC -->

## 1. Ant run target

Calling Ant with no target is the same as calling the target named in the `default` attribute of the `<project>`.

Ant can take a number of options, which it lists if you ask for them with `ant -help`.

## 2. Ant Option: software

Print the version information and exit.

```bash
ant -version
```

## 3. Ant Option: project

### 3.1. build file

Probably the most important Ant option is `-buildfile`. This option lets you control which build file Ant uses, allowing you to divide the targets of a project into multiple files and select the appropriate build file depending on your actions. A shortcut to `-buildfile` is `-f`.

Use the named buildfile, use `-f` as a shortcut:

```bash
ant -buildfile <filename>
ant -f <filename>

ant -buildfile build.xml compile
```

### 3.2. property

Set a property to a value:

```bash
ant -Dproperty=value
```

Load properties from file(`-D` definitions take priority):

```bash
ant -propertyfile file
```

### 3.3. keep-going

The `-keep-going` option tells Ant to try to recover from a failure.

When one target on the command line fails, still run other targets:

```bash
ant -keep-going
ant -k
```

If you supply more than one target on the command line, Ant normally stops the moment any of these targets—or any they depend upon—fail. The `-keep-going` option instructs Ant to continue running any target on the command line that doesn’t depend upon the target that fails. This lets you run a reporting target even if the main build didn’t complete.

### 3.4. project info

The option `-projecthelp` lists **the main targets** in a project and is invaluable whenever you need to know **what targets a build file provides**. Ant lists **only those targets** containing the optional `description` attribute, as these are the targets intended for public consumption.

Print information about the current project:

```bash
ant -projecthelp
ant -p
```

## 4. Ant Option: runtime

### 4.1. runtime info

Print debugging information:

```bash
ant -debug
ant -d
```

Print information that might be helpful to diagnose or report problems.

```bash
ant -diagnostics
```

Produce logging information without adornments.

```bash
ant -emacs
```

Run a quiet build: only print errors

```bash
ant -quiet
ant -q
```

Print verbose output for better debugging.

```bash
ant -verbose
ant -v
```

```xml
<target name="execute" depends="compile" description="Runs the program">
    <echo level="warning" message="running" />
    <java classname="com.example.ant.Main" classpath="build/classes">
        <arg value="a"/>
        <arg value="b"/>
        <arg file="."/>
    </java>
</target>
```

### 4.2. output format: emacs

Getting rid of the `[java]` prefix is easy: we run the build file with the `-emacs` option. This omits the **task-name prefix** from all lines printed. The option is called `-emacs` because the output is now in the `emacs` format for invoked tools, which enables that and other editors to locate the lines on which errors occurred.

不带有-emacs

```bash
$ ant -f build-final.xml
Buildfile: example/build-final.xml

init:

compile:

execute:
     [echo] running
     [java] a
     [java] b
     [java] /home/liusen/example

BUILD SUCCESSFUL
Total time: 0 seconds
```

带有-emacs

```bash
$ ant -emacs -f build-final.xml 
Buildfile: example/build-final.xml

init:

compile:

execute:
running
a
b
/home/liusen/example

BUILD SUCCESSFUL
Total time: 0 seconds
```

### 4.3. output level: debug, verbose, quiet

**Three** of the Ant options control **how much information is output when Ant runs**. Two of these (`-verbose` and `-debug`) progressively increase the amount. The `-verbose` option is useful when you’re curious about how Ant works or why a build isn’t behaving. The `-debug` option includes **all the normal and verbose output** and much more low-level information, primarily only of interest to Ant developers. To see nothing but errors or a final build failed/success message, use `-quiet`:

```bash
$ ant -quiet -f build-final.xml execute
     [echo] running

BUILD SUCCESSFUL
Total time: 0 seconds
```

In **quiet** runs, not even `<echo>` statements appear. One of the attributes of `<echo>` is the `level` attribute, which takes five values: `error`, `warning`, `info`, `verbose`, and `debug` control the amount of information that appears. The default value `info` ensures that messages appear in normal builds and in `-verbose` and `-debug` runs. By inserting an `<echo>` statement into our `execute` target with the `level` set to `warning`, we ensure that the message appears even when the build is running as `-quiet`:

```xml
<echo level="warning" message="running"/>
```

Ant produces a verbose log when invoked with the `-verbose` parameter. This is a very useful feature when figuring out what a build file does. If ever you are unsure why a build is not behaving as expected, add the `-v` or `-verbose` option to get lots more information.

```bash
ant -verbose
ant -v
```
