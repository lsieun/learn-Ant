# Ant command-line options

<!-- TOC -->

- [1. ant software info](#1-ant-software-info)
- [2. project](#2-project)
  - [2.1. build file](#21-build-file)
  - [2.2. property](#22-property)
  - [2.3. keep-going](#23-keep-going)
  - [2.4. project info](#24-project-info)
- [3. runtime](#3-runtime)
  - [3.1. runtime info](#31-runtime-info)

<!-- /TOC -->

Calling Ant with no target is the same as calling the target named in the `default` attribute of the `<project>`.

```bash
ant -version
```

Ant produces a verbose log when invoked with the `-verbose` parameter. This is a very useful feature when figuring out what a build file does.

```bash
ant -verbose
## 或者
ant -v
```

If ever you are unsure why a build is not behaving as expected, add the `-v` or `-verbose` option to get lots more information.


Ant can take a number of options, which it lists if you ask for them with `ant -help`.

## 1. ant software info

```bash
## Print the version information and exit.
ant -version
```

## 2. project

### 2.1. build file

Probably the most important Ant option is `-buildfile`. This option lets you control which build file Ant uses, allowing you to divide the targets of a project into multiple files and select the appropriate build file depending on your actions. A shortcut to `-buildfile` is `-f`.

```bash
## Use the named buildfile, use -f as a shortcut.
ant -buildfile <filename>
ant -f <filename>

## 例
ant -buildfile build.xml compile
```

### 2.2. property

```bash
## Set a property to a value.
ant -Dproperty=value

## Load properties from file; -D definitions take priority
ant -propertyfile file
```

### 2.3. keep-going

```bash
## When one target on the command line fails, still run other targets.
ant -keep-going
ant -k
```

The `-keep-going` option tells Ant to try to recover from a failure. If you supply more than one target on the command line, Ant normally stops the moment any of these targets—or any they depend upon—fail. The `-keep-going` option instructs Ant to continue running any target on the command line that doesn’t depend upon the target that fails. This lets you run a reporting target even if the main build didn’t complete.

### 2.4. project info

The option `-projecthelp` lists **the main targets** in a project and is invaluable whenever you need to know **what targets a build file provides**. Ant lists **only those targets** containing the optional `description` attribute, as these are the targets intended for public consumption.

```bash
## Print information about the current project
ant -projecthelp
## 或者
ant -p
```

## 3. runtime

### 3.1. runtime info

```bash
## Print debugging information
ant -debug
ant -d

## Print information that might be helpful to diagnose or report problems.
ant -diagnostics

## Produce logging information without adornments.
ant -emacs

## Run a quiet build: only print errors
ant -quiet
ant -q

## Print verbose output for better debugging.
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

Getting rid of the `[java]` prefix is easy: we run the build file with the `-emacs` option. This omits the **task-name prefix** from all lines printed. The option is called `-emacs` because the output is now in the `emacs` format for invoked tools, which enables that and other editors to locate the lines on which errors occurred.

```bash
## 不带有-emacs
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

## 带有-emacs
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

Three of the Ant options control **how much information is output when Ant runs**. Two of these (`-verbose` and `-debug`) progressively increase the amount. The `-verbose` option is useful when you’re curious about how Ant works or why a build isn’t behaving. The `-debug` option includes **all the normal and verbose output** and much more low-level information, primarily only of interest to Ant developers. To see nothing but errors or a final build failed/success message, use `-quiet`:

```bash
$ ant -quiet -f build-final.xml execute
     [echo] running

BUILD SUCCESSFUL
Total time: 0 seconds
```

In quiet runs, not even `<echo>` statements appear. One of the attributes of `<echo>` is the `level` attribute, which takes five values: `error`, `warning`, `info`, `verbose`, and `debug` control the amount of information that appears. The default value `info` ensures that messages appear in normal builds and in `-verbose` and `-debug` runs. By inserting an `<echo>` statement into our `execute` target with the `level` set to `warning`, we ensure that the message appears even when the build is running as `-quiet`:

```xml
<echo level="warning" message="running"/>
```












