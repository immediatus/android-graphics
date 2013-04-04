package com.immediatus.graphics.camera

class BoundCamera(x: Float, y: Float, w: Float, h: Float) extends Camera(x, y, w, h) {
   private var _isBoundsEnabled = false
   private var _boundsMinX = 0f
   private var _boundsMaxX = 0f
   private var _boundsMinY = 0f
   private var _boundsMaxY = 0f
   private var _boundsCenterX = 0f
   private var _boundsCenterY = 0f
   private var _boundsWidth = 0f
   private var _boundsHeight = 0f

   def this (x: Float, y: Float, w: Float, h: Float, boundMinX: Float, boundMaxX: Float, boundMinY: Float, boundMaxY: Float) {
     this(x, y, w, h)
     setBounds(boundMinX, boundMaxX, boundMinY, boundMaxY)
     _isBoundsEnabled = true
   }

   def isBoundsEnabled = _isBoundsEnabled
   def boundsWidth =  _boundsWidth
   def boundsHeight = _boundsHeight

   def setBoundsEnabled(boundsEnabled: Boolean) = _isBoundsEnabled = boundsEnabled

   def setBounds(minX: Float, maxX: Float, minY: Float, maxY: Float) {
     _boundsMinX = minX
     _boundsMaxX = maxX
     _boundsMinY = minY
     _boundsMaxY = maxY

     _boundsWidth = _boundsMaxX - _boundsMinX
     _boundsHeight = _boundsMaxY - _boundsMinY

     _boundsCenterX = _boundsMinX + _boundsWidth * 0.5f
     _boundsCenterY = _boundsMinY + _boundsHeight * 0.5f
   }

   override def setCenter(x: Float, y: Float) {
     super.setCenter(x, y)
     if (_isBoundsEnabled) ensureInBounds()
   }

   protected def ensureInBounds() {
      super.setCenter(determineBoundedX, determineBoundedY)
   }

   private def determineBoundedX = {
      if (_boundsWidth < width) _boundsCenterX
      else {
          val currentCenterX = centerX
          val minXBoundExceededAmount = _boundsMinX - minX
          val minXBoundExceeded = minXBoundExceededAmount > 0
          val maxXBoundExceededAmount = maxX - _boundsMaxX
          val maxXBoundExceeded = maxXBoundExceededAmount > 0

          (minXBoundExceeded, maxXBoundExceeded) match {
             case (true, true)   => currentCenterX - maxXBoundExceededAmount + minXBoundExceededAmount
             case (true, false)  => currentCenterX + minXBoundExceededAmount
             case (false, true)  => currentCenterX - maxXBoundExceededAmount
             case (false, false) => currentCenterX
          }
      }
   }

   private def determineBoundedY = {
      if (_boundsHeight < height) _boundsCenterY
      else {
         val currentCenterY = centerY
         val minYBoundExceededAmount = _boundsMinY - minY
         val minYBoundExceeded = minYBoundExceededAmount > 0
         val maxYBoundExceededAmount = maxY - _boundsMaxY
         val maxYBoundExceeded = maxYBoundExceededAmount > 0

         (minYBoundExceeded, maxYBoundExceeded)  match {
             case (true, true)   => currentCenterY - maxYBoundExceededAmount + minYBoundExceededAmount
             case (true, false)  => currentCenterY + minYBoundExceededAmount
             case (false, true)  => currentCenterY - maxYBoundExceededAmount
             case (false, false) => currentCenterY
          }
      }
   }
}
