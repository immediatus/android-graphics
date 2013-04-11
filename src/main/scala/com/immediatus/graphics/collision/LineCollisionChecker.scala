package com.immediatus.graphics.collision

import com.immediatus.graphics.primitive.Line

object LineCollisionChecker extends BaseCollisionChecker {

   def checkLineCollision(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float, x4: Float, y4: Float): Boolean =
      ((relativeCCW(x1, y1, x2, y2, x3, y3) * relativeCCW(x1, y1, x2, y2, x4, y4) <= 0) &&
         (relativeCCW(x3, y3, x4, y4, x1, y1) * relativeCCW(x3, y3, x4, y4, x2, y2) <= 0))

   def fillVertices(line: Line): Iterator[Float] =
      line.getLocalToLayerTransformation().transform(0, 0, line.x2 - line.x1, line.y2 - line.y1)
}
