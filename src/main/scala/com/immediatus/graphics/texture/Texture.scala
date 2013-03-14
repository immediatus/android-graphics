 package com.immediatus.graphics.texture

import javax.microedition.khronos.opengles.GL10

import com.immediatus.graphics.utils.GL10Wrapper
import com.immediatus.graphics.utils.PixelFormat


abstract class Texture(
    protected var PixelFormat: PixelFormat,
    protected var TextureOptions: TextureOptions) {

  private val HARDWARETEXTUREID_FETCHER = new Array[Int](1)
  private var onLoadedToHardware: Texture => Unit = null
  private var onUnloadedFromHardware: Texture => Unit = null

  protected var HardwareTextureID = -1
  protected var LoadedToHardware = false
  protected var UpdateOnHardwareNeeded = false

  protected def writeTextureToHardware(gl: GL10)

  protected def applyTextureOptions(gl: GL10) {
    TextureOptions(gl)
  }

  protected def bindTextureOnHardware(gl: GL10){
    GL10Wrapper(gl).forceBindTexture(HardwareTextureID)
  }

  protected def deleteTextureOnHardware(gl: GL10){
    GL10Wrapper(gl).deleteTexture(HardwareTextureID);
  }

  protected def generateHardwareTextureID(gl: GL10){
    gl.glGenTextures(1, HARDWARETEXTUREID_FETCHER, 0);
    HardwareTextureID = HARDWARETEXTUREID_FETCHER(0);
  }

  def hardwareTextureID = HardwareTextureID
  def isLoadedToHardware = LoadedToHardware
  def isUpdateOnHardwareNeeded =UpdateOnHardwareNeeded

  def pixelFormat = PixelFormat
  def textureOptions = TextureOptions

  def loadToHardware(gl: GL10): this.type = {
    GL10Wrapper(gl).enableTextures

    generateHardwareTextureID(gl)
    bindTextureOnHardware(gl)
    applyTextureOptions(gl)
    writeTextureToHardware(gl)

    UpdateOnHardwareNeeded = false
    LoadedToHardware = true

    if (onLoadedToHardware != null) onLoadedToHardware(this)

    this
  }

  def unloadFromHardware(gl: GL10): this.type = {
    GL10Wrapper(gl).enableTextures

    deleteTextureOnHardware(gl)

    HardwareTextureID = -1
    LoadedToHardware = false

    if (onUnloadedFromHardware != null) onUnloadedFromHardware(this)

    this
  }

  def reloadToHardware(gl: GL10): this.type = {
    unloadFromHardware(gl)
    loadToHardware(gl)

    this
  }

  def bind(gl: GL10): this.type = {
    GL10Wrapper(gl).bindTexture(HardwareTextureID)

    this
  }

  def onLoadedToHardware(f: Texture => Unit): this.type = {
    onLoadedToHardware = f

    this
  }

  def onUnloadedFromHardware(f: Texture => Unit): this.type = {
    onUnloadedFromHardware = f

    this
  }
}
