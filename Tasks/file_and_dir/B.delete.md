# delete Task

<!-- TOC -->

- [1. delete an individual file](#1-delete-an-individual-file)
- [2. delete an entire directory](#2-delete-an-entire-directory)
- [3. delete selected files](#3-delete-selected-files)
- [4. delete failure](#4-delete-failure)
- [5. delete verbose](#5-delete-verbose)

<!-- /TOC -->

## 1. delete an individual file

The `<delete>` task can delete an individual file with a single `file` attribute:

```xml
<delete file="${dist.doc.dir}/readme.txt"/>
```

## 2. delete an entire directory

It can just as easily delete an entire directory with

```xml
<delete dir="${dist.dir}"/>
```

**This task is dangerous**, as it can silently delete everything in the specified directory and those below it. If someone accidentally sets the `dist.dir` property to the current directory, then the entire project will be destroyed. Be careful of what you delete.

## 3. delete selected files

For more selective operations, `<delete>` takes a fileset as a nested element, so you can specify a pattern, such as all backup files in the source directories:

```xml
<delete>
    <fileset dir="${src.dir}" includes="*~" defaultexcludes="false"/>
</delete>
```

This fileset has the attribute `defaultexcludes="false"`. Usually, filesets ignore the editor- and SCM -generated backup files that often get created, but when trying to delete such files you need to turn off this filtering. Setting the `defaultexcludes` attribute to `false` has this effect.

## 4. delete failure

Three attributes on `<delete>` handle failures: `quiet`, `failonerror`, and `deleteonexit`. The task cannot delete files if another program has a lock on the file, so deletion failures are not unheard of, especially on Windows. When the `failonerror` flag is `true`, as it is **by default**, Ant halts the build with an error. If the flag is `false`, then Ant reports the error before it continues to delete the remaining files. You can see that something went wrong, but the build continues:

```xml
<delete defaultexcludes="false" failonerror="false" >
    <fileset dir="${dist.dir}" includes="**/"/>
</delete>
```

The `quiet` option is nearly the exact opposite of `failonerror` . When `quiet="true"`, errors aren’t reported and the build continues. Setting this flag implies you don’t care whether the deletion worked or not. It’s the equivalent of `rm -q` in Unix. The final flag, `deleteonexit`, tells Ant to tell the JVM to try to delete the file again when the JVM is shut down. You can’t rely on this cleanup being called, but you could maybe do some tricks here, such as marking a file that you know is in use for delayed deletion. Things may not work as expected on different platforms or when Ant is run from an IDE.

## 5. delete verbose

There’s also a `verbose` flag to tell the task to list all the files as it goes. This can be useful for seeing what’s happening:

```xml
<delete failonerror="false" verbose="true">
    <fileset dir="${dist.dir}" includes="**/"/>
</delete>
```

Deleting files is usually a housekeeping operation. Its role in packaging is to **clean up destination directories** where files can go before adding the directory contents to JAR, Zip, or tar archives. Create a clean directory with a `<delete>` command and a `<mkdir>` command, then copy all the files to be packaged into this directory tree.

> 至此结束： Two wrongs don't make a right.
