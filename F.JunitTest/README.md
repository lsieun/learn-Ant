# Testing with JUnit

You can write code, but unless you’re going to write tests or formal proofs of correctness, you have no way of knowing if it works.

**Ant** and **JUnit** make this possible. JUnit provides the test framework to write your tests in Java, and Ant runs the tests. The two go hand in hand. JUnit makes it easy to write tests. Ant makes it easy to run those tests, capture the results, and halt the build if a test fails. It will even create HTML reports.

## What is Testing, And Why do it?

Testing is running a program or library with valid and invalid inputs to see that it behaves as expected in all situations.

Many people run their application with valid inputs, but that’s just demonstrating that it can be made to work in controlled circumstances. Testing aims to break the application with bad data and to show that it’s broken.

**Automated testing** is the idea that tests should be executed automatically, and the results should be evaluated by machines. Modern software development processes all embrace testing as early as possible in the development lifecycle.

Before exploring JUnit, we need to define some terms.

- **Unit tests** test a piece of a program, such as a class, a module, or a single method. They can identify problems in a small part of the application, and often you can run them without deploying the application.
- **System tests** verify that a system as a whole works. A server-side application would be deployed first; the tests would be run against that **deployed system**, and may simulate client behavior. Another term for this is **functional testing**.
- **Acceptance tests** verify that the entire system/application meets the customers’ acceptance criteria. Performance, memory consumption, and other criteria may be included above the simple “does it work” assessment. These are also sometimes called **functional tests**, just to cause extra confusion.
- **Regression testing** means testing a program to see that a change has not broken anything that used to work.

JUnit is a **unit-test** framework; you write tests in Java to verify that Java components work as expected. It can be used for **regression testing**, by rerunning a large test suite after every change. It can also be used for some system and acceptance testing, with the help of extra libraries and tools.
