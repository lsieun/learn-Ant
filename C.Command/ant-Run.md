# ant Run

## Running the build file

You can simply list **one or more targets** on the command line, so all of the following are valid:

```xml
ant
ant init
ant clean
ant compile
ant archive
```

Calling Ant with no target is the same as calling the target named in the `default` attribute of the `<project>`.

```xml
<project name="structured" default="archive" >

</project>
```

## Incremental builds

All of these tasks in the build file check their dependencies, and do nothing if they do not see a need. The `<mkdir>` task doesn’t create directories that already exist, `<javac>` compiles source files when they’re newer than the corresponding `.class` file, and the `<jar>` task compares the time of all files to be added to the archive with the time of the archive itself. No files have been compiled, and the JAR is untouched. This is called an **incremental build**.

If you add the `-verbose` flag to the command line, you’ll get more detail on what did or did not take place. The verbose run provides a lot of information, much of which may seem distracting. When a build is working well, you don’t need it, but it’s invaluable while developing that file.

## Running multiple targets on the command line

Developers can run **multiple targets** in a single build, by listing the targets one after the other on the command line. But what happens when you type `ant compile archive` at the command line? Many people would expect Ant to pick an order that executes each target and its dependencies once only: `[init, compile, archive]`. Unix Make would certainly do that, but Ant does not. Instead, it executes each target and dependents in turn, so the actual sequence is `init, compile`, then `init, compile, archive`.

```bash
$ ant compile archive
Buildfile: example/build.xml

init:
    [mkdir] Created dir: example/build/classes
    [mkdir] Created dir: example/dist

compile:
    [javac] Compiling 1 source file to example/build/classes

init:

compile:

archive:
      [jar] Building jar: example/dist/project.jar

BUILD SUCCESSFUL
Total time: 0 seconds
```

Being able to run multiple targets on the command line lets developers type a sequence of operations such as `ant clean execute` to clean the output directory, rebuild everything, and run the program.

