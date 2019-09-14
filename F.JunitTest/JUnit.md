# JUnit

<!-- TOC -->

- [1. JUnit’s architecture](#1-junits-architecture)
- [2. Writing a test case](#2-writing-a-test-case)
  - [2.1. 第一种方法：继承TestCase](#21-%e7%ac%ac%e4%b8%80%e7%a7%8d%e6%96%b9%e6%b3%95%e7%bb%a7%e6%89%bftestcase)
  - [2.2. 第二种方法：使用@Test注解](#22-%e7%ac%ac%e4%ba%8c%e7%a7%8d%e6%96%b9%e6%b3%95%e4%bd%bf%e7%94%a8test%e6%b3%a8%e8%a7%a3)
- [3. The lifecycle of a TestCase](#3-the-lifecycle-of-a-testcase)
- [4. Asserting desired results](#4-asserting-desired-results)

<!-- /TOC -->

## 1. JUnit’s architecture

- `TestCase`: abstract class
- `Assert`

The `TestCase` class represents a test to run.

The `Assert` class provides a set of assertions that methods in a test case can make, assertions that verify that the program is doing what we expect.

## 2. Writing a test case

### 2.1. 第一种方法：继承TestCase

- Create a subclass of `junit.framework.TestCase`
- Provide a constructor, accepting a single String name parameter, which calls `super(name)`.
- Write some public no-argument `void` methods prefixed by the word `test`

```java
import junit.framework.TestCase;

public class SimpleTest extends TestCase {

    public SimpleTest(String name) {
        super(name);
    }

    public void testCreation() {
        Event event=new Event();
    }
}
```

### 2.2. 第二种方法：使用@Test注解

```java
import org.junit.Test;

public class MyTest {
    @Test
    public void testABC() {
        System.out.println("ABC");
    }
}
```

## 3. The lifecycle of a TestCase

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

## 4. Asserting desired results

A test method within a JUnit test case **succeeds** if it completes without throwing an exception. A test **fails** if it throws a `junit.framework.AssertionFailedError` or derivative class. A test terminates with an **error** if the method throws any other kind of exception. Anything other than **success** means that something went wrong, but failures and errors are reported differently.

> 注意：这段当中要区分开succeed, fail, error这三个概念

`AssertionFailedError` exceptions are thrown whenever a JUnit framework assertion fails. Most of the assertion methods compare **an actual value** with **an expected one**, or examine other simple states of `Object` references. There are variants of the `assert` methods for the primitive datatypes and the `Object` class itself.
