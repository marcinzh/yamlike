package yamlist


/** Wrapper for a sequence that collects elements using YAML-like syntax */

final case class Yamlist[+B](unwrap: Vector[B]):
  def -[A, B2 >: B](a: A)(using y: Yamlement.Into[A, B2]): Yamlist[B2] = Yamlist(unwrap :+ y.fun(a))
