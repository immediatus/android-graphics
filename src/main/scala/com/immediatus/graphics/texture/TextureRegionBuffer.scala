package com.immediatus.graphics.texture

import com.immediatus.graphics.buffer.BufferObject

import com.immediatus.graphics.vertex.RectangleVertexBuffer.VERTICES_PER_RECTANGLE

class TextureRegionBuffer(
      protected val _textureRegion: BaseTextureRegion,
      drawType: Int,
      managed: Boolean
   ) extends BufferObject(2 * VERTICES_PER_RECTANGLE, drawType, managed) {

  import java.lang.Float.floatToRawIntBits

  private var _flippedVertical = false
  private var _flippedHorizontal = false

  def textureRegion = _textureRegion
  def isFlippedHorizontal = _flippedHorizontal
  def isFlippedVertical = _flippedVertical

  def setFlippedHorizontal(flippedHorizontal: Boolean) {
     if(_flippedHorizontal != flippedHorizontal) {
        _flippedHorizontal = flippedHorizontal
        update()
     }
  }

  def setFlippedVertical(flippedVertical: Boolean) {
     if(_flippedVertical != flippedVertical) {
        _flippedVertical = flippedVertical
        update()
     }
  }

  def update() = _lock.synchronized {
     val x1 = floatToRawIntBits(_textureRegion.textureCoordinateX1)
     val y1 = floatToRawIntBits(_textureRegion.textureCoordinateY1)
     val x2 = floatToRawIntBits(_textureRegion.textureCoordinateX2)
     val y2 = floatToRawIntBits(_textureRegion.textureCoordinateY2)

     if (_flippedVertical) {
        if (_flippedHorizontal) {
           _bufferData(0) = x2
           _bufferData(1) = y2
           _bufferData(2) = x2
           _bufferData(3) = y1
           _bufferData(4) = x1
           _bufferData(5) = y2
           _bufferData(6) = x1
           _bufferData(7) = y1
        } else {
           _bufferData(0) = x1
           _bufferData(1) = y2
           _bufferData(2) = x1
           _bufferData(3) = y1
           _bufferData(4) = x2
           _bufferData(5) = y2
           _bufferData(6) = x2
           _bufferData(7) = y1
         }
     } else {
        if(_flippedHorizontal) {
           _bufferData(0) = x2
           _bufferData(1) = y1
           _bufferData(2) = x2
           _bufferData(3) = y2
           _bufferData(4) = x1
           _bufferData(5) = y1
           _bufferData(6) = x1
           _bufferData(7) = y2
        } else {
           _bufferData(0) = x1
           _bufferData(1) = y1
           _bufferData(2) = x1
           _bufferData(3) = y2
           _bufferData(4) = x2
           _bufferData(5) = y1
           _bufferData(6) = x2
           _bufferData(7) = y2
         }
     }

     _floatBuffer.position = 0
     _floatBuffer.put(_bufferData)
     _floatBuffer.position = 0

     setHardwareBufferNeedsUpdate()
   }
}
