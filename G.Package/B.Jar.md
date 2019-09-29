# Jar

<!-- TOC -->

- [1. Create Jar File](#1-create-jar-file)
- [2. Testing the JAR file](#2-testing-the-jar-file)
- [3. Creating JAR manifests](#3-creating-jar-manifests)
- [4. Adding extra metadata to the JAR](#4-adding-extra-metadata-to-the-jar)
- [5. JAR file best practices](#5-jar-file-best-practices)
- [6. Signing JAR files](#6-signing-jar-files)
  - [6.1. Generating a signing key](#61-generating-a-signing-key)
  - [6.2. Signing the file](#62-signing-the-file)
- [7. Testing With Jar Files](#7-testing-with-jar-files)

<!-- /TOC -->

我的总结：

- （1） 创建Jar文件
  - 使用`<jar>` Task
  - 添加meta data，例如`manifest.mf`
- （2） 对Jar文件进行签名
- （3） 对Jar文件进行JUnit测试

The **JAR** file is the central redistributable of Java. It has derivatives, the most common of which are the **WAR** and **EAR** files for web applications and Enterprise applications, respectively. Underneath, they’re all **Zip archives**. Zip files/archives can store lots of files inside by using a choice of compression algorithms, including “uncompressed.”

The **JAR**, **WAR**, and **EAR** archives are all variations of the basic Zip file, with a text manifest to describe the file and potentially add signing information. The **WAR** and **EAR** files add standardized subdirectories to the JAR file to store libraries, classes, and XML configuration files. This can make the **WAR** and **EAR** files self-contained redistributables.

Building and manipulating JAR files is a common activity; anyone who uses Ant to build a project will soon become familiar with the `<zip>` and `<jar>` tasks.

A **JAR** file stores **classes** in a simple tree resembling a package hierarchy, with any **metadata** added to the `META-INF` directory<sub>注：Jar文件中包含了两类文件，class文件和meta data文件。</sub>. This directory contains a manifest file `MANIFEST.MF`, which describes the JAR file to the classloader.

## 1. Create Jar File

The `<jar>` task creates a manifest file unless you explicitly provide one.

```xml
<target name="jar" depends="compile">
    <jar destfile="${dist.dir}/d1-core.jar"
         duplicate="preserve"
         compress="false">
        <fileset dir="${build.classes.dir}"/>
    </jar>
</target>
```

The `compress` attribute controls whether the archive is compressed. By default `compress="true"`, but an uncompressed archive may be faster to load. Compressed files do download faster, however.<sub>注：这里讲compress属性</sub>

One thing that is important is that all the `<jar>` targets in this book have `duplicate="preserve"` set. The `duplicate` attribute tells Ant what to do when multiple filesets want to copy a file to the same path in the archive. It takes three values

- `add`: silently add the duplicate file. This is the default.
- `preserve`: ignore the duplicate file; preserve what is in the archive.
- `fail`: halt the build with an error message.

The default option, `add`, is dangerous because it silently corrupts JAR files. Ant itself will ignore the duplicate entry, and so will the JDK `jar` program. Other tools, such as the `javac` compiler, aren’t so forgiving, and will throw an `IndexOutOfBoundsException` or some other obscure stack trace if they encounter duplicate entries. If you don’t want to have users of your application or library making support calls, or want to waste mornings trying to track down these problems in other people’s libraries, change the default value! The most rigorous(谨慎的) option is `fail`, which warns of a duplication; `preserve` is good for producing good files without making a fuss.

The current preferred practice for libraries<sub>注：这里讲的是命名规则</sub> is to create an archive filename from a **project name** along with a **version number** in the format `d1-core-0.1.jar`. This lets users see at a glance what version a library is. We can support this practice with some property definitions ahead of the `jar` target:

```xml
<property name="project.name" value="${ant.project.name}" />
<property name="project.version" value="0.1alpha" />
<property name="target.name" value="${project.name}-${project.version}.jar" />
<property name="target.jar" location="${dist.dir}/${target.name}" />

<target name="jar" depends="compile">
    <jar destfile="${target.jar}"
         duplicate="preserve"
         compress="false">
        <fileset dir="${build.classes.dir}"/>
    </jar>
</target>
```

This target will now create the JAR file `dist/diary-core-0.1alpha.jar`. The `<jar>` task is **dependency-aware**; if any source file is newer than the JAR file, the JAR is rebuilt. Deleting source files doesn’t constitute a change that merits a rebuild; a clean build is needed to purge those from the JAR file.

There is an `update` attribute that looks at dependencies between **source files** and **files stored inside the archive**. It can be used for incremental JAR file updates, in which only changed files are updated. Normally, we don’t bother with things like this; we just rebuild the entire JAR when a source file changes. JAR creation time only becomes an issue with big projects, such as in EAR files or WAR files.


Once created, we need to check that the JAR file contains everything it needs.

## 2. Testing the JAR file

Just as there’s a `<jar>` task, there’s an `<unjar>` task to expand a JAR, a task which is really an alias of `<unzip>`. The task expands the Zip/JAR file into a directory tree, where you can verify that files and directories are in place either manually or by using the `<available>` and `<filesmatch>` conditions. Graphical tools may be easier to use, but they have a habit of changing the case of directories for usability, which can cause confusion. WinZip is notorious for doing this, making any all-upper-case directory lower-case and leading to regular bug reports in Ant, bug reports that are always filed as “INVALID”.

Expanding a file is easy:

```xml
<target name="unjar" depends="dist" >
    <unjar src="${target.jar}" dest="${build.dir}/unjar"/>
</target>
```

The `<unjar>` task takes a source file, specified by `src`, and a destination directory, `dest`, and unzips the file into the directory, preserving the hierarchy. It’s dependency-aware; newer files are not overwritten, and the timestamp of the files in the archive is propagated to the unzipped files.

You can selectively unzip parts of the archive, which may save time when the file is large. To use the task to **validate the build process after the archive has been unzipped**, you should **check for the existence of needed files** or, perhaps, even their values:

```xml
<target name="test-jar" depends="jar" >
    <property name="unjar.dir" location="${build.dir}/unjar"/>
    <unjar src="${target.jar}" dest="${unjar.dir}">
        <patternset>
            <include name="d1/**/*"/>
        </patternset>
    </unjar>

    <condition property="jar.uptodate">
        <filesmatch file1="${build.classes.dir}/d1/core/Event.class" file2="${unjar.dir}/d1/core/Event.class"/>
    </condition>

    <fail unless="jar.uptodate" message="file mismatch in JAR"/>
</target>
```

Here we expand classes in the archive and then verify that a file in the expanded directory tree matches that in the tree of compiled classes. Binary file comparison is a highly rigorous form of validation, but it can be slow for large files.

**To be honest, we rarely bother with these verification stages**. Instead, we include the JAR file on the classpath when we run our unit tests. This is the best verification of them all. If we left something out of the JAR , the unit tests will let us know.

## 3. Creating JAR manifests

The `<jar>` task creates a JAR manifest if needed. It will contain **the manifest version** and **the version of Ant** used to build the file:

```txt
Manifest-Version: 1.0
Ant-Version: Apache Ant 1.10.6
Created-By: 1.8.0_181-b13 (Oracle Corporation)
```

Adding a manifest to the JAR file is trivial; point the manifest attribute of the task at a predefined manifest file:

```xml
<target name="dist-with-manifest" depends="compile" description="make the distribution">
    <jar destfile="${target.jar}"
         duplicate="preserve"
         index="true"
         manifest="src/META-INF/MANIFEST.MF">
        <fileset dir="${build.classes.dir}"/>
    </jar>
</target>
```

This target needs a manifest file here in `src/META-INF/MANIFEST.MF`

```xml
Manifest-Version: 1.0
Created-By: Antbook Development Team
Sealed: true
Main-Class: d1.core.Diagnostics
```

When Ant runs the `<jar>` task, it will parse and potentially correct the manifest before inserting it into the JAR file. If the manifest is invalid, you’ll find out now, rather than when you ship.

This process has one weakness: someone has to create the manifest first. Why not create it during the build process, enabling us to use Ant properties inside the manifest? This is where the `<manifest>` task comes in.

```xml
<target name="jar-dynamic-manifest" depends="compile">
    <property name="manifest.mf" location="${build.dir}/manifest.mf"/>

    <manifest file="${manifest.mf}">
        <attribute name="Built-By" value="${user.name}"/>
        <attribute name="Sealed" value="true"/>
        <attribute name="Built-On" value="${timestamp.isoformat}"/>
        <attribute name="Main-Class" value="${main.class}"/>
    </manifest>

    <jar destfile="${target.jar}" duplicate="preserve" manifest="${manifest.mf}">
        <fileset dir="${build.classes.dir}"/>
    </jar>
</target>
```

## 4. Adding extra metadata to the JAR

There’s a nested fileset element, `<metainf>`, which lets you specify the metadata files to add to the JAR .

```xml
<jar destfile="${target.jar}"
     duplicate="preserve"
     manifest="src/META-INF/MANIFEST.MF">
    <fileset dir="${build.classes.dir}"/>
    <metainf dir="src/META-INF/"/>
</jar>
```

## 5. JAR file best practices

There are four things to consider for better `<jar>` tasks:

- Copy all the files you want to include in the JAR into one place before building. This makes it easier to see what will be included.
- Create your own manifest, either in a file or in Ant, and explicitly ask for it with the manifest attribute. If you leave it to the `<jar>` task, you get a minimal manifest.
- Always set `duplicate="preserve"`. It keeps duplicate entries out of a file and avoids possible problems later on.
- Finally, and arguably most importantly, give your libraries a **version number** at the end.

## 6. Signing JAR files

Signed JAR files are loaded slightly differently, with the classloader preventing other JAR files from declaring classes in the same packages.

To sign JAR files, we need **a public/private key pair** in a **password-protected keystore**, and Ant will need that **password**. One thing you don’t want to do is put that in the build file itself—anyone with access to the source repository will see it. This isn’t what you want in your build file:

```xml
<property name="keystore.password" value="secret"/>
```

Instead, you need the `<input>` task to prompt the user.

```xml
<target name="get-password" >
    <input addproperty="keystore.password" >password for keystore:</input>
    <echo level="verbose">password = ${keystore.password}</echo>
</target>
```

This task pauses the build with a prompt; the user then has to enter a string. In a `-verbose` run, we echo this back:

```txt
get-password:
    [input] password for keystore:
more-secret
    [echo] password = more-secret
```

Here we use `${user.home}/.secret`, a location which is restricted to the user on a Windows NTFS file system. For Unix, we want to make it readable only by us. For that, we declare a `<chmod>` operation to lock it down. This task runs the `chmod` program to set file or directory
permissions, but only on platforms that support it. On Windows, it’s a harmless no-op. We look for a `keystore.properties` file in this directory and save the keystore there to keep it private:

```xml
<target name="init-security">
    <property name="keystore.dir" location="${user.home}/.secret"/>
    <mkdir dir="${keystore.dir}"/>
    <chmod file="${keystore.dir}" perm="700"/>
    <property name="keystore" location="${keystore.dir}/local.keystore"/>
    <property file="${keystore.dir}/keystore.properties"/>
    <property name="keystore.alias" value="code.signer"/>
</target>
```

After changing the `get-password` target to depend upon this new `init-security` target, the file `keystore.properties` will be read before the `<input>` operation. If we put the relevant declaration in there (`keystore.password=hidden.secret`)

```xml
<target name="get-password" depends="init-security">
    <input addproperty="keystore.password">password for keystore:</input>
    <echo level="verbose">password = ${keystore.password}</echo>
</target>
```

this is what we see at input time:

```txt
init-security:
    [mkdir] Skipping /home/liusen/.secret because it already exists.
    [chmod] Skipping fileset for directory /home/liusen. It is empty.
 [property] Loading /home/liusen/.secret/keystore.properties

get-password:
    [input] skipping input as property keystore.password has already been set.
     [echo] password = hidden.secret
```

With the password in a property, we’re nearly ready to sign the JAR . We just need **a certificate** and the `<signjar>` task. First, **the certificate**.

### 6.1. Generating a signing key

To authenticate JARs in a Java runtime, you have to buy a certificate from one of the approved vendors. For testing purposes or for private use, you can generate a self-signed certificate using Sun’s `keytool` tool, which Ant wraps up into the `<genkey>` task. This task adds **a key** into **a keystore**, creating **the store** if needed:

```xml
    <target name="create-signing-key" depends="get-password">
        <genkey
                alias="${keystore.alias}"
                keystore="${keystore}"
                storepass="${keystore.password}"
                validity="366">
            <dname>
                <param name="CN" value="autosigner"/>
                <param name="OU" value="Steve and Erik"/>
                <param name="O" value="Apache Software Foundation"/>
                <param name="C" value="EU"/>
            </dname>
        </genkey>
    </target>
```

This task creates **a new alias** in the **keystore**, with a certificate that’s valid for 366 days. Although these keys are cryptographically sound, tools such as the Java Web Start don’t trust them. If you’re verifying JAR files in your own application, you’re free to use self-generated keys, and within an organization or community you may be able to convince end users to add your certificate (or private certification authority) to the trusted list.

### 6.2. Signing the file

The `<signjar>` task **signs JAR files**. It checksums all the entries in the file, signs these checksums, and adds them to the manifest. It also adds signature information to the `META-INF` directory in the JAR file. The task needs **the location and the password of the keystore file**, and **the alias** and **any optional extra password for the signature itself**. It will then modify the JAR file in place by invoking the `jarsigner` tool in the JDK :

```xml
<target name="sign-jar" depends="jar,get-password">
    <signjar jar="${target.jar}"
             alias="${keystore.alias}"
             keystore="${keystore}"
             storepass="${keystore.password}"/>
</target>
```

Our manifest now contains digest signatures of the classes inside the JAR, and there are new files in the `META-INF` directory, including **the public certificate of the generated pair**.

The `<signjar>` task can bulk(批量) sign a set of JAR files, using a nested `fileset` element. It also performs basic dependency checking, by not attempting to sign any files that are already signed by the user. It doesn’t check to see if the file has changed since the last signing. This means that you should not mix JAR signing with incremental JAR creation: the `update` flag in the `<jar>` task must remain at `false`.

Java behaves differently with signed JARs, and some applications can break. To be sure that this has not happened, we must take the signed JAR file of the diary classes and run our existing tests against it.

## 7. Testing With Jar Files

Running JUnit against a signed JAR file, rather than the raw classes, lets us test more things. It lets us test (1)that the classes were added to the JAR file<sub>注：第一，class文件没有缺失</sub>, (2)that we’ve remembered to add any resources the application needs<sub>注：第二，资源文件没有缺失</sub>, and (3)that the signing process has not broken anything<sub>注：第三，签名没有破坏Jar文件</sub>. It also lets us state that the tests were run against the redistributables, which is something to be proud of.

It is very easy to test against the JAR file. Recall that we set up our classpath for compiling and running tests like this:

```xml
<path id="test.compile.classpath">
    <path refid="compile.classpath"/>
    <pathelement location="${build.classes.dir}"/>
</path>
```

We need to change one line to run against the generated JAR file:

```xml
<path id="test.compile.classpath">
    <path refid="compile.classpath"/>
    <pathelement location="${target.jar}"/>
</path>
```

To verify everything works, run `ant clean test`. As `clean` builds are usually fast, don’t be afraid to run them regularly.

```xml
<path id="test.jar.classpath">
    <path refid="compile.classpath"/>
    <pathelement location="${target.jar}"/>
    <pathelement location="${test.classes.dir}"/>
</path>

<target name="test-jar" depends="test-compile,sign-jar">
    <junit printsummary="false" haltonfailure="false">
        <classpath refid="test.jar.classpath"/>
        <formatter type="brief" usefile="false"/>
        <batchtest todir="${test.data.dir}">
            <fileset dir="${test.classes.dir}"
                     includes="**/test/*Test.class"/>
        </batchtest>
    </junit>
</target>
```
