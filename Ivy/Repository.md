# Ivy Repository

## Concept

The default settings include 3 types of repositories:

- local: A repository which is private to the user.
- shared: A repository which is shared between all the members of a team
- public: A public repository in which most modules, and especially third party modules, can be found

## Setting up the repositories

- `root`: `${ivy.default.ivy.user.dir}` (default: `~/.ivy2/`)
- Local:  `${ivy.local.default.root}` (default: `${ivy.default.ivy.user.dir}/local`)
- Shared: `${ivy.shared.default.root}` (default: `${ivy.default.ivy.user.dir}/shared`)

First, several repositories use the same root in your filesystem. Referenced as `${ivy.default.ivy.user.dir}`, this is by default the directory `.ivy2` in your user home.

Note that several things can be done by setting Ivy variables. To set them without defining your own `ivysettings.xml` file, you can:

- **set an Ant property** before any call to Ivy in your build file if you use Ivy from Ant
- set an environment variable if you use Ivy from the command line

For example:

```xml
<target name="resolve">
    <property name="ivy.default.ivy.user.dir" value="/path/to/ivy/user/dir"/>
    <ivy:resolve/>
</target>
```

### Local

By default, the local repository lies in `${ivy.default.ivy.user.dir}/local`. This is usually a good place, but you may want to modify it. No problem, you just have to set the `ivy.local.default.root` Ivy variable to the directory you want to use:

For example:

```txt
ivy.local.default.root=/opt/ivy/repository/local
```

If you already have something you would like to use as your local repository, you may also want to modify the layout of this repository. Once again, two variables are available for that:

- `ivy.local.default.ivy.pattern` which gives the pattern to find **Ivy module descriptor files**
- `ivy.local.default.artifact.pattern` which gives the pattern to find **the artifacts**

For example:

```txt
ivy.local.default.root=/opt/ivy/repository/local
ivy.local.default.ivy.pattern=[module]/[revision]/ivy.xml
ivy.local.default.artifact.pattern=[module]/[revision]/[artifact].[ext]
```

### Shared

By default, the shared repository lies in `${ivy.default.ivy.user.dir}/shared`. This is fine if you work alone, but the shared repository is supposed to be, mmm, shared! So changing this directory is often required, and it is usually modified to point to **a network shared directory**. You can use the `ivy.shared.default.root` variable to specify a different directory.

Moreover, you can also configure the layout with variables similar to the ones used for the local repository:

- `ivy.shared.default.ivy.pattern` which gives the pattern to find Ivy module descriptor files
- `ivy.shared.default.artifact.pattern` which gives the pattern to find the artifacts

For example:

```txt
ivy.shared.default.root=/opt/ivy/repository/shared
ivy.shared.default.ivy.pattern=[organisation]/[module]/[revision]/ivy.xml
ivy.shared.default.artifact.pattern=[organisation]/[module]/[revision]/[artifact].[ext]
```

### Public

By default, the public repository is ibiblio<sub>这是个什么呢？</sub> in m2 compatible mode (in other words, the Maven 2 public repository).

This repository has the advantage of providing a lot of modules, with metadata for most of them. The quality of metadata is not always perfect, but it’s a very good start to use a tool like Ivy and benefit from the power of transitive dependency management.

Despite its ease of use, we suggest reading the Best practices to have a good understanding of the pros and cons of using a public unmanaged repository before depending on such a repository for your enterprise build system.

## Going further

By default, Ivy is configured using an `ivysettings.xml` which is packaged in the Ivy jar(`org/apache/ivy/core/settings/ivysettings.xml`). Here is this settings file:

```xml
<ivysettings>
    <settings defaultResolver="default"/>
    <include url="${ivy.default.settings.dir}/ivysettings-public.xml"/>
    <include url="${ivy.default.settings.dir}/ivysettings-shared.xml"/>
    <include url="${ivy.default.settings.dir}/ivysettings-local.xml"/>
    <include url="${ivy.default.settings.dir}/ivysettings-main-chain.xml"/>
    <include url="${ivy.default.settings.dir}/ivysettings-default-chain.xml"/>
</ivysettings>
```

If you want to define **your own public resolver**, you will just have to configure Ivy with the settings like the following:

```xml
<ivysettings>
    <settings defaultResolver="default"/>
    <include url="http://myserver/ivy/myivysettings-public.xml"/>
    <include url="${ivy.default.settings.dir}/ivysettings-shared.xml"/>
    <include url="${ivy.default.settings.dir}/ivysettings-local.xml"/>
    <include url="${ivy.default.settings.dir}/ivysettings-main-chain.xml"/>
    <include url="${ivy.default.settings.dir}/ivysettings-default-chain.xml"/>
</ivysettings>
```

