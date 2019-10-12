# Ivy Files

<!-- TOC -->

- [1. Intro](#1-intro)
- [2. Attributes](#2-attributes)
- [3. Child element](#3-child-element)
  - [3.1. info](#31-info)
  - [3.2. dependencies](#32-dependencies)
  - [dependency](#dependency)
- [4. Reference](#4-reference)

<!-- /TOC -->

## 1. Intro

Ivy use is entirely based on **module descriptors** known as "Ivy files". Ivy files are XML files, usually called `ivy.xml`, containing the description of **the dependencies of a module**, **its published artifacts** and **its configurations**.

Here is the simplest Ivy file you can write:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<ivy-module version="2.0">
    <info organisation="myorg" module="mymodule"/>
</ivy-module>
```

For those familiar with XML schema, the schema used to validate Ivy files can be found [here](http://ant.apache.org/ivy/schemas/ivy.xsd). For those using XSD aware IDE, you can declare the XSD in your Ivy files to benefit from code completion / validation:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<ivy-module version="2.0"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:noNamespaceSchemaLocation="http://ant.apache.org/ivy/schemas/ivy.xsd">
  <info organisation="myorg" module="mymodule"/>
</ivy-module>
```

## 2. Attributes

- `version`: the version of the Ivy file specification - should be '2.0' with current version of Ivy

## 3. Child element

### 3.1. info

- `status`: the status of this module. Default value is `integration`.

**Three statuses** are defined by default in Ivy:

- `integration`: revisions built by a continuous build, a nightly build, and so on, fall in this category
- `milestone`: revisions delivered to the public but not actually finished fall in this category
- `release`: a revision fully tested and labelled fall in this category

### 3.2. dependencies

### dependency

rev:

- `latest.integration`: selects the latest revision of the dependency module.
- `latest.[any status]`: selects the latest revision of the dependency module with at least the specified status.

For instance, `latest.milestone` will select the latest version being either a `milestone` or a `release`, and `latest.release` will only select the latest `release`.

## 4. Reference

- [Terminology](http://ant.apache.org/ivy/history/master/terminology.html)
- [Ivy Files](http://ant.apache.org/ivy/history/master/ivyfile.html)
- [info](http://ant.apache.org/ivy/history/master/ivyfile/info.html)
- [dependencies](http://ant.apache.org/ivy/history/master/ivyfile/dependencies.html)





