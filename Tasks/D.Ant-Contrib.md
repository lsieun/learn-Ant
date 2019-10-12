# Ant-Contrib Tasks

<!-- TOC -->

- [1. What's this?](#1-whats-this)
- [2. Download ant-contrib](#2-download-ant-contrib)
- [3. Install ant-contrib](#3-install-ant-contrib)
  - [3.1. 第一种方法](#31-%e7%ac%ac%e4%b8%80%e7%a7%8d%e6%96%b9%e6%b3%95)
  - [3.2. 第二种方法](#32-%e7%ac%ac%e4%ba%8c%e7%a7%8d%e6%96%b9%e6%b3%95)
  - [3.3. 第三种方法](#33-%e7%ac%ac%e4%b8%89%e7%a7%8d%e6%96%b9%e6%b3%95)
  - [3.4. 第四种方法](#34-%e7%ac%ac%e5%9b%9b%e7%a7%8d%e6%96%b9%e6%b3%95)
- [4. Usage](#4-usage)
  - [4.1. if](#41-if)
  - [4.2. switch](#42-switch)
  - [4.3. for](#43-for)
  - [4.4. variable](#44-variable)
  - [4.5. math](#45-math)
- [5. Reference](#5-reference)

<!-- /TOC -->

## 1. What's this?

The [Ant-Contrib](http://ant-contrib.sourceforge.net/) project is a collection of tasks (and at one point maybe types and other tools) for [Apache Ant](http://ant.apache.org/).

## 2. Download ant-contrib

Download ANT Contrib: [https://sourceforge.net/projects/ant-contrib/files/](https://sourceforge.net/projects/ant-contrib/files/)

## 3. Install ant-contrib

|       ant-contrib         | 在ant classpath下 | 不在       |
| ------------------------- | ----------------- | ---------- |
| 使用properties文件        | 第一种方法        | 第二种方法 |
| 使用Antlib方式（XML文件） | 第三种方法        | 第四种方法 |

### 3.1. 第一种方法

Copy `ant-contrib.jar` to the `lib` directory of your Ant installation. If you want to use one of the tasks in your own project, add the following lines to your build file.

```xml
<taskdef resource="net/sf/antcontrib/antcontrib.properties"/>
```

### 3.2. 第二种方法

Keep `ant-contrib.jar` in a separate location. You now have to tell Ant explicitly where to find it (say in `${user.home}/lib/ant`):

```xml
<!-- 使用attribute -->
<taskdef resource="net/sf/antcontrib/antcontrib.properties" classpath="${user.home}/lib/ant/ant-contrib.jar"/>

<!-- 使用nested element -->
<taskdef resource="net/sf/antcontrib/antcontrib.properties">
    <classpath>
        <pathelement location="${user.home}/lib/ant/ant-contrib.jar"/>
    </classpath>
</taskdef>

<path id="tasks.classpath">
    <fileset dir="${user.home}/lib/ant/" includes="*.jar"/>
</path>

<!-- 使用attribute + refid -->
<taskdef resource="net/sf/antcontrib/antcontrib.properties" classpathref="tasks.classpath"/>

<!-- 使用nested element + refid -->
<taskdef resource="net/sf/antcontrib/antcontrib.properties">
    <classpath refid="tasks.classpath"/>
</taskdef>
```

### 3.3. 第三种方法

```xml
<taskdef resource="net/sf/antcontrib/antlib.xml"/>
```

### 3.4. 第四种方法

需要单独指定`ant-contrib.jar`的位置：

```xml
<!-- 使用attribute -->
<taskdef resource="net/sf/antcontrib/antlib.xml"  classpath="${user.home}/lib/ant/ant-contrib.jar"/>

<!-- 使用nested element -->
<taskdef resource="net/sf/antcontrib/antlib.xml">
    <classpath>
        <pathelement location="${user.home}/lib/ant/ant-contrib.jar"/>
    </classpath>
</taskdef>

<path id="tasks.classpath">
    <fileset dir="${user.home}/lib/ant/" includes="*.jar"/>
</path>

<!-- 使用attribute + refid -->
<taskdef resource="net/sf/antcontrib/antlib.xml" classpathref="tasks.classpath"/>

<!-- 使用nested element + refid -->
<taskdef resource="net/sf/antcontrib/antlib.xml">
    <classpath refid="tasks.classpath"/>
</taskdef>
```

## 4. Usage

```xml
<taskdef resource="net/sf/antcontrib/antlib.xml">
    <classpath>
        <pathelement location="${user.home}/lib/ant/ant-contrib.jar"/>
    </classpath>
</taskdef>

<property name="what.is.your.name" value="mobile.qq"/>
```

### 4.1. if

```xml
<target name="test-if">
    <if>
        <equals arg1="${what.is.your.name}" arg2="tom"/>
        <then>
            <echo message="My name is Tom."></echo>
        </then>

        <elseif>
            <equals arg1="${what.is.your.name}" arg2="jerry"/>
            <then>
                <echo message="My name is Jerry."></echo>
            </then>
        </elseif>

        <else>
            <echo message="I don't know your name."/>
        </else>
    </if>
</target>
```

### 4.2. switch

```xml
<target name="test-switch">
    <switch value="${what.is.your.name}">
        <case value="mobile">
            <echo message="The value of property is mobile" />
        </case>
        <case value="qq">
            <echo message="The value of property is qq" />
        </case>
        <default>
            <echo message="The value of property is ${what.is.your.name}" />
        </default>
    </switch>
</target>
```

### 4.3. for

```xml
<target name="test-for-string">
    <for list="a,b,c,d,e" param="letter">
        <sequential>
            <echo>Letter @{letter}</echo>
        </sequential>
    </for>
</target>
```

```xml
<target name="test-for-file">
    <for param="xmlfile">
        <fileset dir="${basedir}" includes="**/*.xml" excludes="**/.idea/**/*.xml"/>
        <sequential>
            <copy file="@{xmlfile}" todir="${build.classes.dir}"></copy>
            <echo message="@{xmlfile}"/>
        </sequential>
    </for>
</target>
```

### 4.4. variable

作为“变量”：可以进行变化

```xml
<target name="test-variable-change">
    <property name="x" value="6"/>
    <echo>${x}</echo>   <!-- will print 6 -->

    <var name="x" unset="true"/>

    <property name="x" value="12"/>
    <echo>${x}</echo>   <!-- will print 12 -->
</target>
```

作为“字符串”： 字符串拼接

```xml
<target name="test-variable-concat">
    <var name="str" value="I"/>
    <var name="str" value="${str} am"/>
    <var name="str" value="${str} a"/>
    <var name="str" value="${str} string."/>
    <echo>${str}</echo> <!-- print: I am a string. -->
</target>
```

### 4.5. math

作为“数值”： 加法运算

```xml
<target name="test-math">
    <var name="op1" value="12"/>
    <var name="op2" value="6"/>
    <var name="op" value="+"/>
    <math result="result" operand1="${op1}"
          operation="${op}"
          operand2="${op2}"
          datatype="int"/>
    <echo>${op1} ${op} ${op2} = ${result}</echo>
</target>
```

## 5. Reference

- [sourceforge: Ant-Contrib Tasks(Home Page)](http://ant-contrib.sourceforge.net/)
- [ANT Contrib Download](https://sourceforge.net/projects/ant-contrib/files/)
