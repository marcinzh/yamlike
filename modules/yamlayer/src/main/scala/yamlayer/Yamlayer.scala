package yamlayer
import zio.{ZIO, ZLayer, Has, Tag}


final class Yamlayer[-RIn, +E, +ROut](nodes: Vector[Node]) {
  def -[E1 >: E, RIn1 <: RIn, RIn2, ROut1 >: ROut, ROut2 <: Has[_]](
    layer: ZLayer[RIn2, E1, ROut2]
  )(implicit tagIn: Tag[RIn2], tagOut: Tag[ROut2]): Yamlayer[RIn1 with RIn2, E1, ROut1 with ROut2] =
    new Yamlayer(nodes :+ Node(layer))

  def composeAll: ZLayer[Any, E, ROut] = Node.layerFromGraph(nodes)
}


object Yamlayer {
  def one[RIn, E, ROut <: Has[_]](layer: ZLayer[RIn, E, ROut])(implicit tagIn: Tag[RIn], tagOut: Tag[ROut]) =
    new Yamlayer(Vector(Node(layer)))
}
