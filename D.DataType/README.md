# Data Type

In programming language terms, **Ant’s tasks** represent the functionality offered by the runtime libraries. **The tasks** are useful only with **data**, the information that they need to know what to do. Java is an object-oriented language where data and functions are mixed into classes. Ant, although written in Java, differentiates between **the tasks** that do the work and **the data** they work with—data represented as **datatypes**. Ant also has the approximate equivalent of variables in its **properties**.

```txt
程序 = 算法 + 数据结构
Java --> function + data --> class
Ant  --> task + data type + properties

- task: 实现具体的功能，形象的说就是某种“机甲工厂”
- data type: 存储数据的各种容器，形象的说它是一个“燃料箱”，供应“机甲工厂”运行，但它只是一个“空壳”，而没有“灵魂”
- properties: 这里就是“变量”，形象的说就是“灵魂”，它能充满“燃料箱”，引导整个“机甲工厂”运作起来
```

To pass **data** to **tasks**, you need to be able to construct and refer to **datatypes** and **properties** in a build file. As with **tasks**, **datatypes** are just pieces of XML , pieces that list files or other resources that a task can use.





