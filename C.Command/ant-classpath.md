# How Ant adds libraries to its classpath

When you type `ant` at the command line, it runs Ant’s launcher code in `ant-launcher.jar`. This sets up the classpath for the rest of the run by

- Adding every JAR listed in the `CLASSPATH` environment variable, unless the `-noclasspath` option is set
- Adding every JAR in the `ANT_HOME/lib` directory
- Adding every JAR in `${user.home}/.ant/lib`, where `${user.home}` is the OS-specific home directory of the user, unless the `-nouserlib` option is set
- Adding every JAR in every directory listed on the command line with the `-lib` option

The key thing to know is that all JAR files in `ANT_HOME/lib` and `${user.home}/.ant/lib` are added to Ant’s classpath automatically.

To see what is on Ant’s classpath, type:

```bash
ant -diagnostics
```

## Interlude: how to automatically fetch JAR files for use with Ant

If you’re online and a proxy server is not blocking outbound HTTP connections, change to Ant’s home directory, then type:

```bash
ant -f fetch.xml all
```

This code runs a special build file that fetches all the libraries that Ant needs and saves them in `ANT_HOME/lib`. If you need to save the JAR files in your personal `${user.home}/.ant/lib` directory, add the `-Ddest=user` clause.









