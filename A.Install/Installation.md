# Installation

## Install Ant

Download a Binary Distribution(`apache-ant-*-bin.tar.gz`)

```txt
https://ant.apache.org/bindownload.cgi
```

Unpacking the Binary Distribution

```bash
sudo tar -xzf apache-ant-bin.tar.gz -C /usr/local
```

Set the `ANT_HOME` environment variable:

```bash
sudo vim /etc/profile
```

```txt
export ANT_HOME=/usr/local/ant
export JAVA_HOME=/usr/local/jdk8
export PATH=${PATH}:${ANT_HOME}/bin:${JAVA_HOME}/bin
```

## Install Ivy


