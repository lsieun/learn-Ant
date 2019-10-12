# Tasks

Ant has three categories of tasks.

- **Core tasks** are built into `ant.jar`(`ANT_HOME/lib/ant.jar`) and are always available, such as `<javac>`, `<jar>`, and `<copy>`.
- **Optional tasks** are tasks that are supplied by the Ant team, but are either viewed as **less important** or **they depend on external libraries or programs**. They come in the `ant-optional.jar` or a dependency-specific JAR , such as `ant-junit-jar`. `<junit>`, `<junitreport>`, `<ssh>`, and `<ftp>` are all optional.
- **Third-party tasks** are Ant tasks written by others.

