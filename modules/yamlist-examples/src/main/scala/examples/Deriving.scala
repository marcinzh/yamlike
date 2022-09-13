package examples
import yamlist._


object Deriving:
  def run() =
    build.show()


  enum Tree[T] derives Yamlement:
    case Leaf(leaf: T)
    case Branch(trees: Vector[Tree[T]])

    def show(): Unit =
      def loop(indent: String, tree: Tree[T]): Unit =
        val prefix = if indent.isEmpty then "" else s"$indent- "
        tree match
          case Leaf(x) => println(s"${prefix}$x")
          case Branch(xs) =>
            println(s"${prefix}tree:")
            for x <- xs do loop(s"$indent  ", x)
      loop("", this)


  object Syntax:
    def tree[T](y: Yamlist[Tree[T]]): Tree[T] = Tree.Branch(y.unwrap)
    given Yamlement.Into[String, Tree[String]] = Yamlement.Into(Tree.Leaf(_))
    given Yamlement[String] = Yamlement.derived


  def build: Tree[String] =
    import Syntax.{given, _}

    tree:
      - "a"
      - "b"
      - tree:
        - tree:
          - "c"
          - "d"
        - "e"
        - "f"
      - "g"
