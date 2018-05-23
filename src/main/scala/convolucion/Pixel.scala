package convolucion

trait Pixel[Repr[Number], @specialized(Double, Int) Number] {
  def scale(n: Number): Repr[Number]
  def plus(that: Repr[Number]): Repr[Number]

  def *(n: Number): Repr[Number] = scale(n)
  def +(that: Repr[Number]): Repr[Number] = plus(that)
}
