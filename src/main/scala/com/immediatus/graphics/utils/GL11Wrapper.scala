package com.immediatus.graphics.utils

import javax.microedition.khronos.opengles.GL11
import javax.microedition.khronos.opengles.GL10

import java.nio.ByteBuffer

import scala.collection.mutable.HashMap
import scala.collection.mutable.SynchronizedMap


object GL11Wrapper {

  private var _glWrappers = new HashMap[GL11,GL11Wrapper] with SynchronizedMap[GL11,GL11Wrapper]

  def apply(gl: GL11) = {
    _glWrappers.getOrElseUpdate(gl, new GL11Wrapper(gl))
  }
}


class GL11Wrapper(gl: GL11) {

  private val HARDWAREBUFFERID_CONTAINER = new Array[Int](1)
  private var _currentHardwareBufferID = -1

  def bindBuffer(hardwareBufferID: Int): this.type = {
    if (_currentHardwareBufferID != hardwareBufferID) {
      _currentHardwareBufferID = hardwareBufferID
      gl.glBindBuffer(GL11.GL_ARRAY_BUFFER, hardwareBufferID)
    }

    this
  }

  def deleteBuffer(hardwareBufferID: Int): this.type = {
    HARDWAREBUFFERID_CONTAINER(0) = hardwareBufferID
    gl.glDeleteBuffers(1, HARDWAREBUFFERID_CONTAINER, 0);

    this
  }

  def texCoordZeroPointer(): this.type = {
    gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, 0)

    this
  }

  def vertexZeroPointer(): this.type = {
    gl.glVertexPointer(2, GL10.GL_FLOAT, 0, 0)

    this
  }

  def bufferData(byteBuffer: ByteBuffer, usage: Int): this.type = {
    gl.glBufferData(GL11.GL_ARRAY_BUFFER, byteBuffer.capacity, byteBuffer, usage)

    this
  }
}
