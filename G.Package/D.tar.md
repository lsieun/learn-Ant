# tar

## Tar files

Tar files are the classic distribution format of Unix. The archive includes not only **the folder hierarchy**, but also **the file permissions**, including the files that are executable. A version of the `tar` program can be found on every Unix platform, and it’s even
cross-compiled for Windows.

Ant can create tar files, including permissions, using its `<tar>` task. This task takes an **implicit fileset** with attributes such as `includes` and `excludes` to control which files to include. We prefer a more verbose and explicit policy of listing **filesets** as **nested elements**. This is more than simply a style policy for better maintenance; it’s a way of having more control over the build.

At the same time, we want to **minimize maintenance**. Although we could create the tar file by copying fileset declarations from the `<zip>` task, we do not want to do that. That would force us to keep both the tar and Zip processes synchronized, or else we may accidentally leave something out of a distribution. Also, we can’t reference `<zipfileset>` declarations inside the `<tar>` task. So how can we reuse all the Zip file work to create a tar file?

Well, after creating the Zip distribution, we unzipped it, to verify it was all there. What if we were to create a tar file from that directory tree, adding file permissions as we go? That might seem like cheating, but from the extreme programming perspective, it’s exactly the kind of lazy coding developers should be doing.

Title: Creating a tar file from our expanded Zip file

```xml
<target name="create-bin-tar" depends="unzip-bin-zipfile">
    <tar destfile="${target.tar}" longfile="gnu">
        <tarfileset dir="${unzip.bin.dir}" excludes="${executables}"/>
        <tarfileset dir="${unzip.bin.dir}" includes="${executables}" filemode="755"/>
    </tar>
</target>
```

The `<tar>` task extends the usual `<fileset>` element with the `<tarfileset>`: a fileset with `filemode` and `dirmode` attributes for Unix permissions. The file permission is in the base-8 format used in Unix API calls. The default permission is `644` (read/write to the owner, read to everyone else), and the default identity is simply the empty string. A mask of `755` adds an executable flag to this permission list, whereas `777` grants read, write, and execution access to all. The `<tarfileset>` element also supports the `prefix` attribute found in `<zipfileset>`, which lets you place files into the archive in a directory with a different name from their current directory.

## Problems with tar files

The original tar file format and program doesn’t handle very long path names. There’s a **100-character limit**, which is easily exceeded in any Java source tree. **The GNU tar program** supports longer filenames, unlike **the original Unix implementation**. You can tell the `<tar>` task what to do when it encounters this situation with its `longfile` attribute.

Values for the `longfile` attribute. Although optional, setting this attribute shows that you have chosen an explicit policy. Of the options, `fail`, `gnu`, and `warn` make the most sense.

| Longfile value | Meaning                                                      |
| -------------- | ------------------------------------------------------------ |
| `fail`         | Fail the build                                               |
| `gnu`          | Save long pathnames in the gnu format                        |
| `omit`         | Skip files with long pathnames                               |
| `truncate`     | Truncate long pathnames to 100 characters                    |
| `warn`         | Save long pathnames in the `gnu` format, and print a warning message `[default]` |

If you choose to use **the GNU format**, add a warning note on the download page about using GNU `tar` to expand the library.

Tar files are also a weak format for sharing, because they’re uncompressed, and so can be overweight compared to Zip files. This issue is addressed by compressing them.

## Compressing the archive

Redistributable tar files are normally compressed by using either the `gzip` or `bzip2` algorithms as `.tar.gz` and `.tar.bz2` files, respectively. This process is so ubiquitous that the GNU `tar` tool has the options `--gzip` and `--ungzip` to do the `.gz` compression and decompression, along with the `tar` creation or extraction operations.

Ant can compress the .tar file by using the `<gzip>` and `<bzip2>` tasks:

```xml
<target name="compress-tar" depends="create-bin-tar">
    <gzip src="${target.tar}" destfile="${target.tar.gz}"/>
    <bzip2 src="${target.tar}" destfile="${target.tar.bz2}"/>
</target>
```

Apart from the different compression algorithms, the `<gzip>` and `<bzip2>` tasks behave identically. They take a single source file in the `src` attribute and an output file in either the `destfile` or `zipfile` attribute. When executed, these tasks create a suitably compressed file whenever the destination file is absent or older than the source.

We do, of course, have to check that the compressed tar file is usable. Ant has a task called `<untar>` to reverse the `tar` operation, and others called `<gunzip>` and `<bunzip2>` to uncompress the files first. You can use these tasks to verify that the redistributable files are in good condition.

For example, here’s the `.tar.gz` file expanded into a directory tree:

```xml
<property name="untar.dir" location="${build.dir}/untar"/>

<target name="untar-bin.tar.gz" depends="compress-tar">
    <mkdir dir="${untar.dir}"/>
    <gunzip src="${target.tar.gz}" dest="${untar.dir}"/>
    <untar src="${untar.dir}/${project.name-ver}.tar" dest="${untar.dir}"/>
</target>
```

