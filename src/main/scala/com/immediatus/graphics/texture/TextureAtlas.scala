package com.immediatus.graphics.texture

import scala.collection.mutable.ListBuffer

import com.immediatus.graphics.utils.PixelFormat

abstract class TextureAtlas[T <: ITextureAtlasSource](
      private val _width: Int,
      private val _height: Int,
      pixelFormat: PixelFormat,
      textureOptions: TextureOptions) extends Texture(pixelFormat, textureOptions) {

  private var _onTextureAtlasSourceLoadExeption: (TextureAtlas[T], T, Throwable) => Unit = null

  private var _textureAtlasSources = ListBuffer[T]()

  protected def onTextureAtlasSourceLoadExeption(textureAtlas: TextureAtlas[T], textureAtlasSource: T, ex: Throwable) =
    if(_onTextureAtlasSourceLoadExeption != null) _onTextureAtlasSourceLoadExeption(textureAtlas, textureAtlasSource, ex)
    else throw ex

  def width = _width

  def height = _height

  def textureAtlasSources = _textureAtlasSources.toList

  def addTextureAtlasSource(textureAtlasSource: T, positionX: Int, positionY: Int): this.type =  {
    checkTextureAtlasSourcePosition(textureAtlasSource, positionX, positionY)

    textureAtlasSource.positionX = positionX
    textureAtlasSource.positionY = positionY

    _textureAtlasSources += textureAtlasSource

    _updateOnHardwareNeeded = true;

    this
  }

  def removeTextureAtlasSource(textureAtlasSource: T, positionX: Int, positionY: Int): this.type =  {
    _textureAtlasSources.remove(
      _textureAtlasSources.indexWhere(
        s => s == textureAtlasSource && s.positionX == positionX && s.positionY == positionY))

    _updateOnHardwareNeeded = true;

    this
  }

  def clearTextureAtlasSources(): this.type = {
    _textureAtlasSources.clear()

    _updateOnHardwareNeeded = true

    this
  }

  def onTextureAtlasSourceLoadExeption(f: (TextureAtlas[T], T, Throwable) => Unit): this.type = {
    _onTextureAtlasSourceLoadExeption = f

    this
  }

  private def checkTextureAtlasSourcePosition(textureAtlasSource: T, positionX: Int, positionY: Int) {
    if (positionX < 0) throw new IllegalArgumentException(s"Illegal negative texturePositionX supplied: '$positionX'")
    else if (positionY < 0) throw new IllegalArgumentException(s"Illegal negative texturePositionY supplied: '$positionY'")
    else if (positionX + textureAtlasSource.width > width || positionY + textureAtlasSource.height > height)
      throw new IllegalArgumentException("Supplied TextureAtlasSource must not exceed bounds of Texture.")
  }
}
