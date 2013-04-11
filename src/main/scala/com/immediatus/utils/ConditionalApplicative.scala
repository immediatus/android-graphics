package com.immediatus.utils

sealed class ConditionalApplicative[T] private (val value: T) {
   class ElseApplicative(value: T, elseCondition: Boolean) extends ConditionalApplicative[T](value) {
      def $else(f: T => T): T = if(elseCondition) f(value) else value
   }

   def $if(condition: => Boolean)(f: T => T): ElseApplicative =
      if(condition) new ElseApplicative(f(value), false)
      else new ElseApplicative(value, true)

   def $if(condition: T => Boolean)(f: T => T): ElseApplicative =
      if(condition(value)) new ElseApplicative(f(value), false)
      else new ElseApplicative(value, true)
}

object ConditionalApplicative {
   implicit def lift2ConditionalApplicative[T](any: T): ConditionalApplicative[T] = new ConditionalApplicative(any)
   implicit def else2T[T](els: ConditionalApplicative[T]#ElseApplicative): T = els.value
}
