package examples
import yamlist._


object TreeOfStrings:
  def run() =
    build.show()


  case class Streeng(label: String, children: Vector[Streeng]) derives Yamlement:
    def show(): Unit =
      def loop(indent: String, tree: Streeng): Unit =
        val prefix = if indent.isEmpty then "" else s"$indent- "
        val s = prefix + tree.label
        if tree.children.isEmpty then
          println(s)
        else
          println(s"$s:")
          for x <- tree.children do loop(s"$indent  ", x)
      loop("", this)


  object Syntax:
    extension (thiś: String) def apply(xs: Yamlist[Streeng]): Streeng = Streeng(thiś, xs.unwrap)
    given Yamlement.Into[String, Streeng] = Yamlement.Into(Streeng(_, Vector()))
    given Yamlement[String] = Yamlement.derived


  def build: Streeng =
    import Syntax.{given, _}

    // Doesn't compile without the braces :(
    ("root"):
      - "a"
      - ("b"):
        - ("c"):
          - "d"
          - "e"
      - ("f"):
        - "g"
