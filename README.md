# Yamlike

YAML-like list syntax for Scala 3, abusing experimental fewerBraces extension.


## Examples

```scala
val data =
  tree:
    - "foo"
    - tree:
    	- "bar"
    	- "baz"
    - "qux"
```

More in [examples](./modules/examples/src/main/scala/examples).
