package com.immediatus.graphics.utils

case class Point(private val _x: Float, private val _y: Float) {

   def x = _x
   def x_=(x: Float) = Point(x, _y)

   def y = _y
   def y_=(y: Float) = Point(_x, y)

   def set(x: Float, y: Float) = Point(x, y)
   def offset(dx: Float, dy: Float) = Point(_x + dx, _y + dy)
}
