package com.immediatus.graphics.primitive

import com.immediatus.graphics.shape.Shape
import com.immediatus.graphics.collision.LineCollisionChecker
import com.immediatus.graphics.camera.Camera
import com.immediatus.graphics.utils.GL10Wrapper
import com.immediatus.graphics.vertex.LineVertexBuffer

import javax.microedition.khronos.opengles.GL10
import javax.microedition.khronos.opengles.GL11

object Line {
   private final val LINEWIDTH_DEFAULT = 1.0f
}

class Line(
      xi: Float,
      yi: Float,
      protected var _x2: Float,
      protected var _y2: Float,
      private var _lineWidth: Float
   ) extends Shape(xi, yi) {

   private val _lineVertexBuffer: LineVertexBuffer = new LineVertexBuffer(GL11.GL_STATIC_DRAW, true)

   updateVertexBuffer()
   setRotationCenter(width * .5f, height * .5f)
   setScaleCenter(width * .5f, height * .5f)

   def this(x1: Float, y1: Float, x2: Float, y2: Float) = this(x1, y1, x2, y2, Line.LINEWIDTH_DEFAULT)

   def vertexBuffer: LineVertexBuffer = _lineVertexBuffer


   def x1: Float = super.x
   def y1: Float = super.y
   def x2: Float = _x2
   def y2: Float = _y2
   def lineWidth: Float = _lineWidth
   def lineWidth_=(w: Float): Unit = _lineWidth = w

   override def height: Float = _y2 - y
   override def width: Float = _x2 - x

   override def setPosition(xi: Float, yi: Float) {
      super.setPosition(xi, yi)

      _x2 = _x2 + (x - xi)
      _y2 = _y2 + (y - yi)
   }

   def setPosition(x1i: Float, y1i: Float, x2i: Float, y2i: Float) {
      _x2 = x2i
      _y2 = y2i

     super.setPosition(x1i, y1i)
     updateVertexBuffer()
   }

   protected override def isCulled(camera: Camera) = true //camera.isLineVisible(this)

   protected override def onInitDraw(gl: GL10) {
      super.onInitDraw(gl)
      GL10Wrapper(gl).
        disableTextures.
        disableTexCoordArray.
        lineWidth(_lineWidth)
   }

   protected override def onUpdateVertexBuffer() {
      _lineVertexBuffer.update(0, 0, x2 - x1, y2 - y1)
   }

   protected override def drawVertices(gl: GL10, camera: Camera) {
      gl.glDrawArrays(GL10.GL_LINES, 0, LineVertexBuffer.VERTICES_PER_LINE)
   }

   override def getLayerCenterCoordinates(): Iterator[Float] = null

   override def convertLayerToLocalCoordinates(x: Float, y: Float): Iterator[Float] = null

   override def convertLocalToLayerCoordinates(x: Float, y: Float): Iterator[Float] = null
}
