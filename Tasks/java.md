# java Task

## Best Practices

When calling a Java program, we recommend that you

- Set the arguments using one `<arg>` entry per parameter.
- Use `<arg file="filename">` to pass in file parameters.
- Explicitly state the classpath, rather than rely on Ant’s own classpath.
- Set `failonerror="true"` unless you want to ignore failures or capture the result.
