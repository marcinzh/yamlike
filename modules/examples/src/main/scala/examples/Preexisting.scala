package examples
import yamlike._


object Preexisting:
  def run(): Unit =
    for x <- build do
      println(x)

  def list[T](xs: Yamlist[T]): List[T] = xs.unwrap.toList

  def build: List[String] =
    given Yamlike[String] = Yamlike.derived

    list:
      - "foo"
      - "bar"
      - "qux"
