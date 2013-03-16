package com.immediatus.graphics.utils

import scala.collection.mutable.ListBuffer

import android.graphics.Bitmap

sealed trait BitmapTextureFormatEnum[T <: { def bitmapConfig: Bitmap.Config; def pixelFormat: PixelFormat }] {
  trait Value { self : T =>
    _values += this
  }

  private val _values = ListBuffer.empty[T]
  def values = _values
}

sealed abstract class BitmapTextureFormat(val bitmapConfig: Bitmap.Config, val pixelFormat: PixelFormat) extends BitmapTextureFormat.Value

object BitmapTextureFormat extends BitmapTextureFormatEnum[BitmapTextureFormat]  {
  def fromPixelFormat(pixelFormat: PixelFormat) = pixelFormat match {
    case PixelFormat.RGBA_8888 => BitmapTextureFormat.RGBA_8888
    case PixelFormat.RGBA_4444 => BitmapTextureFormat.RGBA_4444
    case PixelFormat.RGB_565 => BitmapTextureFormat.RGB_565
    case PixelFormat.A_8 => BitmapTextureFormat.A_8
    case _ => throw new IllegalArgumentException("Unsupported PixelFormat.")
  }

  case object RGBA_8888 extends BitmapTextureFormat(Bitmap.Config.ARGB_8888, PixelFormat.RGBA_8888)
  case object RGB_565 extends BitmapTextureFormat(Bitmap.Config.RGB_565, PixelFormat.RGB_565)
  case object RGBA_4444 extends BitmapTextureFormat(Bitmap.Config.ARGB_4444, PixelFormat.RGBA_4444)
  case object A_8 extends BitmapTextureFormat(Bitmap.Config.ALPHA_8, PixelFormat.A_8)
}
