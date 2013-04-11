package com.immediatus.graphics.input

import android.view.MotionEvent

import com.immediatus.graphics.utils.Point

case class TouchEvent(
      x: Float,
      y: Float,
      action:Int,
      pointerID: Int,
      motionEvent: MotionEvent
   ) {

   final val ACTION_CANCEL    = MotionEvent.ACTION_CANCEL
   final val ACTION_DOWN      = MotionEvent.ACTION_DOWN
   final val ACTION_MOVE      = MotionEvent.ACTION_MOVE
   final val ACTION_OUTSIDE   = MotionEvent.ACTION_OUTSIDE
   final val ACTION_UP        = MotionEvent.ACTION_UP

   def position = Point(x, y)

   def isActionDown =      action == ACTION_DOWN
   def isActionUp =        action == ACTION_UP
   def isActionMove =      action == ACTION_MOVE
   def isActionCancel =    action == ACTION_CANCEL
   def isActionOutside =   action == ACTION_OUTSIDE
}
