 package com.immediatus.graphics.texture

import javax.microedition.khronos.opengles.GL10

import com.immediatus.graphics.utils.GL10Wrapper
import com.immediatus.graphics.utils.PixelFormat


abstract class Texture(
    private var _pixelFormat: PixelFormat,
    private var _textureOptions: TextureOptions) {

  private val HARDWARETEXTUREID_FETCHER = new Array[Int](1)

  private var _onLoadedToHardware: Texture => Unit = null
  private var _onUnloadedFromHardware: Texture => Unit = null

  protected var _hardwareTextureID = -1
  protected var _loadedToHardware = false
  protected var _updateOnHardwareNeeded = false

  protected def writeTextureToHardware(gl: GL10)

  protected def applyTextureOptions(gl: GL10) {
    _textureOptions(gl)
  }

  protected def bindTextureOnHardware(gl: GL10){
    GL10Wrapper(gl).forceBindTexture(_hardwareTextureID)
  }

  protected def deleteTextureOnHardware(gl: GL10){
    GL10Wrapper(gl).deleteTexture(_hardwareTextureID);
  }

  protected def generateHardwareTextureID(gl: GL10){
    gl.glGenTextures(1, HARDWARETEXTUREID_FETCHER, 0);
    _hardwareTextureID = HARDWARETEXTUREID_FETCHER(0);
  }

  protected def onLoadedToHardware(texture: Texture) = if(_onLoadedToHardware != null) _onLoadedToHardware(texture)

  protected def onUnloadedFromHardware(texture: Texture) = if(_onUnloadedFromHardware != null) _onUnloadedFromHardware(texture)

  def hardwareTextureID = _hardwareTextureID
  def isLoadedToHardware = _loadedToHardware
  def isUpdateOnHardwareNeeded = _updateOnHardwareNeeded

  def markToReload = _updateOnHardwareNeeded = true

  def pixelFormat = _pixelFormat
  def textureOptions = _textureOptions

  def loadToHardware(gl: GL10): this.type = {
    GL10Wrapper(gl).enableTextures

    generateHardwareTextureID(gl)
    bindTextureOnHardware(gl)
    applyTextureOptions(gl)
    writeTextureToHardware(gl)

    _updateOnHardwareNeeded = false
    _loadedToHardware = true

    onLoadedToHardware(this)

    this
  }

  def unloadFromHardware(gl: GL10): this.type = {
    GL10Wrapper(gl).enableTextures

    deleteTextureOnHardware(gl)

    _hardwareTextureID = -1
    _loadedToHardware = false

    onUnloadedFromHardware(this)

    this
  }

  def reloadToHardware(gl: GL10): this.type = {
    unloadFromHardware(gl)
    loadToHardware(gl)

    this
  }

  def bind(gl: GL10): this.type = {
    GL10Wrapper(gl).bindTexture(_hardwareTextureID)

    this
  }

  def onLoadedToHardware(f: Texture => Unit): this.type = {
    _onLoadedToHardware = f

    this
  }

  def onUnloadedFromHardware(f: Texture => Unit): this.type = {
    _onUnloadedFromHardware = f

    this
  }
}
