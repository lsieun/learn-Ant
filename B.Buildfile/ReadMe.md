# Buildfile

Ant is told what to build by an XML file, a **build file**. This file describes all the actions to build an application, such as **creating directories**, **compiling the source**, **making a JAR file** and **running the program**.

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

The build file is in XML, with the root `<project>` element representing a **Ant project**. This project contains **targets**, each of which represents **a stage of the project**. A target can depend on other targets, which is stated by listing the dependencies in the `depends` attributes of the `<target>`. Ant uses this information to determine which targets to execute, and in what order.

The actual work of the build is performed by **Ant tasks**. These tasks implement their own dependency checking<sub>注：这里的dependency checking可能是指：如果目录存在，就不创建目录了；如果有最新编译好的class文件，就不重新进行编译了</sub>, so they only do work if it is needed.

Some of the basic Ant tasks are `<echo>` to print a message, `<delete>` to delete files and directories, `<mkdir>` to create directories, `<javac>` to compile Java source, and `<jar>` to create an archive file. The first three of these tasks look like XML versions of shell commands, but the latter two demonstrate the power of Ant. They contain **dependency logic**, so that `<javac>` will compile only those source files for which the destination binary is missing or out of date, and `<jar>` will create a JAR file only if its input files are newer than the output.

Running Ant is called **building**; a build either succeeds or fails. Builds fail **when there’s an error in the build file**, or **when a task fails by throwing an exception**. In either case, Ant lists the line of the build file where the error occurred. Ant can build from the command line, or from within Java IDEs. The command line has many options to control the build and what output gets displayed. Rerunning a build with the `-verbose` option provides more detail as to what is happening. Alternatively, the `-quiet` option runs a build nearly silently. The most important argument to the command line is **the name of the targets to run**—Ant executes each of these targets and all its dependencies.

```bash
## Print information about the current project
ant -projecthelp
## 或者
ant -p
```
