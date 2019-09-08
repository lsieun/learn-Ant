# javac Task

## Basic Usage

```xml
<javac srcdir="${src}" destdir="${build}" classpath="xyz.jar" debug="on" source="1.4"/>
```

## dependency-checking

The dependency-checking code in `<javac>` relies on the source files being laid out this way. When the Java compiler compiles the files, it always places the **output files in a directory tree** that matches the **package declaration**. The next time the `<javac>` task runs, its dependency-checking code looks at **the tree of generated class files** and compares it to the source files. It doesn’t look inside the source files to find their package declarations; it relies on **the source tree** being laid out to match **the destination tree**.

> 疑问：这段我不能很好的理解，

Note: For Java source file dependency checking to work, you must lay out **source in a directory tree** that matches **the package declarations in the source**.

Be aware that dependency checking of `<javac>` is simply limited to comparing the dates on the source and destination files. A regular clean build is a good practice — do so once a day or after refactoring classes and packages.
