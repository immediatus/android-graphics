package com.immediatus.graphics.shape

import javax.microedition.khronos.opengles.GL10
import javax.microedition.khronos.opengles.GL11

import com.immediatus.graphics.GraphicsUnit
import com.immediatus.graphics.input.TouchEvent
import com.immediatus.graphics.camera.Camera
import com.immediatus.graphics.utils.{GL10Wrapper, Transformation}
import com.immediatus.graphics.vertex.VertexBuffer
import com.immediatus.utils.UnitComposit
import com.immediatus.utils.ConditionalApplicative._


abstract class Shape(
      shapeX: Float,
      shapeY: Float
   ) extends GraphicsUnit with UnitComposit[Shape] {

   final val BLENDFUNCTION_SOURCE_DEFAULT = GL10.GL_SRC_ALPHA
   final val BLENDFUNCTION_DESTINATION_DEFAULT = GL10.GL_ONE_MINUS_SRC_ALPHA
   final val BLENDFUNCTION_SOURCE_PREMULTIPLYALPHA_DEFAULT = GL10.GL_ONE
   final val BLENDFUNCTION_DESTINATION_PREMULTIPLYALPHA_DEFAULT = GL10.GL_ONE_MINUS_SRC_ALPHA

   protected var SourceBlendFunction = BLENDFUNCTION_SOURCE_DEFAULT
   protected var DestinationBlendFunction = BLENDFUNCTION_DESTINATION_DEFAULT

   private var _cullingEnabled = false

   setPosition(shapeX, shapeY)

   def isVertexBufferManaged = vertexBuffer.isManaged
   def setVertexBufferManaged(vertexBufferManaged: Boolean): Unit = vertexBuffer.setManaged(vertexBufferManaged)

   def setBlendFunction(sourceBlendFunction: Int, destinationBlendFunction: Int): Unit ={
      SourceBlendFunction = sourceBlendFunction
      DestinationBlendFunction = destinationBlendFunction
   }

   def width: Float
   def height: Float

   def widthScaled = width * scaleX
   def heightScaled = height * scaleY

   def isCullingEnabled = _cullingEnabled
   def setCullingEnabled(cullingEnabled: Boolean): Unit = _cullingEnabled = cullingEnabled

   override def doDraw(gl: GL10, camera: Camera) {
      onInitDraw(gl)
      onApplyVertices(gl)
      drawVertices(gl, camera)
   }

   override def getLocalToLayerTransformation(): Transformation = {
      _localToLayerTransformation = getLocalToParentTransformation().
         $if(hasParent) {
            _.postConcat(parent.self.getLocalToLayerTransformation())
         }

      _localToLayerTransformation
   }

   override def getLayerToLocalTransformation(): Transformation = {
      _layerToLocalTransformation = getParentToLocalTransformation().
         $if(hasParent) {
            _.postConcat(parent.self.getLayerToLocalTransformation())
         }

      _layerToLocalTransformation
   }

   protected def onUpdateVertexBuffer(): Unit
   protected def vertexBuffer: VertexBuffer
   protected def drawVertices(gl: GL10, camera: Camera)
   protected def isCulled(camera: Camera): Boolean

   protected def onAreaTouched(event: TouchEvent, x: Float, y: Float) = false

   protected override def onManagedDraw(gl: GL10, camera: Camera) {
      if (!_cullingEnabled || !isCulled(camera)) onManagedDraw(gl, camera)
   }

   protected override def finalize() {
      super.finalize()
      if (vertexBuffer.isManaged) vertexBuffer.unloadFromActiveBufferObjectManager()
   }

   protected def onInitDraw(gl: GL10) {
      GL10Wrapper(gl).
         setColor(red, green, blue, alpha).
         enableVertexArray.
         blendFunction(SourceBlendFunction, DestinationBlendFunction)
   }

   protected def onApplyVertices(gl: GL10) {
      import com.immediatus.graphics.utils.GL11Wrapper._

      GL10Wrapper(gl).
         $if(_.EXTENSIONS_VERTEXBUFFEROBJECTS) {
            vertexBuffer.selectOnHardware(gl.asInstanceOf[GL11])
            _.vertexZeroPointer
         }.$else {
            _.vertexPointer(vertexBuffer.floatBuffer.buffer)
         }
    }

   protected def updateVertexBuffer() = onUpdateVertexBuffer()
}
