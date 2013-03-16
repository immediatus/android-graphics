package com.immediatus.graphics.vertex

import com.immediatus.graphics.utils.FastFloatBuffer

class RectangleVertexBuffer(drawType: Int, managed: Boolean) extends VertexBuffer(2 * 4 /*VERTICES_PER_RECTANGLE*/, drawType, managed) {
  import java.lang.Float.floatToRawIntBits

  val VERTICES_PER_RECTANGLE = 4
  private val FLOAT_TO_RAW_INT_BITS_ZERO = floatToRawIntBits(0)

  def update(width: Float, height: Float) = _lock.synchronized {
    val x = FLOAT_TO_RAW_INT_BITS_ZERO
    val y = FLOAT_TO_RAW_INT_BITS_ZERO
    val x2 = floatToRawIntBits(width)
    val y2 = floatToRawIntBits(height)

    _bufferData(0) = x
    _bufferData(1) = y
    _bufferData(2) = x
    _bufferData(3) = y2
    _bufferData(4) = x2
    _bufferData(5) = y;
    _bufferData(6) = x2
    _bufferData(7) = y2

    floatBuffer.position = 0
    floatBuffer.put(_bufferData)
    floatBuffer.position = 0

    super.setHardwareBufferNeedsUpdate()
  }
}
