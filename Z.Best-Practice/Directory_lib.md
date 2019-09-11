# Managing Library Dependencies

Most Java projects depend on other Java libraries and JAR files. Some may only be needed at build time, others during testing or when executing the final program. The Ant build file needs to set up the classpath for the tasks that perform all these operations; otherwise the build will fail. How should this be done?

The simplest (and most common) way to manage libraries is to have a directory (by convention, the directory `lib`), into which you put all dependent libraries. To add a new JAR to the project, drop it into the directory.

To include these files on our compilation classpath, we declare a path that includes every JAR file in the directory:

```xml
<path id="compile.classpath">
    <fileset dir="lib">
        <include name="*.jar"/>
    </fileset>
</path>
```

This path can then be fed to the compiler by referring to it in the `<javac>` task:

```xml
<javac destdir="${build.classes.dir}" srcdir="src">
    <classpath refid="compile.classpath"/>
</javac>
```

With this technique, it’s easy to add new JAR files, and you can see what libraries a project uses. If you check the JARs in the `lib` directory into the source code repository, all developers will get the files they need—and any updates.


