package com.immediatus.graphics.utils

case class Vector2(x: Float, y: Float) {
  private val PI = 3.1415927F
  private val RADIANS_TO_DEGREES = 180F / PI
  private val DEGREES_TO_RADIANS = PI / 180

  def len() = Math.sqrt(len2).toFloat

  def len2() = x * x + y * y;

  def cpy()  = Vector2(x, y)

  def sub(v:Vector2 ) = Vector2(x - v.x, y - v.y)

  def nor() = {
    val l = len
    if (l != 0) Vector2(x / l, y / l) else cpy()
  }

  def add(v: Vector2): Vector2 = add(v.x, v.y)

  def add(x1: Float, y1: Float) = Vector2(x + x1, y + y1)

  def dot(v: Vector2) = x * v.x + y * v.y

  def mul(scalar: Float) = Vector2(x * scalar, y * scalar)

  def dst(v: Vector2) = Math.sqrt(dst2(v))

  def dst(x1: Float, y1: Float) = {
    val dx = x1 - x
    val dy = y1 - y
    Math.sqrt(dx * dx + dy * dy)
  }

  def dst2(v: Vector2) = {
    val dx = v.x - x
    val dy = v.y - y
    dx * dx + dy * dy
  }

  def sub(x1: Float, y1: Float) = Vector2(x - x1, y - y1)

  def mul(m: Float*) = Vector2(x * m(0) + y * m(3) + m(6), x * m(1) + y * m(4) + m(7))

  def crs(v: Vector2): Float = crs(v.x, v.y)

  def crs(x1: Float, y1: Float) = x * y1 - y * x1

  def angle() = {
    val angle = Math.atan2(y, x) * RADIANS_TO_DEGREES;
    if (angle < 0) angle + 360 else angle
  }

  def rotate (angle: Float) = {
    val rad = angle * DEGREES_TO_RADIANS
    val cos = Math.cos(rad).toFloat
    val sin = Math.sin(rad).toFloat
    Vector2(x * cos - y * sin, x * sin + y * cos)
  }

  def lerp (target: Vector2, alpha: Float) = mul(1.0f - alpha).add(target.mul(alpha))
}

