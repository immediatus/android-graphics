package com.immediatus.graphics.texture

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.BitmapFactory


import java.io.IOException
import java.io.InputStream

class AssetBitmapTextureAtlasSource(
    private val _context: Context,
    private val _assetPath: String
  ) extends IBitmapTextureAtlasSource {

  private var _x = 0
  private var _y = 0

  private var _width = 0
  private var _height = 0

  private def loadWidthAndHeightFromResources() {
    val decodeOptions = new BitmapFactory.Options
    decodeOptions.inJustDecodeBounds = true
    var in: InputStream = null
    try {
      in = _context.getAssets.open(_assetPath)
      BitmapFactory.decodeStream(in, null, decodeOptions)
    } catch { case e: IOException => e.printStackTrace()
    } finally {
      try { if(in != null) in.close() }
      catch { case e: IOException => e.printStackTrace() }
    }

    _width = decodeOptions.outWidth
    _height = decodeOptions.outHeight
  }

  private def this(ctx: Context, assetPath:String, x: Int, y: Int, w: Int, h: Int) {
    this(ctx, assetPath)
    _x = x
    _y = y
    _width = w
    _height = h
  }

  def this(ctx: Context, assetPath: String, x: Int, y: Int) {
    this(ctx, assetPath)
    _x = x
    _y = y

    loadWidthAndHeightFromResources()
  }

  override def copy() = new AssetBitmapTextureAtlasSource(_context, _assetPath, _x, _y, _width, _height)

  override def positionX = _x

  override def positionY = _y

  override def positionX_=(x: Int): Unit = _x = x

  override def positionY_=(y: Int): Unit = _y = y

  override def width = _width

  override def height = _height

  override def onLoadBitmap(config: Config): Bitmap = {
    var in: InputStream = null
    try{
      val decodeOptions = new BitmapFactory.Options
      decodeOptions.inPreferredConfig = config
      in = _context.getAssets().open(_assetPath)
      BitmapFactory.decodeStream(in, null, decodeOptions)
    }
    catch { case e: IOException => null }
    finally {
      try { if(in != null) in.close() }
      catch { case e: IOException => e.printStackTrace() }
    }
  }
}
