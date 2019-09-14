# Directory Structure

An Ant project should split **source files**, **compiled classes files**, and **distribution packages** into separate directories. This makes them much easier to manage during the build process.

| Directory name       | Function                                                     |
| -------------------- | ------------------------------------------------------------ |
| `src`                | Source files                                                 |
| `test`               | Test source files                                            |
| `build`              | All files generated in a build that can be deleted and recreated |
| `build/classes`      | Intermediate output (created; cleanable)                     |
| `build/test/classes` | Test compiled files                                          |
| `dist`               | Distributable files (created; cleanable)                     |




