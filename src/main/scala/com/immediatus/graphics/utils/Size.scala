package com.immediatus.graphics.utils

case class Size(private val _width: Float, private val _height: Float) {

   def width = _width
   def width_=(w: Float) = Size(w, _height)

   def height = _height
   def height_=(h: Float) = Size(_width, h)

   def set(w: Float, h: Float) = Size(w, h)
}
