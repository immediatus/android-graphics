package com.immediatus.graphics.collision

class BaseCollisionChecker {

   def checkAxisAlignedRectangleCollision(
      leftA: Float, topA: Float, rightA: Float, bottomA: Float,
      leftB: Float, topB: Float, rightB: Float, bottomB: Float) =
         leftA < rightB && leftB < rightA && topA < bottomB && topB < bottomA

   def relativeCCW(rx1: Float, ry1: Float, rx2: Float, ry2: Float, rpX: Float, rpY: Float): Int = {
      import com.immediatus.utils.ConditionalApplicative._

      val x1 = rx1
      val y1 = ry1
      val x2 = rx2 - rx1
      val y2 = ry2 - ry1
      val pX = rpX - rx1
      val pY = rpY - ry1

      (pX * y2 - pY * x2).
         $if(_ == 0f) { _ => pX * x2 + pY * y2 }.
         $if(_  > 0f) { _ => (pX - x2) * x2 + (pY - y2) * y2 }.
         $if(_  < 0f) { _ => -1f }.
         $else { _.$if(_ > 0f) { _ => 1f }. $else { _ => 0f } }.
         toInt
    }
}
