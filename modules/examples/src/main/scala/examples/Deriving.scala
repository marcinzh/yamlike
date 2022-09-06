package examples
import yamlike._


object Deriving:
  def run() =
    build.show()

  enum Tree[T] derives Yamlike:
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

  def tree[T](y: Yamlist[Tree[T]]): Tree[T] = Tree.Branch(y.unwrap)

  def build: Tree[String] =
    given Yamlike.Into[String, Tree[String]] = Yamlike.Into(Tree.Leaf(_))
    given Yamlike[String] = Yamlike.derived

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
