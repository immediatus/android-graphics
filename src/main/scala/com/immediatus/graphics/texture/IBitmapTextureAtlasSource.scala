package com.immediatus.graphics.texture

import android.graphics.Bitmap
import android.graphics.Bitmap.Config

trait IBitmapTextureAtlasSource extends ITextureAtlasSource {
  def onLoadBitmap(config: Config): Bitmap
  def copy(): IBitmapTextureAtlasSource
}
