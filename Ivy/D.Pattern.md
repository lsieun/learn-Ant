# Pattern

## Apache Ivy Settings Files

Till now we have used **default settings** to carry out the tasks. Apache Ivy allows to **override the default settings** through a settings file. Apache Ivy setting file is an XML file, usually named as `ivysettings.xml`. In subsequent chapters, we use `ivysettings.xml` to **override the default settings**.

## Apache Ivy Pattern

Apache Ivy uses a concept called **Pattern** in many tasks and settings. Pattern is used to define a directory structure or the artifact file name. Ivy uses patterns to

- to name artifacts.
- to place artifacts in the proper directory.<sub>往哪儿放</sub>
- to access or search artifacts in repositories.<sub>从哪儿取</sub>

Apache Ivy **Pattern** is composed of **tokens** which are replaced by **actual values** to evaluate a particular module or artifact. Token are surrounded by **square brackets** as `[ext]`.

```txt
Pattern --> token --> actual value
```

Frequently used Tokens

| Token            | Evaluates as            |
| ---------------- | ----------------------- |
| `[organisation]` | organisation name       |
| `[module]`       | module name             |
| `[revision]`     | revision name           |
| `[artifact]`     | artifact name (or id)   |
| `[type]`         | artifact type           |
| `[ext]`          | artifact file extension |
| `[conf]`         | configuration name      |

We may mix **tokens**, **Ivy variables** and **actual directory names** to compose a pattern. For example, we wish to install or add module - `Pigo` developed by `xyz.com` to our local repository. Module `pigo` Version `2.0` has **three artifacts** - **pigoapp class jar**, **pigoapp source jar** and **pigoapp javadoc jar**. If we set file pattern as `[module]-[artifact]-[type]-[revision].[ext]`, then Ivy will rename the artifact files when they are placed into the cache. Tokens, their values and filename for each of the artifact will be as follows.

```txt
pattern = tokens + Ivy variables + actual directory names
```

Token examples

| Artifact                 | Pattern [module]-[artifact]-[type]-[revision].[ext] becomes | File name (cache)           |
| ------------------------ | ----------------------------------------------------------- | --------------------------- |
| pigoapp classes jar file | [pigo]-[pigoapp]-[jar]-[2.0].[jar]                          | pigo-pigoapp-jar-2.0.jar    |
| pigoapp source jar file  | [pigo]-[pigoapp]-[source]-[2.0].[jar]                       | pigo-pigoapp-source-2.0.jar |
| pigoapp javadoc jar file | [pigo]-[pigoapp]-[doc]-[2.0].[jar]                          | pigo-pigoapp-doc-2.0.jar    |

### Apache Ivy Pattern to change the repository layout

**Patterns** are helpful to control the **directory structure** and **artifact naming**. Let’s go through some of the pattern examples which change the repository layout.

```txt
[organisation]/[module]/[type]s/[artifact]-[revision].[ext]
```

This pattern makes Ivy to

- create a directory with `organisation` name.
- then creates a subdirectory with `module` name.
- then creates three subdirectories for **3 types** – `jar`, `source` and `doc`. As there is a `s` after `[type]`, the actual directory names will be `jars`, `sources` and `docs`.
- then place the artifacts under the respective directories. File name consists of `artifact` name, `revision` and `extension`.

Resultant directory tree and files are shown in the next screenshot.

```txt
com.xyz/pigo/jars/pigoapp-1.1.jar
com.xyz/pigo/sources/pigoapp-1.1.jar
com.xyz/pigo/docs/pigoapp-1.1.jar
```

Let’s see what would be the layout with the next pattern

```txt
[organisation]/[module]/[artifact]-[type]-[revision].[ext]
```

This pattern lands all artifacts in the same directory and artifact file name ends up with the types. Layout and file names are shown in the next screenshot.

```txt
com.xyz/pigo/pigoapp-jar-1.1.jar
com.xyz/pigo/pigoapp-source-1.1.jar
com.xyz/pigo/pigoapp-doc-1.1.jar
```

### Apache Ivy Pattern to rename the artifacts in Retrieval Task

In the previous chapter, for `<ivy:retrieve>` we used following pattern

```txt
myfolder/[artifact]-[revision].[ext]
```

This pattern makes Ivy to

- to retrieve required artifacts and **rename the artifacts** with `artifact` name, `revision` and `ext`.
- then copy the artifact to `myfolder`.

```txt
myfolder/pigoapp-1.1.jar
myfolder/pigoapp-1.1.jar
myfolder/pigo/pigoapp-1.1.jar
```

## Summing up

To sum up, Apache Ivy uses Pattern setting to

- to name or rename artifacts.
- to change the repository or directory layouts.
- to place artifacts in the proper directory.
- to access, search or retrieve artifacts from different repositories layouts.
