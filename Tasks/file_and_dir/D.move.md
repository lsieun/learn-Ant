# move Task

Ant’s `<move>` task can move files around. It first tries to rename the file or directory; if this fails, then it copies the file and deletes the originals. An unwanted side effect is that if `<move>` has to copy, Unix file permissions will get lost.

The syntax of this task is nearly identical to `<copy>`, as it’s a direct subclass of the `<copy>` task:

```xml
<move file="readme.txt" todir="${dist.doc.dir}"/>
```

As with `<copy>`, this task uses timestamps to avoid overwriting newer files unless `overwrite="true"`.

The `<move>` task is surprisingly rare in build files, as copying and deleting files are much more common activities. Its main role is renaming generated or copied files, but since `<copy>` can rename files during the copy process and even choose a different destination directory, there’s little need for the task.
