package com.immediatus.graphics.utils

import android.util.FloatMath

object Transformation {
  def apply() = new Transformation()
  def apply(a: Float, b: Float, c: Float, d: Float, tx: Float, ty: Float) = new Transformation(a, b, c, d, tx, ty)
}

class Transformation(
    val a: Float = 1.0f,
    val b: Float = 0.0f,
    val c: Float = 0.0f,
    val d: Float = 1.0f,
    val tx: Float = 0.0f,
    val ty: Float = 0.0f
  ) {

  private final val DEG_TO_RAD = Math.PI.toFloat / 180.0f
  private def degToRad(degree: Float) =  DEG_TO_RAD * degree

  def preTranslate(x: Float, y: Float): Transformation = Transformation(a, b, c, d, tx + x * a + y * c, ty + x * b + y * d)

  def postTranslate(x: Float, y: Float): Transformation = Transformation(a, b, c, d, tx + x, ty + y)

  def setToTranslate(x: Float, y: Float): Transformation = Transformation(a, b, c, d, x, y)

  def preScale(sx: Float, sy: Float): Transformation = Transformation(a * sx, b * sx, c * sy, d * sy, tx, ty)

  def postScale(sx: Float, sy: Float): Transformation = Transformation(a * sx, b * sy, c * sx, d * sy, tx * sx, ty * sy)

  def setToScale(sx: Float, sy: Float): Transformation = Transformation(sx, 0.0f, 0.0f, sy, 0.0f, 0.0f)

  def preRotate(a: Float): Transformation = {
    val angleRad = degToRad(a)
    val sin = FloatMath.sin(angleRad)
    val cos = FloatMath.cos(angleRad)

    Transformation(cos * a + sin * c, cos * b + sin * d, cos * c - sin * a, cos * d - sin * b, tx, ty)
  }

  def postRotate(a: Float): Transformation = {
    val angleRad = degToRad(a)
    val sin = FloatMath.sin(angleRad)
    val cos = FloatMath.cos(angleRad)

    Transformation(a * cos - b * sin, a * sin + b * cos, c * cos - d * sin, c * sin + d * cos, tx * cos - ty * sin, tx * sin + ty * cos)
  }

  def setToRotate(a: Float): Transformation = {
    val angleRad = degToRad(a)
    val sin = FloatMath.sin(angleRad)
    val cos = FloatMath.cos(angleRad)

    Transformation(cos, sin, -sin, cos, 0.0f, 0.0f)
  }

  def postConcat(t: Transformation): Transformation = postConcat(t.a, t.b, t.c, t.d, t.tx, t.ty)

  def preConcat(t: Transformation ): Transformation = preConcat(t.a, t.b, t.c, t.d, t.tx, t.ty)

  def transform(vertices: Float *): Iterator[Float] = vertices.grouped(2).flatMap{ case (x :: y :: Nil) => (x * a + y * c + tx) :: (x * b + y * d + ty) :: Nil }

  private def preConcat(a1: Float, b1: Float, c1: Float, d1: Float, tx1: Float, ty1: Float) =
    Transformation(a1 * a + b1 * c, a1 * b + b1 * d, c1 * a + d1 * c, c1 * b + d1 * d, tx1 * a + ty1 * c + tx, tx1 * b + ty1 * d + ty)

  private def postConcat(a1: Float, b1: Float, c1: Float, d1: Float, tx1: Float, ty1: Float) =
    Transformation(a * a1 + b * c1, a * b1 + b * d1, c * a1 + d * c1, c * b1 + d * d1, tx * a1 + ty * c1 + tx1, tx * b1 + ty * d1 + ty1)
}
