package com.immediatus.graphics.buffer

import javax.microedition.khronos.opengles.GL11

import com.immediatus.graphics.utils.GL11Wrapper
import com.immediatus.graphics.utils.FastFloatBuffer

abstract class BufferObject(
    capacity: Int,
    protected var _drawType: Int,
    protected var _managed: Boolean
  ) {

  private val HARDWAREBUFFERID_FETCHER = new Array[Int](1)

  protected val _bufferData: Array[Int] = new Array[Int](capacity)
  protected val _floatBuffer: FastFloatBuffer = new FastFloatBuffer(capacity)
  protected val _lock : AnyRef = new Object()

  private var _hardwareBufferID = -1
  private var _loadedToHardware = false
  private var _hardwareBufferNeedsUpdate = true

  def isManaged = _managed
  def floatBuffer = _floatBuffer
  def hardwareBufferID = _hardwareBufferID
  def isLoadedToHardware = _loadedToHardware
  def markToReload = _loadedToHardware = false
  def setHardwareBufferNeedsUpdate() = _hardwareBufferNeedsUpdate = true

  def selectOnHardware(gl: GL11) {
    if (_hardwareBufferID != -1) {

      GL11Wrapper(gl).bindBuffer(hardwareBufferID)

      if(_hardwareBufferNeedsUpdate) {
          _hardwareBufferNeedsUpdate = false;
          _lock.synchronized { GL11Wrapper(gl).bufferData(_floatBuffer.buffer, _drawType) }
      }
    }
  }

  def loadToHardware(gl: GL11){
    _hardwareBufferID = generateHardwareBufferID(gl)
    _loadedToHardware = true
  }

  def unloadFromHardware(gl: GL11){
    deleteBufferOnHardware(gl)
    _hardwareBufferID = -1
    _loadedToHardware = false
  }

  private def deleteBufferOnHardware(gl: GL11) {
    GL11Wrapper(gl).deleteBuffer(this._hardwareBufferID);
  }

  private def generateHardwareBufferID(gl: GL11): Int = {
    gl.glGenBuffers(1, HARDWAREBUFFERID_FETCHER, 0)
    HARDWAREBUFFERID_FETCHER(0)
  }
}
