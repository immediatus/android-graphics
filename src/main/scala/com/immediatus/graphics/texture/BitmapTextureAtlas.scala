package com.immediatus.graphics.texture

import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.opengl.GLUtils

import javax.microedition.khronos.opengles.GL10

import com.immediatus.graphics.utils.BitmapTextureFormat
import com.immediatus.graphics.utils.GL10Wrapper

class BitmapTextureAtlas(
    width: Int,
    height: Int,
    private val _bitmapTextureFormat: BitmapTextureFormat,
    textureOptions: TextureOptions
  ) extends TextureAtlas[IBitmapTextureAtlasSource] (width, height, _bitmapTextureFormat.pixelFormat, textureOptions){

    def this(width: Int, height: Int) = this(width, height, BitmapTextureFormat.RGBA_8888, TextureOptions.DEFAULT)
    def this(width: Int, height: Int, format: BitmapTextureFormat) = this(width, height, format, TextureOptions.DEFAULT)
    def this(width: Int, height: Int, options: TextureOptions) = this(width, height, BitmapTextureFormat.RGBA_8888, options)

    def bitmapTextureFormat = _bitmapTextureFormat

    protected override def writeTextureToHardware(gl: GL10) {
      val bitmapConfig = _bitmapTextureFormat.bitmapConfig
      val glFormat = pixelFormat.glFormat
      val glType = pixelFormat.glType
      val preMultipyAlpha = textureOptions.preMultiplyAlpha

      textureAtlasSources.foreach(source => {
        if(source != null) {
          val bitmap = source.onLoadBitmap(bitmapConfig)
          try {
            if(bitmap == null) throw new IllegalArgumentException(source.getClass.getSimpleName + ": " + source.toString + " returned a null Bitmap.")
            if (preMultipyAlpha) GLUtils.texSubImage2D(GL10.GL_TEXTURE_2D, 0, source.positionX, source.positionY, bitmap, glFormat, glType)
            else GL10Wrapper(gl).glTexSubImage2D(GL10.GL_TEXTURE_2D, 0, source.positionX, source.positionY, bitmap, pixelFormat)
          } catch {
            case ex: IllegalArgumentException => onTextureAtlasSourceLoadExeption(this, source, ex)
          }
        }
      })
    }

    protected override def bindTextureOnHardware(gl: GL10) {
        super.bindTextureOnHardware(gl)

        val glFormat = pixelFormat.glFormat
        val glType = pixelFormat.glType

        gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, glFormat, width, height, 0, glFormat, glType, null)
    }
}
