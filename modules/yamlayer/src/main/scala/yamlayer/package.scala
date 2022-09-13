import zio.{ZIO, ZLayer, Has, Tag, NeedsEnv}


package object yamlayer {
  implicit final class ZLayer_Extension[RIn, E, ROut <: Has[_]](thiś: ZLayer[RIn, E, ROut]) {
    final def unary_-(implicit tagIn: Tag[RIn], tagOut: Tag[ROut]): Yamlayer[RIn, E, ROut] =
      Yamlayer.one(thiś)
  }

  implicit final class ZIO_Extension[R, E, A](thiś: ZIO[R, E, A]) {
    final def provideLayers[RIn, E1 >: E, ROut <: Has[_]](y: Yamlayer[RIn, E1, ROut])(
      implicit ev1: ROut <:< R, ev2: ROut <:< RIn, ev3: NeedsEnv[R]
    ): ZIO[Any, E1, A] =
      thiś.provideLayer(y.composeAll)
  }
}
