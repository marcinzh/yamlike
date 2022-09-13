package yamlayer
import zio.{ZIO, ZLayer, Tag, Has, LightTypeTag => LTT}


private [yamlayer] final class Node(val someLayer: CapturedLayer[_, _ <: Has[_]]) {
  val requires: Set[LTT] = Node.tagToSet(someLayer.tagIn)
  val provides: Set[LTT] = Node.tagToSet(someLayer.tagOut)
}


private [yamlayer] final class CapturedLayer[RIn, ROut <: Has[_]](
  val layer: ZLayer[RIn, _, ROut],
  val tagIn: Tag[RIn],
  val tagOut: Tag[ROut],
) {
  def castIn[RIn2] = this.asInstanceOf[CapturedLayer[RIn2, ROut]]
}


private [yamlayer] final class FreeLayer[R <: Has[_]](
  val layer: ZLayer[Any, _, R],
  val tag: Tag[R],
) {
  def plus[ROut <: Has[_]](that: CapturedLayer[_, ROut]): FreeLayer[R with ROut] = {
    implicit def a: Tag[R] = tag
    implicit def b: Tag[ROut] = that.tagOut
    new FreeLayer(layer >+> that.castIn[R].layer, implicitly[Tag[R with ROut]])
  }
  def castOut[R2 <: Has[_]] = this.asInstanceOf[FreeLayer[R2]]
  def get[E]: ZLayer[Any, E, R] = layer.asInstanceOf[ZLayer[Any, E, R]]
}


private [yamlayer] object FreeLayer {
  def empty = new FreeLayer(ZLayer.succeed(()), implicitly[Tag[Has[Unit]]])
}


private [yamlayer] object Node {
  def apply[RIn: Tag, E, ROut <: Has[_]: Tag](layer: ZLayer[RIn, E, ROut])(implicit tagIn: Tag[RIn], tagOut: Tag[ROut]): Node =
    new Node(new CapturedLayer(layer, tagIn, tagOut))

  private val tagOfAny = Tag[Any]

  def tagToSet(tag: Tag[_]): Set[LTT] =
    if (tag == tagOfAny)
      Set()
    else
      tag.tag.decompose

  def layerFromGraph[R, E](allNodes: Vector[Node]): ZLayer[Any, E, R] = {
    checkAmbig(allNodes)

    val requiredBy: Map[LTT, Set[Node]] =
      (for {
        n <- allNodes
        t <- n.requires
      } yield (t, Set(n)))
      .groupMapReduce(_._1)(_._2)(_ | _)
      .withDefaultValue(Set())

    def loop[A <: Has[_], B <: Has[_]](todos0: Vector[Node], unsat0: Map[Node, Set[LTT]], accum0: FreeLayer[A]): FreeLayer[B] = {
      if (todos0.isEmpty) {
        val failedUnsat = unsat0.filter(_._2.nonEmpty)
        if (failedUnsat.isEmpty)
          accum0.castOut[B]
        else
          sys.error(s"Failed to satisfy dependencies of ${failedUnsat.size} layers.")
      }
      else {
        val node = todos0.head
        val more = todos0.tail
        val accum1 = accum0.plus(node.someLayer).castOut[A]
        val (todos1, unsat1) =
          node.provides.flatMap(requiredBy).foldLeft((more, unsat0)) {
            case ((todos, unsat), next) =>
              val remainingDeps: Set[LTT] = unsat(next) -- node.provides
              val unsat2 = unsat.updated(next, remainingDeps)
              val todos2 = if (remainingDeps.nonEmpty) todos else todos :+ next
              (todos2, unsat2)
          }
        loop(todos1, unsat1, accum1)
      }
    }

    val unsat: Map[Node, Set[LTT]] = (for { n <- allNodes.iterator } yield n -> n.requires).toMap
    val roots: Vector[Node] = allNodes.filter(_.requires.isEmpty)
    loop(roots, unsat, FreeLayer.empty).get
  }

  private def checkAmbig(allNodes: Vector[Node]): Unit = {
    val m = 
      (for {
        n <- allNodes
        t <- n.provides
      } yield (t, n))
      .groupMap(_._1)(_._2)
      .filter(_._2.size > 1)
    
    if (m.nonEmpty)
      sys.error(s"Found ${m.size} dependencies with ambiguous providers.")
  }
}
