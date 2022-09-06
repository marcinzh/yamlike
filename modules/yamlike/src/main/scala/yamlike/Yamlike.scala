package yamlike


sealed trait Yamlike[A]:
  extension (a: A)
    final def unary_-[B](using y: Yamlike.Into[A, B]): Yamlist[B] = Yamlist(Vector(y.fun(a)))


object Yamlike:
  def apply[T]: Yamlike[T] = new Yamlike[T] {}
  def derived[T] = apply[T]

  case class Into[A, B](fun: A => B)

  object Into:
    given [A]: Into[A, A] = Into(x => x)


final case class Yamlist[+B](unwrap: Vector[B]):
  def -[A, B2 >: B](a: A)(using y: Yamlike.Into[A, B2]): Yamlist[B2] = Yamlist(unwrap :+ y.fun(a))
