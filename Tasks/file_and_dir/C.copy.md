# copy Task

<!-- TOC -->

- [1. copy a single file](#1-copy-a-single-file)
- [2. copy multi files](#2-copy-multi-files)
- [3. timestamp-aware](#3-timestamp-aware)
- [4. change names](#4-change-names)
- [5. limitation](#5-limitation)

<!-- /TOC -->

The task to copy files is, not surprisingly, `<copy>`.

## 1. copy a single file

At its simplest, you can copy files from one place to another. You can specify the destination directory; the task creates it and any parent directories if needed:

```xml
<copy file="readme.html" todir="${dist.doc.dir}"/>
```

You can also give it the complete destination filename, which renames the file during the copy:

```xml
<copy file="readme.html" tofile="${dist.doc.dir}/README.HTML"/>
```

## 2. copy multi files

To do a bulk copy, declare a `fileset` inside the `copy` task; all files will end up in the destination directory named with the `todir` attribute:

```xml
<copy todir="${dist.doc.dir}">
    <fileset dir="doc" >
        <include name="**/*.*"/>
    </fileset>
</copy>
```

## 3. timestamp-aware

By default, `<copy>` is timestamp-aware; it copies only the files that are newer than those of the destination. At build time this is what you want, but if you’re using the task to install something over a newer version, set `overwrite="true"`. This will always overwrite the destination file.

Copied files’ timestamps are set to the current time. To keep the date of the original file, set `preservelastmodified="true"`. Doing so can stop other tasks from thinking that files have changed. Normally, it isn’t needed.

## 4. change names

If you want to change the names of files when copying or moving them, or change the directory layout as you do so, you can specify a `<mapper>` as a nested element of the task.

## 5. limitation

One limitation of Ant is that `<copy>` doesn’t preserve Unix file permissions, because Java doesn’t let it. The `<chmod>` task can be used to set permissions after a copy—a task that is a no-op on Windows—so it can be inserted where it’s needed. Similarly, Ant cannot read permissions when creating a tar archive file, a problem we’ll solve in a different way.

Related to the `<copy>` task is the `<move>` task, which enables you to move or rename files.

> 至此结束： Better late than never.
