# ant

The basic use of the `<ant>` task is simple: you use it to call any target in any other build file, passing in properties and references, if you desire.

To use `<ant>`, we have to know **the name of a target** to call, which means every project must have a standard set of targets.

| Target Name | Function                                            |
| ----------- | --------------------------------------------------- |
| `default`   | The default entry point                             |
| `all`       | Builds and tests everything; creates a distribution |
| `clean`     | Deletes all generated files and directories         |
| `dist`      | Produces the distributables                         |
| `docs`      | Generates all documentation                         |
| `test`      | Runs the unit tests                                 |
| `noop`      | Does nothing but print the name of the project      |





