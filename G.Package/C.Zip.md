# Create Zip Files

<!-- TOC -->

- [1. zip task](#1-zip-task)
- [2. Creating a binary Zip distribution](#2-creating-a-binary-zip-distribution)
- [3. Creating a source distribution](#3-creating-a-source-distribution)

<!-- /TOC -->

## 1. zip task

Ant creates Zip files as easily as it creates JAR files, using `<jar>`’s parent task, `<zip>`<sub>注：讲两者之间的继承关系</sub>. All attributes and elements of `<zip>` can be used in `<jar>`<sub>注：讲两者的相同之处</sub>, but the JAR-specific extras for the manifest and other metadata aren’t supported<sub>注：两者的不同的之处</sub>.

```java
public class Jar extends Zip {
    //
}
```

What the `<zip>` and `<jar>` tasks support is the `<zipfileset>` element. The `<zipfileset>` extends the normal `fileset` with some extra parameters. It lets you add the contents of one Zip file to another, expanding it in the directory tree where you choose<sub>注：从别的zip文件中读取文件到当前的zip文件中</sub>, and it lets you place files imported from the file system into chosen places in the Zip file<sub>注：从File System中读取文件到当前的zip文件中</sub>. This eliminates the need to create a complete directory tree on the local disk before creating the archive.

```java
public class ZipFileSet extends ArchiveFileSet {
}

public abstract class ArchiveFileSet extends FileSet {
}
```

Title: Extra attributes in `<zipfileset>` compared to a `<fileset>`

| Attribute  | Meaning                                                      |
| ---------- | ------------------------------------------------------------ |
| `prefix`   | A directory prefix to use in the Zip file                    |
| `fullpath` | The full path to place the single file in archive            |
| `src`      | The name of a Zip file to include in the archive             |
| `encoding` | The encoding to use for filenames in the Zip file; default is the local encoding |
| `filemode` | Unix file system permission; default is `644`                |
| `dirmode`  | Unix directory permission; default is `755`                  |

## 2. Creating a binary Zip distribution

Creating a binary Zip distribution:

```xml
<target name="create-bin-zipfile" 
        depends="sign-jar,fix-docs" 
        description="create the distributable for Windows">
    <zip destFile="${target.zip}" duplicate="preserve">
        <zipfileset file="${target.jar}" prefix="${project.name-ver}"/>
        <zipfileset dir="${dist.doc.dir}" includes="**/*" prefix="${project.name-ver}/docs"/>
        <zipfileset dir="${javadoc.dir}" includes="**/*" prefix="${project.name-ver}/docs/api"/>
    </zip>
</target>
```

To verify that this task works, we create a target to unzip the file:

```xml
<property name="unzip.dir" location="${build.dir}/unzip"/>
<property name="unzip.bin.dir" location="${unzip.dir}/bin"/>

<target name="unzip-bin-zipfile" depends="create-bin-zipfile">
    <unzip src="${target.zip}" dest="${unzip.bin.dir}"/>
</target>
```

## 3. Creating a source distribution

A source-only distribution contains the source tree and the build file(s); the recipient has to compile everything. Open-source projects may want to consider a single distribution containing the source and the binaries, delivering a quick start from the JAR files, yet offering the opportunity of editing the source to all users.

We’re going to include the JAR file; then the components for our source build file become clear. They are: the `source`, `test`, and `documentation` directory trees; **the build file**; and the binary Zip file itself:

```xml
<target name="create-src-zipfile" depends="sign-jar">
    <zip destfile="${src.zip}" duplicate="preserve">
        <zipfileset file="${target.jar}" prefix="${project.name-ver}"/>
        <zipfileset dir="." includes="src/**,test/**,doc/**,*.xml" prefix="${project.name-ver}"/>
    </zip>
</target>
```

The result is a file that runs out of the box but which contains the entire source and, of course, the build file. We can verify this by unzipping the file:

```xml
<target name="unzip-src-zipfile" depends="create-src-zipfile">
    <unzip src="${src.zip}" dest="${build.dir}/src"/>
</target>
```






