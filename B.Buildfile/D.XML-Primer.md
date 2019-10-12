# XML Primer

<!-- TOC -->

- [1. Character sets](#1-character-sets)
- [2. Binary data](#2-binary-data)
- [3. Escaping characters](#3-escaping-characters)
- [4. XML Namespace](#4-xml-namespace)
- [5. Namespace best practices](#5-namespace-best-practices)

<!-- /TOC -->

XML documents should begin with **an XML prolog**, which indicates **the version** and, optionally, **the encoding of the XML file**—usually the string `<?xml version="1.0"?>`. Next comes the XML content. This must consist of a single XML root element, which can contain other XML content nested inside.

All Ant documents must have `project` as the root element, so all Ant XML files should have a structure something like this:

```xml
<?xml version="1.0"?> <!-- Prolog -->
<project name="example"> <!-- Root element -->

    <!-- Tasks and targets go here -->

</project> <!-- Close the root element -->
```

## 1. Character sets

The default character set of XML files is not that of the local system; it is `UTF-8` encoded Unicode.

For example, to use the European character set, you must declare in the prolog that the file is in the `ISO 8859-1` format:

```xml
<?xml version='1.0' encoding="iso-8859-1"?>
<?xml version="1.0" encoding="UTF-8"?>
```

## 2. Binary data

XML **cannot** contain **binary data**; it has to be encoded using techniques like **base-64 encoding**. This is rarely an issue in Ant build files.

## 3. Escaping characters

The most common symbols that you must escape in an Ant file.

| Symbol                                           | Ant XML representation |
| ------------------------------------------------ | ---------------------- |
| `<`                                              | `&lt;`                 |
| `>`                                              | `&gt;`                 |
| `"`                                              | `&quot;`               |
| `'`                                              | `&apos;`               |
| newline; `\n`                                    | `&#10;`                |
| A Unicode character(such as ß, hex value `00df`) | `&#x00df;`             |

```xml
<echo message="&apos;&lt;$&gt;&amp;&quot;"/>
<echo message="&#x00df;"/>
```

Output:

```txt
[echo] '<$>&"
[echo] ß
```

Because escaping characters can become very messy and inconvenient, XML provides a mechanism for allowing unescaped text within **a CDATA section**. In Ant’s build files, CDATA sections typically appear around script blocks or SQL commands. **CDATA sections** begin with `<![CDATA[` and end with `]]>` . The ending sequence of a CDATA section is the only set of characters that requires escaping internally. A CDATA example is

```xml
<echo><![CDATA[<b>hello</b> world]]></echo>
```

Output:

```txt
[echo] <b>hello</b> world
```

Even within **CDATA** or **Unicode escaping**, not all characters are allowed. As an example, the ASCII `NUL` symbol, `\u0000`, is forbidden, even with an `&#00;` declaration. The only allowed characters in the range `0-31` are `8`, `10` and `13`; **tab**, **newline** and **carriage-return**.

## 4. XML Namespace

```txt
prefix --> XML namespace --> URI/URL --> no requirement for any file to be retrievable
```

## 5. Namespace best practices

To keep the complexity of XML namespaces manageable in a build file, here are some good practices:

- Use the same prefix for the same namespace across all your build files.
- Never declare a new default namespace in Ant projects; **don’t** use an `xmnls="http://antbook.org"` declaration.
- Declare the namespaces in the root `<project>` element if they are to be used in more than one place, or in the `<target>` that contains the tasks.

