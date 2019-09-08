# A first Ant build

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

## STEP FOUR : IMPOSING STRUCTURE

> 对于source code和output files进行不同directory管理，是十分有必要的

Source files, output files, and the build file: they’re all in the same directory. If this project gets any bigger, things will get out of hand. Before that happens, we must impose some structure. The structure we’re going to impose is quite common with Ant and is driven by the three changes we want to make to the project.

- We want to automate the cleanup in Ant. If done incorrectly, this could accidentally delete source files. To minimize that risk, you should always separate source and generated files into different directories.
- We want to place the Java source file into a Java package.
- We want to create a JAR file containing the compiled code. This should be placed somewhere that also can be cleaned up by Ant.

To add packaging and clean-build support to the build, we have to isolate the **source**, **intermediate**, and **final files**. Once **source** and **generated files** are separated, it’s safe to clean the latter by deleting the output directory, making clean builds easy. It’s good to get into the habit of doing clean builds. The first step, then, is to sort out the source tree.

## Laying out the source directories

We like to have a standard directory structure for laying out projects. Ant doesn’t mandate this, but it helps if everyone uses a similar layout.

An Ant project should split **source files**, **compiled classes files**, and **distribution packages** into separate directories. This makes them much easier to manage during the build process.

| Directory name  | Function                                                     |
| --------------- | ------------------------------------------------------------ |
| `src`           | Source files                                                 |
| `build`         | All files generated in a build that can be deleted and recreated |
| `build/classes` | Intermediate output (created; cleanable)                     |
| `dist`          | Distributable files (created; cleanable)                     |

We do need to create the top-level `build` directory and the `classes` subdirectory. We do this with the Ant task `<mkdir>`, which creates a directory. In fact, **it creates parent directories**, too, **if needed**:

```xml
<mkdir dir="build/classes">
```

The `dist` directory contains redistributable artifacts of the project. A common stage in a build process is to package files, placing the packaged file into the `dist` directory. There may be different types of packaging— JAR , Zip, tar, and WAR , for example— and so a subdirectory is needed to keep all of these files in a place where they can be identified and deleted for a clean build. To create the distribution directory, we insert another call to `<mkdir>`:

```xml
<mkdir dir="dist">
```

To create the JAR file, we’re going to use an Ant task called, appropriately, `<jar>`.

```xml
<jar destfile="dist/project.jar" basedir="build/classes" />
```

## Incremental builds

Ant goes through all the targets, but none of the tasks say that they are doing any work. Here’s why: all of these tasks in the build file check their dependencies, and do nothing if they do not see a need. The `<mkdir>` task doesn’t create directories that already exist, `<javac>` compiles source files when they’re newer than the corresponding `.class` file, and the `<jar>` task compares the time of all files to be added to the archive with the time of the archive itself. No files have been compiled, and the JAR is untouched. This is called an **incremental build**.

If you add the `-verbose` flag to the command line, you’ll get more detail on what did or, in this case, did not take place.

```bash
ant -v
```
