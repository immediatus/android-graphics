package com.immediatus.graphics.vertex

import com.immediatus.utils.FastFloatBuffer

object LineVertexBuffer {
   final val VERTICES_PER_LINE = 2
}

class LineVertexBuffer(drawType: Int, managed: Boolean) extends VertexBuffer(2 * LineVertexBuffer.VERTICES_PER_LINE, drawType, managed) {
   import java.lang.Float.floatToRawIntBits

   def update(x1: Float, y1: Float, x2: Float, y2: Float) = _lock.synchronized {
      _bufferData(0) = floatToRawIntBits(x1)
      _bufferData(1) = floatToRawIntBits(y1)
      _bufferData(2) = floatToRawIntBits(x2)
      _bufferData(3) = floatToRawIntBits(y2)

      floatBuffer.position = 0
      floatBuffer.put(_bufferData)
      floatBuffer.position = 0

      super.setHardwareBufferNeedsUpdate()
    }
}
