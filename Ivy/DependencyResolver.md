# Dependency Resolver

A **dependency resolver** is **a pluggable class** in Ivy which is used to:

- (1) find dependencies' Ivy files
- (2) download dependencies' artifacts

The notion of artifact "downloading" is large: an artifact can be on a web site, or on the local file system of your machine. The download is thus the act of bring a file from a **repository** to the **Ivy cache**.

Moreover, the fact that it is **the responsibility of the resolver** to find **Ivy files** and download **artifacts** helps to implement various resolving strategies.

As you see, **a dependency resolver** can be thought of as a class responsible for describing a repository.

## Reference

- [resolvers](http://ant.apache.org/ivy/history/2.5.0-rc1/settings/resolvers.html)







