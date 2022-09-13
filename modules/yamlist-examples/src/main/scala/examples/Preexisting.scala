package examples
import yamlist._


object Preexisting:
  def run() =
    for x <- build do
      println(x)


  object Syntax:
    def list[T](xs: Yamlist[T]): List[T] = xs.unwrap.toList
    given Yamlement[String] = Yamlement.derived


  def build: List[String] =
    import Syntax.{given, _}

    list:
      - "foo"
      - "bar"
      - "qux"
