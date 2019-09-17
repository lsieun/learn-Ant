# mkdir Task

The Ant task `<mkdir>` creates a directory. In fact, **it creates parent directories**, too, **if needed**:

```xml
<property name="dist.dir" location="dist"/>
<property name="dist.doc.dir" location="${dist.dir}/doc"/>

<mkdir dir="${dist.doc.dir}"/>
```

> 至此结束： Hope for the best, but prepare for the worst.
