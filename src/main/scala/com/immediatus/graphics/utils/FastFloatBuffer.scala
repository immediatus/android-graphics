package com.immediatus.graphics.utils

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer

class FastFloatBuffer(c: Int) {
  import java.lang.Float.floatToRawIntBits

  val BYTES_PER_FLOAT = 4

  private val _buffer = ByteBuffer.allocateDirect(c * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder)
  private val _floatBuffer = _buffer.asFloatBuffer
  private val _intBuffer = _buffer.asIntBuffer

  def flip(): this.type = {
    _buffer.flip()
    _floatBuffer.flip()
    _intBuffer.flip()

    this
  }

  def put(item: Float): this.type = {
    _buffer.position(_buffer.position + BYTES_PER_FLOAT)
    _floatBuffer.put(item)
    _intBuffer.position(_intBuffer.position + 1)

    this
  }

  def put(data: Array[Float]): this.type = {
    val length = data.length
    _buffer.position(_buffer.position + BYTES_PER_FLOAT * length)
    _floatBuffer.position(_floatBuffer.position + length)
    _intBuffer.put(data.map(floatToRawIntBits), 0, length)

    this
  }

  def put(data: Array[Int]): this.type = {
    val length = data.length
    _buffer.position(_buffer.position + BYTES_PER_FLOAT * length)
    _floatBuffer.position(_floatBuffer.position + length)
    _intBuffer.put(data, 0, length)

    this
  }

  def put(b: FastFloatBuffer): this.type = {
    _buffer.put(b._buffer)
    _floatBuffer.position(_buffer.position >> 2)
    _intBuffer.position(_buffer.position >> 2)

    this
  }

  def buffer = _buffer

  def capacity = _floatBuffer.capacity

  def position = _floatBuffer.position

  def position_= (p: Int):Unit = {
     _buffer.position(p * BYTES_PER_FLOAT)
     _floatBuffer.position(p)
     _intBuffer.position(p)
  }

  def slice() = _floatBuffer.slice

  def remaining() = _floatBuffer.remaining

  def limit() = _floatBuffer.limit

  def clear(): this.type = {
    _buffer.clear()
    _floatBuffer.clear()
    _intBuffer.clear()

    this
  }

  def convert(data: Float*): Array[Int] = data.map(floatToRawIntBits).toArray[Int]
}
