# Installation

There are basically two ways to install Ivy: either manually or automatically.

## Manually

Download the version you want here, unpack the downloaded zip file wherever you want, and copy **the Ivy jar file** into **your Ant lib directory** (`ANT_HOME/lib`).

If you use Ant 1.6.0 or superior, you can then simply go to the [src/example/hello-ivy](https://gitbox.apache.org/repos/asf?p=ant-ivy.git;a=tree;f=src/example;hb=HEAD) dir and run Ant: if the build is successful, you have successfully installed Ivy!

## Automatically

## Apache Ant and Apache Ivy Installation

Following commands installs Apache Ant at `/opt/ant`:

```bash
tar -C /opt/ant/ -xzvf apache-ant-1.8.2-bin.tar.gz
tar -xzvf apache-ivy-2.2.0-bin-with-deps.tar.gz
cp apache-ivy-2.2.0/ivy-2.2.0.jar /opt/ant/apache-ant-1.8.2/lib
export ANT_HOME=/opt/ant/apache-ant-1.8.2
export PATH=$PATH:$ANT_HOME/bin
```

Environment variables `ANT_HOME` and `PATH` are essential to run Ant. These exports may be moved to `.bash_profile` in Linux so that they are always set when you login.

## Test the Apache Ivy installation

To test the Ant and Apache Ivy installation, add following `build.xml` file to work dir.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project name="test ivy" default="test" xmlns:ivy="antlib:org.apache.ivy.ant">
    <target name="test" description="==>  Test ivy installation">
        <ivy:settings/>
    </target>
</project>
```

and run Ant.

```bash
ant
```

Successful build indicates that ant and ivy installation is fine.

### Build Error

Typical error installation error is

```xml
build.xml:5: Problem: failed to create task or type antlib:org.apache.ivy.ant:settings
```

check the following to resolve this

- (1) environment variable `ANT_HOME` is not set properly
- (2) `ivy.jar` is missing from `ANT_HOME/lib`


