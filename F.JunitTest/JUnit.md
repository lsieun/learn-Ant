# JUnit

## The lifecycle of a TestCase

JUnit runs every test method in the same way. It enumerates **all test methods** in **a test class** and **creates an instance of that class** for **each test method**, passing the method name to the constructor. Then, for every test method, it runs the following routine:

```java
public void runBare() throws Throwable {
    this.setUp();

    try {
        this.runTest();
    } catch (Throwable var10) {
        exception = var10;
    } finally {
        this.tearDown();
    }

}
```

That is, it calls the method `public void setUp()`, runs the **test method** through some introspection magic, and then calls `public void tearDown()`. The results are forwarded to any classes that are listening for results.

You can add any number of **test methods** to a `TestCase`, all beginning with the prefix `test`. Methods without this prefix are ignored. You can use this trick to turn off tests or to write helper methods to simplify testing.

**Test methods** can throw any exception they want: there’s no need to catch exceptions and turn them into assertions or failures. What you do have to make sure of is that the method signature matches what’s expected: **no parameters** and **a `void` return type**. If you accidentally add a return type or an argument, the method is no longer a test.

To create or configure objects before running each test method, you should override the empty `TestCase.setUp` method and configure member variables or other parts of the running program. You can use the `TestCase.tearDown` method to close any open connections or in some way clean up the machine, along with `try {} finally {}` clauses in the methods themselves.

**An instance of the class** is created for **every test method** before any of the tests are run, you can’t do setup work in the constructor, or cleanup in any finalizer.

To summarize: the `setUp` and `tearDown` methods are called before and after every test method and should be the only place where you prepare for a test and clean up afterwards.

## Adding JUnit to Ant

Ant has a task to run the JUnit tests called, not surprisingly, `<junit>`. This is an **optional task**.

Ant has three categories of tasks.

- **Core tasks** are built into `ant.jar`(`ANT_HOME/lib/ant.jar`) and are always available.
- **Optional tasks** are tasks that are supplied by the Ant team, but are either viewed as **less important** or **they depend on external libraries or programs**. They come in the `ant-optional.jar` or a dependency-specific JAR , such as `ant-junit-jar`.
- **Third-party tasks** are Ant tasks written by others.

The `<junit>` task is an **optional task**, which depends upon JUnit’s JAR file to run the tests.


## The JUnit Task: `<junit>`
