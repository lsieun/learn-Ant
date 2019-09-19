# Run Native Programs

A native program is any program compiled for the local system, or, in Unix, any shell script marked as executable.

To run an external program in Ant, use the `<exec>` task. It lets you declare the following options:

- The name of the program and arguments to pass in
- The directory in which it runs
- Whether the application failure halts the build
- The maximum duration of a program
- A file or a property to store the output
- Other Ant components to act as input sources or destinations
- Environment variables
- A string that should be in the name of the OS

One use of the task is to create a symbolic link to a file for which there is no intrinsic Java command:

```xml
<exec executable="ln">
    <arg value="-s"/>
    <arg value="build.xml"/>
    <arg value="symlink.xml"/>
</exec>
```

You don’t need to supply the full path to the executable if it’s on the current path.

## Executing shell commands

```xml
<exec executable="sh" failonerror="true">
    <arg line="-c 'jps -l &gt; processes.txt'"/>
</exec>
```

A lot of people encounter problems trying to run shell commands on Ant. All you have to do is remember that **shell commands aren’t native programs**. It’s the shell itself that Ant must start with the `<exec>` task.
