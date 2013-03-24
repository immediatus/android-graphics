package com.immediatus.graphics.texture

import javax.microedition.khronos.opengles.GL10
import javax.microedition.khronos.opengles.GL11

import com.immediatus.graphics.utils.GL10Wrapper
import com.immediatus.graphics.utils.GL11Wrapper

abstract class BaseTextureRegion(
    private val _texture: Texture,
    private var _x: Int,
    private var _y: Int,
    private var _width: Int,
    private var _height: Int
  ){

  private val _textureRegionBuffer = new TextureRegionBuffer(this,GL11.GL_STATIC_DRAW, true)
  initTextureBuffer()

  protected def initTextureBuffer() {
    updateTextureRegionBuffer();
  }

  protected def deepCopy(): BaseTextureRegion

  def textureCoordinateX1: Float
  def textureCoordinateY1: Float
  def textureCoordinateX2: Float
  def textureCoordinateY2: Float

  def x = _x
  def y = _y
  def width = _width
  def width_=(width: Int): Unit = _width = width
  def height = _height
  def height_=(height:Int): Unit = _height = height
  def texture = _texture
  def textureRegionBuffer = _textureRegionBuffer
  def isFlippedHorizontal = _textureRegionBuffer.isFlippedHorizontal
  def isFlippedVertical =_textureRegionBuffer.isFlippedVertical
  def isTextureRegionBufferManaged = _textureRegionBuffer.isManaged
  def setFlippedHorizontal(flippedHorizontal: Boolean) = _textureRegionBuffer.setFlippedHorizontal(flippedHorizontal)
  def setFlippedVertical(flippedVertical: Boolean) = _textureRegionBuffer.setFlippedVertical(flippedVertical)
  def setTextureRegionBufferManaged(textureRegionBufferManaged: Boolean) = _textureRegionBuffer.setManaged(textureRegionBufferManaged)

  def setTexturePosition(x: Int, y: Int): Unit = {
    _x = x
    _y = y
    updateTextureRegionBuffer()
  }

  def onApply(gl:GL10): Unit = {
    _texture.bind(gl)
    if (GL10Wrapper(gl).EXTENSIONS_VERTEXBUFFEROBJECTS) {
      val gl11 = gl.asInstanceOf[GL11]
      _textureRegionBuffer.selectOnHardware(gl11)
      GL11Wrapper(gl11).texCoordZeroPointer
    } else {
      GL10Wrapper(gl).texCoordPointer(_textureRegionBuffer.floatBuffer.buffer)
    }
  }

  protected def updateTextureRegionBuffer(): Unit = _textureRegionBuffer.update()
}
