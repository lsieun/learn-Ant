# Data Type

In programming language terms, **Ant’s tasks** represent the functionality offered by the runtime libraries. **The tasks** are useful only with **data**, the information that they need to know what to do. Java is an object-oriented language where data and functions are mixed into classes. Ant, although written in Java, differentiates between **the tasks** that do the work and **the data** they work with—data represented as **datatypes**. Ant also has the approximate equivalent of variables in its **properties**.

```txt
程序 = 算法 + 数据结构
Java --> function + data --> class
Ant  --> task + data type + properties

- task: 实现具体的功能，形象的说就是某种“机甲工厂”
- data type: 存储数据的各种容器，形象的说它是一个“燃料箱”，供应“机甲工厂”运行，但它只是一个“空壳”，而没有“灵魂”
- properties: 这里就是“变量”，形象的说就是“灵魂”，它能充满“燃料箱”，引导整个“机甲工厂”运作起来
```

To pass **data** to **tasks**, you need to be able to construct and refer to **datatypes** and **properties** in a build file. As with **tasks**, **datatypes** are just pieces of XML , pieces that list files or other resources that a task can use.

An Ant **datatype** is equivalent to a Java class—behind the scenes they’re actually implemented as such. Datatypes store complex pieces of information used in the build—for example, a list of files to compile or a set of directories to delete. These are the kinds of things Ant has to manage, so build files need a way to describe them. Ant datatypes can do this. The datatypes act as parameters to tasks. You can declare them inside a task or define them outside, give them a name, and then pass that name to a task. This lets you share a datatype across more than one task.

A typical Ant build has to handle **files** and **paths**, especially the notorious **classpath**. Ant **datatypes** can handle **files** and **paths** natively. The `fileset` and `path` datatypes crop up(突然出现) throughout Ant build files.

The `fileset` datatype can enumerate which files to compile, package, copy, delete, or test. Defining a `fileset` of all Java files, for example, is straightforward:

```xml
<fileset id="source.fileset" dir="src" includes="**/*.java" />
```

By providing an `id` attribute, we’re defining a **reference**. This reference name can be used later wherever a `fileset` is expected.

For example, copying our source code to another directory using the same `source.fileset` is

```xml
<copy todir="backup">
    <fileset refid="source.fileset"/>
</copy>
```

This will work only if the fileset was defined previously in the build, such as in a predecessor target. Otherwise, Ant will fail with an error about an undefined reference.

Several important facts about Ant:

- Ant uses **datatypes** to provide rich, reusable parameters to tasks.
- `<javac>` is a task that utilizes most of Ant’s datatypes.
- **Paths** represent **an ordered list of files and directories**. Many tasks can accept a **classpath**, which is an **Ant path**. Paths can be specified in a cross-platform manner—the MS-DOS conventions of semicolon (`;`) and backslash (`\`) or the Unix conventions of colon (`:`) and forward slash (`/`); Ant sorts it all out at runtime.
- **Filesets** represent **a collection of files** rooted from **a specified directory**. Tasks that operate on sets of files often use **Ant’s fileset datatype**.
- **Patternsets** represent **a collection of file-matching patterns**. Patternsets can be defined and applied to any number of filesets.



