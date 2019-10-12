# Optional Tasks

## Ant optional tasks都有哪些呢？

- Testing： `<junit>`, `<junitreport>`
- Networking： `<telnet>`, `<ftp>`, `<sshexec>`, `<scp>`, `<setproxy>`, `<rexec>`

## classpath

Most **optional tasks** need **an extra library or program** to work, and they come in JAR files that group them by their dependencies.

For example, the `<scp>` task of lives in `ant-jsch.jar` and depends upon the `jsch.jar` SSH library.

| Task    | 所在Jar包      | 依赖Jar包  |
| ------- | -------------- | ---------- |
| `<scp>` | `ant-jsch.jar` | `jsch.jar` |

Those tasks that depend on extra libraries usually need them in Ant’s own classpath. That means they must be in (1)one of the directories from which Ant loads JAR files, (2)a directory named on the command line with the `-lib` option, or (3)listed in the `CLASSPATH` environment variable.

## Installing Optional Tasks

To use most optional tasks, you must download and install extra libraries or programs. The JAR containing the task itself isn’t always enough: any extra libraries that an optional task depends on must be on the classpath of Ant or you’ll see an error message.

The power tool for tracking down many of these problems is `ant -diagnostics`.

There is also a `<diagnostics>` task, which runs the diagnostics code inside Ant itself:

```xml
<target name="diagnostics" description="diagnostics">
    <diagnostics/>
</target>
```

## Interlude: how to automatically fetch JAR files for use with Ant

If you’re online and a proxy server is not blocking outbound HTTP connections, change to Ant’s home directory, then type:

```bash
ant -f fetch.xml all
```

This code runs a special build file that fetches all the libraries that Ant needs and saves them in `ANT_HOME/lib`. If you need to save the JAR files in your personal `${user.home}/.ant/lib` directory, add the `-Ddest=user` clause.


