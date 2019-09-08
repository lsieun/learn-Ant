# project

The `<project>` element is always the root element in Ant build files, in this case containing two attributes, `name` and `default`. The `<target>` element is a child of `<project>`.

```xml
<?xml version="1.0"?>
<project name="firstbuild" default="compile" >

<target name="compile">
    <javac srcdir="." />
    <echo>compilation complete!</echo>
</target>

</project>
```

All Ant build files must contain a single `<project>` element as the root element. It tells Ant the `name` of the project and, optionally, the `default` target.

Underneath the `<project>` element is a `<target>` with the name `compile`. A target represents a single stage in the build process. It can be called from the command line or it can be used internally. A build file can have many targets, each of which must have **a unique name**.

## attribute


## sub element













