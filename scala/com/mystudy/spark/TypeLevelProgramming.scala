package com.mystudy.spark

object TypeLevelProgramming {
  //boilerplate
  import scala.reflect.runtime.universe._

  def show[T](value:T)(implicit tag: TypeTag[T]) =tag.toString().replace("com.mystudy.spark.TypeLevelProgramming","").replace("TypeTag","")

  //type-level programming
  //Peano arithmetics
  trait Nat
  class _0 extends Nat
  class Succ[N<:Nat] extends Nat
  type _1=Succ[_0]
  type _2=Succ[_1]
  type _3=Succ[_2]
  type _4=Succ[_3]
  type _5=Succ[_4]

  trait <[A<:Nat, B<:Nat]

  object < {
    implicit def ltBasic[B<:Nat]: <[_0, Succ[B]] = new <[_0,Succ[B]]{}
    implicit def inductive[A <: Nat, B <: Nat](implicit lt: <[A,B]): <[Succ[A],Succ[B]] = new <[Succ[A],Succ[B]] {}
    def apply[A <: Nat, B <: Nat](implicit  lt: <[A,B]) = lt

  }

  val comparison: <[_1,_3] = <[_1, _3]
  val comper : _3 < _4 = <[_3,_4]

  def main(args:Array[String]):Unit={
    println(show(comparison))
    println(show(comper))
  }

}
