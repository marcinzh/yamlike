


# Yamlike

Two tiny DSLs providing YAML-like list syntax. Inspired by fewerBraces extension for Scala 3.


## 1. yamlist

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.marcinzh/yamlist_3/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.marcinzh/yamlist_3)

Provides a tiny, auto-derivable typeclass for list elements. Together with fewerBraces 
extension, allows creating custom DSLs with YAML-like list syntax:

```Scala
val data =
  foo:
    - bar
    - qux:
    	- baz
```

Usage in [examples](./modules/yamlist-examples/src/main/scala/examples).

```Scala
libraryDependencies += "io.github.marcinzh" %% "yamlist" % "0.2.0"
```


## 2. yamlayer

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.marcinzh/yamlayer_3/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.marcinzh/yamlayer_3)

Augmentation of ZIO 1.x.

ZIO 2.x introduced `provide` macro. It works, by taking an arbitrarily ordered collection of `ZLayer`s, and automatically building dependency graph from them.

This subproject extends ZIO 1.x with single `provideLayers` method. It brings in functionality similar to ZIO 2.x's `provide`.
The main difference, is that in `yamlayer` the topological sorting of dependencies
happens at **runtime**, instead of at **compile** time (no macros are used).

Usage:

```Scala
import zio._
import yamlayer._

someZIOProgram.provideLayers(
  - layer1
  - layer2
  - layer3
)
```

Or with fewerBraces extension, for even more YAML-like look 😎:
```Scala
someZIOProgram.provideLayers:
  - layer1
  - layer2
  - layer3
```

Cross compiled for Scala 2.13.x and 3.x.

```Scala
libraryDependencies += "io.github.marcinzh" %% "yamlayer" % "0.2.0"
```
