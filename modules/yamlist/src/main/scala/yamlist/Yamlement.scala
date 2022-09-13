package yamlist


/** Typeclass enabling YAML-like syntax for list elements */

sealed trait Yamlement[A]:
  extension (a: A)
    final def unary_-[B](using y: Yamlement.Into[A, B]): Yamlist[B] = Yamlist(Vector(y.fun(a)))


object Yamlement:
  def apply[T]: Yamlement[T] = new Yamlement[T] {}
  def derived[T] = apply[T]

  /** Optional conversion */
  case class Into[A, B](fun: A => B)

  object Into:
    given [A]: Into[A, A] = Into(x => x)
