package com.immediatus.graphics.manager

import scala.collection.mutable.HashSet
import scala.collection.mutable.SynchronizedSet
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.SynchronizedBuffer

import javax.microedition.khronos.opengles.GL10
import java.io.IOException

import com.immediatus.graphics.texture.Texture


class TextureManager {
  private val _texturesManaged = new HashSet[Texture] with SynchronizedSet[Texture]
  private val _texturesLoaded = new ArrayBuffer[Texture] with SynchronizedBuffer[Texture]
  private val _texturesToBeLoaded = new ArrayBuffer[Texture] with SynchronizedBuffer[Texture]
  private val _texturesToBeUnloaded = new ArrayBuffer[Texture] with SynchronizedBuffer[Texture]

  private val _lock : AnyRef = new Object()

  def clear() = _lock.synchronized {
    _texturesToBeLoaded.clear()
    _texturesLoaded.clear()
    _texturesManaged.clear()
  }

  def loadTexture(tex: Texture) = _lock.synchronized {
    if (_texturesManaged.contains(tex)) {
      _texturesToBeUnloaded -= tex
    } else{
      _texturesManaged += tex
      _texturesToBeLoaded += tex
    }
  }

  def unloadTexture(tex: Texture) = _lock.synchronized {
    if (_texturesManaged.contains(tex)) {
      if (_texturesLoaded.contains(tex)) _texturesToBeUnloaded += tex
      else if (_texturesToBeLoaded.contains(tex)) {
        _texturesToBeLoaded -= tex
        _texturesManaged -= tex
      }
    }
  }

  def loadTextures(texs: Texture*) = texs.foreach(loadTexture)

  def unloadTextures(texs: Texture*) = texs.foreach(unloadTexture)

  def reloadTextures() = _lock.synchronized {

    _texturesManaged.foreach(_.markToReload)

    _texturesToBeLoaded ++= _texturesLoaded
    _texturesLoaded.clear()

    _texturesManaged ++= _texturesToBeUnloaded
    _texturesToBeUnloaded.clear()
  }

  def updateTextures(gl: GL10) = _lock.synchronized {

    try { _texturesLoaded.filter(_.isUpdateOnHardwareNeeded).foreach(_.reloadToHardware(gl)) }
    catch { case e: IOException => }

    try { _texturesToBeLoaded.filter(!_.isLoadedToHardware).foreach(_.loadToHardware(gl)) }
    catch { case e: IOException => }

    _texturesLoaded ++= _texturesToBeLoaded
    _texturesToBeLoaded.clear()

    try { _texturesToBeUnloaded.filter(_.isLoadedToHardware).foreach(_.unloadFromHardware(gl)) }
    catch { case e: IOException => }

    _texturesLoaded --= _texturesToBeUnloaded
    _texturesManaged --= _texturesToBeUnloaded
    _texturesToBeUnloaded.clear()

    System.gc()
  }
}
