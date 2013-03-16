package com.immediatus.graphics.utils

import scala.collection.mutable.ListBuffer

import javax.microedition.khronos.opengles.GL10

sealed trait PixelFormatEnum[T <: { def glFormat: Int; def glType: Int; def bitsPerPixel: Int }] {
  trait Value { self : T =>
    _values += this
  }

  private val _values = ListBuffer.empty[T]
  def values = _values
}

sealed abstract class PixelFormat(val glFormat: Int, val glType: Int, val bitsPerPixel: Int) extends PixelFormat.Value

object PixelFormat extends PixelFormatEnum[PixelFormat] {
  case object UNDEFINED extends PixelFormat(-1, -1, -1)
  case object RGBA_4444 extends PixelFormat(GL10.GL_RGBA, GL10.GL_UNSIGNED_SHORT_4_4_4_4, 16)
  case object RGBA_5551 extends PixelFormat(GL10.GL_RGBA, GL10.GL_UNSIGNED_SHORT_5_5_5_1, 16)
  case object RGBA_8888 extends PixelFormat(GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, 32)
  case object RGB_565 extends PixelFormat(GL10.GL_RGB, GL10.GL_UNSIGNED_SHORT_5_6_5, 16)
  case object A_8 extends PixelFormat(GL10.GL_ALPHA, GL10.GL_UNSIGNED_BYTE, 8)
  case object I_8 extends PixelFormat(GL10.GL_LUMINANCE, GL10.GL_UNSIGNED_BYTE, 8)
  case object AI_88 extends PixelFormat(GL10.GL_LUMINANCE_ALPHA, GL10.GL_UNSIGNED_BYTE, 16)
}
