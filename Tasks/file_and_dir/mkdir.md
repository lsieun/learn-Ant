# mkdir

We do need to create the top-level `build` directory and the `classes` subdirectory. We do this with the Ant task `<mkdir>`, which, like the shell command of the same name, creates a directory. In fact, **it creates parent directories**, too, **if needed**:

```xml
<mkdir dir="build/classes">
<mkdir dir="dist">
```

