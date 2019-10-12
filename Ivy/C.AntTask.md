# Apache Ivy Ant Tasks

Apache Ivy comes with its own set of Ant Tasks which can be called from Ant build file.

| Task      | Description                                                  |
| --------- | ------------------------------------------------------------ |
| Resolve   | Resolves the dependencies described in `ivy.xml` and places the resolved dependencies in ivy cache. |
| Cachepath | Constructs an ant path consisting of artifacts in ivy cache which can be referred in other ant tasks through Ant path mechanism. |
| Retrieve  | Copies the resolved dependencies from cache to a specified directory |
| Install   | Installs a module to a specified repository.                 |
| Publish   | Publish a module to a repository.                            |

## Resolve task

