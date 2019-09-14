# JUnit Task

<!-- TOC -->

- [1. Adding JUnit to Ant](#1-adding-junit-to-ant)
- [2. Directory Structure](#2-directory-structure)
- [3. Execute One Test Case](#3-execute-one-test-case)
  - [3.1. haltonfailure](#31-haltonfailure)
  - [3.2. printsummary](#32-printsummary)
  - [3.3. formatters](#33-formatters)
- [4. Running multiple tests with `<batchtest>`](#4-running-multiple-tests-with-batchtest)
- [5. Generate HTML Test Reports](#5-generate-html-test-reports)
  - [5.1. Generate XML](#51-generate-xml)
  - [5.2. XML to HTML](#52-xml-to-html)
  - [5.3. Halting the builds after generating reports](#53-halting-the-builds-after-generating-reports)
- [6. ADVANCED `<junit>` TECHNIQUES](#6-advanced-junit-techniques)
  - [6.1. Running a single test case](#61-running-a-single-test-case)
  - [6.2. Running JUnit in its own JVM](#62-running-junit-in-its-own-jvm)
  - [6.3. Passing information to test cases](#63-passing-information-to-test-cases)
  - [6.4. Enabling Java Assertions](#64-enabling-java-assertions)
  - [6.5. Customizing the `<junitreport>` reports](#65-customizing-the-junitreport-reports)
  - [6.6. Creating your own test result formatter](#66-creating-your-own-test-result-formatter)
- [7. BEST PRACTICES](#7-best-practices)
- [Summary](#summary)

<!-- /TOC -->

## 1. Adding JUnit to Ant

Ant has a task to run the JUnit tests called, not surprisingly, `<junit>`. This is an **optional task**.

Ant has three categories of tasks.

- **Core tasks** are built into `ant.jar`(`ANT_HOME/lib/ant.jar`) and are always available.
- **Optional tasks** are tasks that are supplied by the Ant team, but are either viewed as **less important** or **they depend on external libraries or programs**. They come in the `ant-optional.jar` or a dependency-specific JAR , such as `ant-junit-jar`.
- **Third-party tasks** are Ant tasks written by others.

The `<junit>` task is an **optional task**, which depends upon JUnit’s JAR file to run the tests.

All we need to do then is **download `junit.jar`**, stick it in the correct place and have Ant’s `<junit>` task pick it up. This is something that can be done by hand, or it can be done by asking Ant to do the work itself.

## 2. Directory Structure

```xml
<property name="src.dir" location="src"/>
<property name="build.dir" location="build"/>
<property name="dist.dir" location="dist"/>
<property name="lib.dir" location="lib"/>

<property name="build.classes.dir" location="${build.dir}/classes"/>

<property name="test.src.dir" location="test"/>
<property name="test.dir" location="${build.dir}/test"/>
<property name="test.classes.dir" location="${test.dir}/classes"/>
<property name="test.data.dir" location="${test.dir}/data"/>
<property name="test.reports.dir" location="${test.dir}/reports"/>

<path id="compile.classpath">
    <fileset dir="${lib.dir}">
        <include name="*.jar"/>
    </fileset>
</path>

<path id="test.compile.classpath">
    <path refid="compile.classpath"/>
    <pathelement location="${build.classes.dir}"/>
</path>

<path id="test.classpath">
    <path refid="test.compile.classpath"/>
    <pathelement location="${test.classes.dir}"/>
</path>

<target name="init">
    <mkdir dir="${build.classes.dir}"/>
    <mkdir dir="${dist.dir}"/>
</target>

<target name="test-init">
    <mkdir dir="${test.classes.dir}"/>
    <delete dir="${test.data.dir}"/>
    <delete dir="${test.reports.dir}"/>
    <mkdir dir="${test.data.dir}"/>
    <mkdir dir="${test.reports.dir}"/>
</target>

<target name="compile" depends="init">
    <javac srcdir="${src.dir}" destdir="${build.classes.dir}">
        <include name="**/*.java"/>
    </javac>
</target>

<target name="test-compile" depends="compile,test-init">
    <javac srcdir="${test.src.dir}"
            destdir="${test.classes.dir}"
            debug="true"
            debuglevel="lines,vars,source"
            includeAntRuntime="true">
        <classpath refid="test.compile.classpath"/>
    </javac>
</target>
```

The `includeAntRuntime="true"` flag is a sign that we’re pulling in Ant’s own classpath in order to get one file, `junit.jar`. The alternative is to add `junit.jar` to the projects `lib` directory.

> 我遇到的问题：即使我加了`includeAntRuntime="true"`，但是编译的时候，还是不去加载junit.jar，不知道是为什么，还没有解决。我只好复制一份junit.jar到lib目录下。

## 3. Execute One Test Case

The `<junit>` task runs one or more JUnit tests, then collects and displays the results. It can also halt the build when a test fails.

```xml
<property name="test.suite" value="d1.core.LessSimpleTest"/>

<target name="test-one-class" depends="test-compile">
    <junit printsummary="false" haltonfailure="true">
        <classpath refid="test.classpath"/>
        <formatter type="brief" usefile="false"/>
        <test name="${test.suite}"/>
    </junit>
</target>
```

**By default**, `<junit>` doesn’t halt the build when tests fail. There’s a reason for this: you may want to format the results before halting. For now, we can set the `haltonfailure` attribute to `true` to stop the build immediately.

### 3.1. haltonfailure

理解`haltonfailure`：

- 假设：我们要经历四个过程：第一个过程，编译src目录下的源代码；第二个过程，编译test目录下的源代码；第三个过程，对编译后的test文件进行junit测试；第四个过程，将src编译后的class文件打包成jar文件
- `<junit>` task是对应着第三个过程，而`haltonfailure`就是`<junit>`的属性
- 如果`haltonfailure`为`false`，那么即使单元测试出现错误，也会继续执行第四个过程
- 如果`haltonfailure`为`true`，那么当单元测试出现错误，就停下来了，不会再执行第四个过程了

### 3.2. printsummary

理解`printsummary`，示例代码如下：

```java
public void testAssert() {
    System.out.println("Hello World");

    Date now = new Date();
    assertEquals("Two Dates not equal", now, now);

    boolean flag = false;
    assertTrue("flag is not true", flag);

    System.out.println("Hello JUnit");
}
```

- 当`printsummary`为`false`（默认值）的时候，会打印下面的信息

```txt
[junit] Test d1.core.LessSimpleTest FAILED
```

- 当`printsummary`为`true`的时候，会“多”打印一些信息，例如：

```txt
[junit] Running d1.core.LessSimpleTest       （这里是多打印的信息）
[junit] Tests run: 2, Failures: 1, Errors: 0, Skipped: 0, Time elapsed: 0.012 sec       （这里是多打印的信息）
[junit] Test d1.core.LessSimpleTest FAILED
```

- 在单元测试的代码中，有一句`System.out.println("Hello World");`，当`printsummary`为`true`或`false`的时候，都不会打印出来；当`printsummary`为`withOutAndErr`时，才会打印出来

```txt
    [junit] Running d1.core.LessSimpleTest
    [junit] Tests run: 1, Failures: 2, Errors: 0, Skipped: 0, Time elapsed: 0.01 sec
    [junit] Output:
    [junit] Hello World       （注意：这里并没有打印“Hello JUnit”，是因为assertTrue出错了）
    [junit]
    [junit] Test d1.core.LessSimpleTest FAILED

```

在这里，`printsummary`算是一个“精简版”的输出结果，只是告诉我们“单元测试出错了”，但是并没有告诉我们“哪里出错了”，下面的formatter会帮助我们指出“问题在哪里”。

### 3.3. formatters

The `<junit>` task outputs test results through **formatters**. One or more `<formatter>` elements can be nested either directly under `<junit>` or under its `<test>` and `<batchtest>` elements.

Ant includes the three formatters

| formatter | Description                                                  |
| --------- | ------------------------------------------------------------ |
| `brief`   | Summarizes test failures in plain text                       |
| `plain`   | Provides text details of test failures and statistics of each test run |
| `xml`     | Creates XML results for post-processing                      |


By default, `<formatter>` output is directed to files, but it can be directed to Ant’s console instead.

```xml
<junit printsummary="false" haltonfailure="true">
    <classpath refid="test.classpath"/>
    <formatter type="brief" usefile="false"/>
    <test name="${test.suite}"/>
</junit>
```

Formatters normally write their output to files in the directory specified by the `<test>` or `<batchtest>` elements, but `usefile="false"` tells them to write to the Ant console. We turn off the `printsummary` option because it duplicates and interferes with the console output.

**Detailed test results** are the best way of determining **where problems lie**. The other thing that can aid diagnosing the problem is **the application’s output**, which we can also pick up from a test run.

## 4. Running multiple tests with `<batchtest>`

So far, we’ve only run one test case using the `<test>` tag. You can specify any number of `<test>` elements inside a `<junit>` declaration, but that’s inefficient. Developers should not have to edit the build file when adding new test cases.

You can nest **filesets** within `<batchtest>` to include all your test cases.

```xml
<target name="test-multi-classes" depends="test-compile">
    <junit printsummary="false" haltonfailure="false">
        <classpath refid="test.classpath"/>
        <formatter type="brief" usefile="false"/>
        <batchtest todir="${test.data.dir}">
            <fileset dir="${test.classes.dir}" includes="**/*Test.class"/>
        </batchtest>
    </junit>
</target>
```

## 5. Generate HTML Test Reports

### 5.1. Generate XML

Use the `XML` formatter for JUnit. This formatter creates an XML file for every test class. We can add it alongside the `brief` formatter:

```xml
<target name="test-multi-classes" depends="test-compile">
    <junit printsummary="false" haltonfailure="false">
        <classpath refid="test.classpath"/>
        <formatter type="brief" usefile="false"/>
        <formatter type="xml"/>
        <batchtest todir="${test.data.dir}">
            <fileset dir="${test.classes.dir}" includes="**/*Test.class"/>
        </batchtest>
    </junit>
</target>
```

The effect of adding the XML formatter is **the creation of an XML file for each `<test>` element**. For us, the filename is `${test.data.dir}/TEST-d1.core.test.AllTests.xml`.

XML files are not what we want; we want human-readable HTML reports. This is a bit of post-processing that `<junitreport>` does for us. It applies some XSL transformations to the XML files generated by the XML `<formatter>`, creating HTML files summarizing the test run. You can browse tests, see which ones failed, and view their output. You can also serve the HTML files up on a web site.

### 5.2. XML to HTML

Adding the reporting to our routine is simply a matter of setting `haltonfailure="false"` in `<junit>` so the build continues after the failure, then declaring the `<junitreport>` task after the `<junit>` run:

```xml
<target name="test-multi-classes" depends="test-compile">
    <junit printsummary="false" haltonfailure="false">
        <classpath refid="test.classpath"/>
        <formatter type="brief" usefile="false"/>
        <formatter type="xml"/>
        <batchtest todir="${test.data.dir}">
            <fileset dir="${test.classes.dir}" includes="**/*Test.class"/>
        </batchtest>
    </junit>
    <junitreport todir="${test.data.dir}">
        <fileset dir="${test.data.dir}">
            <include name="TEST-*.xml"/>
        </fileset>
        <report format="frames" todir="${test.reports.dir}"/>
    </junitreport>
</target>
```

The `<fileset>` is necessary and should normally include all files called `TEST-*.xml`. The `<report>` element instructs the transformation to use either `frames` or `noframes` Javadoc-like formatting, with the results written to the `todir` directory.

### 5.3. Halting the builds after generating reports

To halt the build after creating the HTML pages, we make the `<junit>` task set an Ant property when a test fails, using the `failureProperty` and `errorProperty` attributes. A test **failure** means an assertion was thrown, while an **error** means that some other exception was raised in the test case. Some teams like to differentiate between the two, but we don’t. We just want to halt the built if a test failed for any reason, which we can ask for by naming the same property on both attributes. We also need to set `haltOnFailure="false"`, or, given that `false` is the default value, omit the attribute entirely.

Using the properties set by `<junit>`, we can generate the reports before we fail the build.

```xml
<target name="test-reporting" depends="test-compile">
    <junit printsummary="false"
            errorProperty="test.failed"
            failureProperty="test.failed" fork="true">
        <classpath>
            <path refid="test.classpath"/>
        </classpath>
        <formatter type="brief" usefile="false"/>
        <formatter type="xml"/>
        <batchtest todir="${test.data.dir}" >
            <fileset dir="${test.classes.dir}"
                        includes="**/*Test.class"/>
        </batchtest>
    </junit>
    <junitreport todir="${test.data.dir}">
        <fileset dir="${test.data.dir}">
            <include name="TEST-*.xml"/>
        </fileset>
        <report format="frames"
                todir="${test.reports.dir}"/>
    </junitreport>
    <!-- Conditional fail task triggered when the property is set-->
    <fail if="test.failed">
        Tests failed. Check ${test.reports.dir}
    </fail>
</target>
```

Running this target will run the tests, create the report, and halt the build if any test raised an error or failed. It even prints the name of the directory into which the reports went, for pasting into a web browser.

The HTML reports are the nicest way to view test results. Be aware that the XSL transformation can take some time when there are a lot of tests to process. The plain text output is much faster. Sometimes we split the testing and report creation in two in order to let us run the tests without creating the HTML reports.

Projects can use what we’ve covered—**batch execution of test cases** and **HTML output**—for most of their needs.

## 6. ADVANCED `<junit>` TECHNIQUES

### 6.1. Running a single test case

### 6.2. Running JUnit in its own JVM

### 6.3. Passing information to test cases

### 6.4. Enabling Java Assertions

### 6.5. Customizing the `<junitreport>` reports

### 6.6. Creating your own test result formatter

## 7. BEST PRACTICES

## Summary

总结下来，只有两点：

- 如何使用batchtest
- 如何生成HTML报告
