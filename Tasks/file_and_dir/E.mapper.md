# mapper

<!-- TOC -->

- [1. Identity mapper](#1-identity-mapper)
- [2. Flatten mapper](#2-flatten-mapper)
- [3. Glob mapper](#3-glob-mapper)
- [4. Regexp mapper](#4-regexp-mapper)
- [5. Package mapper](#5-package-mapper)
- [6. Merge mapper](#6-merge-mapper)
- [7. Composite mapper](#7-composite-mapper)
- [8. Chained Mapper](#8-chained-mapper)

<!-- /TOC -->

Ant’s mappers generate **a new set of filenames** from **source files**. Any time you need to move sets of files into a new directory hierarchy, or change parts of the filename itself, such as an extension, look for an appropriate mapper.

The following table shows the built-in mapper types. They are used by `<uptodate>`, `<move>`, `<copy>`, `<apply>`, and several other tasks.

| Type           | Description                                                  |
| -------------- | ------------------------------------------------------------ |
| `identity`     | The target is identical to the source filename.              |
| `flatten`      | Source and target filenames are identical, with the target filename having all leading directory paths stripped. |
| `merge`        | All source files are mapped to a single target file specified in the `to` attribute. |
| `glob`         | A single asterisk (`*`) used in the `from` pattern is substituted into the `to` pattern. Only files matching the `from` pattern are considered. |
| `package`      | A subclass of the `glob` mapper, `package` functions similarly except that it replaces path separators with the dot character (`.`) so that a file with the hierarchical package directory structure can be mapped to a flattened directory structure while retaining the package structure in the filename. |
| `regex`        | Both the `from` and `to` patterns define regular expressions. Only files matching the `from` expression are considered. |
| `unpackage`    | Replaces dots in a Java package with directory separators.   |
| `composite`    | Applies all nested mappers in parallel.                      |
| `chained`      | Applies all nested mappers in sequence.                      |
| `filter`       | Applies a list of `pipe` commands to each filename.          |
| `scriptmapper` | Creates an output filename by running code in a scripting language. |

Mappers are powerful, and it’s worthwhile looking at them in detail. If a project has any need to rename files and directories or move files into a different directory tree, a mapper will probably be able to do it. Let’s explore them in some more detail.

## 1. Identity mapper

The first mapper is the `identity` mapper, which is the default mapper of `<copy>` and `<move>`. It’s used when a task needs a mapper, but you don’t need to do any filename transformations:

```xml
<identitymapper/>
```

Because it’s the default mapper of `<copy>`, the following declarations are equivalent:

```xml
<copy todir="new_web">
    <fileset dir="web" includes="**/*.jsp"/>
    <identitymapper/>
</copy>

<copy todir="new_web">
    <fileset dir="web" includes="**/*.jsp"/>
</copy>
```

It’s fairly rare to see the identity mapper because you get it for free.

The next mapper, the `flatten` mapper, is used when collecting files together in a single directory, such as when collecting JAR files to go into the `WEB-INF/lib` directory of a web application.

## 2. Flatten mapper

The `flatten` mapper strips all directory information from the source filename to map to the target filename. This is one of the most useful mapping operations, because it collects files from different places and places them into a single directory. If we wanted to copy and flatten all JAR files from a library directory hierarchy into a single directory ready for packaging, we would do this:

```xml
<copy todir="dist/lib">
    <fileset dir="lib" includes="**/*.jar"/>
    <flattenmapper />
</copy>
```

If multiple files have the same name in the source fileset, only one of them will be mapped to the destination directory—and you cannot predict which one.

Although it copies everything to a single directory, the flatten mapper doesn’t rename files. To do that, use either the `glob` or `regexp` mapper.

## 3. Glob mapper

The very useful `glob` mapper can do simple file renaming, such as changing a file extension. It has two attributes, `to` and `from`, each of which takes a string with a single asterisk (`*`) somewhere inside. The text matched by the pattern in the `from` attribute is substituted into the `to` pattern:

```xml
<copy todir="new_web">
    <fileset dir="web" includes="**/*.jsp"/>
    <globmapper from="*.jsp" to="*.jsp.bak" />
</copy>
```

This task declaration will copy all JSP pages from the `web` directory to the `new_web` directory with each source `.jsp` file given the `.jsp.bak` extension.

If you have more complex file-renaming problems, it’s time to reach for the big brother of the `glob` mapper, the `regexp` mapper, which can handle arbitrary regular expressions.

## 4. Regexp mapper

The `regexp` mapper takes a regular expression in its `from` attribute. Source files matching this pattern get mapped to the target file. The target filename is built using the `to` pattern, with pattern substitutions from the `from` pattern, including `\0` for the fully matched source filename and `\1` through `\9` for patterns matched with enclosing parentheses in the from pattern.

Here’s a simple example of a way to map all `.java` files to `.java.bak` files. It has the same effect as the `glob` mapper example, shown above:

```xml
<regexpmapper from="^(.*)\.java$" to="\1.java.bak"/>
```

The `<copy>` example for the `glob` mapper can be rewritten this way:

```xml
<copy todir="new_web">
    <fileset dir="web" includes="**/*.jsp"/>
    <regexpmapper from="^(.*)\.jsp$" to="\1.jsp.bak" />
</copy>
```

Quite sophisticated mappings can be done with this mapper, such as removing a middle piece of a directory hierarchy and other wacky tricks. To find the pattern syntax, look up `java.util.regex.Pattern` in the JDK documentation.

## 5. Package mapper

The `package` mapper transforms the `*` pattern in its `from` attribute into a dotted package string in the `to` pattern. It replaces each directory separator with a dot (`.`).

The result is a flattening of the directory hierarchy where Java files need to be matched against data files that have the fully qualified class name embedded in the filename. This mapper was written for use with the data files generated by the `<junit>` task’s XML formatter.

The data files resulting from running a test case with `<junit>` are written to a single directory with the filename `TEST -<fully qualified classname>.xml`. The package mapper lets you map from Java classnames to these files:

```xml
<packagemapper from="*.java" to="${results.dir}/TEST-*.xml" />
```

Another use would be to create a flat directory tree of all the source code:

```xml
<copy todir="out">
    <fileset dir="src" includes="**/*.java"/>
    <packagemapper from="*.java" to="*.java" />
</copy>
```

Running this target would copy a file such as `src/org/d1/core/Constants.java` to `out/core.d1.Constants.java`.

This mapper has an opposite, the `unpackagemapper`, which goes from dotted filenames to directory separators.

All the mappers covered so far focus on renaming individual files in a copy. Mappers can do more than this, as they can provide any mapping from source filenames to destination names. One mapper, the `merge` mapper, maps every source file to the same destination mapper.

## 6. Merge mapper

The `merge` mapper maps all source files to the same destination file, which limits its value in a `<copy>` operation. However, it comes in handy in the `<uptodate>` task. This is a task that compares a fileset of source files to a mapped set of destination files and sets a property if the destination files are as new as the source files. This property indicates that the destination files are up-to-date.

With the merge mapper, `<uptodate>` lets us test if an archive file contains all the latest source files:

```xml
<uptodate property="zip.notRequired">
    <srcfiles dir="src" includes="**/*.java"/>
    <mergemapper to="${dist.dir}/src.zip"/>
</uptodate>
```

The property will not be set if the Zip file is out of date, a fact that can be used to trigger the execution of a conditional target that will create the Zip file only on demand.

Mappers can also go the other way, **generating multiple names** from **a single source**, which lets you map a source file to multiple destinations.

## 7. Composite mapper

The `composite` mapper takes multiple mappers inside it and returns the result of mapping the source file to every mapper. The more source files you have, the more mapped filenames you end up with: it’s the “or” operation of mapping. Here we copy our source files to their original name and into the same directory with a `.java.txt` suffix:

```xml
<copy todir="dist/source">
    <fileset dir="src" includes="**/*.java" />
    <compositemapper>
        <identitymapper />
        <globmapper from="*.java" to="*.java.txt"/>
    </compositemapper>
</copy>
```

> 我在自己的电脑上，这个不起作用，只有第一个mapper执行

There’s one other mapper that takes nested mappers, the chained mapper.

## 8. Chained Mapper

The `chained` mapper lets you chain together a list of other mappers to create the final set of filenames. We could use this mapper to copy the source files into a flat directory using `<flattenmapper>`, then change the extension to `.txt` using the `<globmapper>`:

```xml
<copy todir="dist/source">
    <fileset dir="src" includes="**/*.java" />
    <chainedmapper>
        <flattenmapper/>
        <globmapper from="*.java" to="*.txt" />
    </chainedmapper>
</copy>
```

This is good for composing complex filename transformations.

