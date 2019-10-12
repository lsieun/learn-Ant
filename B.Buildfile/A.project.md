# project

The `<project>` element is always the root element in Ant build files, in this case containing two attributes, `name` and `default`.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project name="core-diary" default="compile" basedir=".">

    <description>
        This build file compiles, tests, packages and
        distributes the core library of the diary application.
    </description>

    <target name="compile">
        <echo>compilation complete!</echo>
    </target>

</project>
```

All Ant build files must contain a single `<project>` element as the root element. It tells Ant the `name` of the project and, optionally, the `default` target.

Underneath the `<project>` element is a `<target>` with the name `compile`. A target represents a single stage in the build process. It can be called from the command line or it can be used internally. A build file can have many targets, each of which must have **a unique name**.

## attribute


## nested element

### description

Some text in the `<description>` element is useful, as it’s printed when Ant is passed the `-p` or the `-projecthelp` parameter.










