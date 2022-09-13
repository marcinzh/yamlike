package examples
import yamlist._


object Main:
  def main(args: Array[String]): Unit =
    Preexisting.run()
    println()
    Deriving.run()
    println()
    TreeOfStrings.run()
