package com.immediatus.graphics.manager

import scala.collection.mutable.HashSet
import scala.collection.mutable.SynchronizedSet
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.SynchronizedBuffer

import javax.microedition.khronos.opengles.GL11;

import com.immediatus.graphics.buffer.BufferObject


class BufferObjectManager{
  private val _bufferObjectsManaged = new HashSet[BufferObject] with SynchronizedSet[BufferObject]
  private val _bufferObjectsLoaded = new ArrayBuffer[BufferObject] with SynchronizedBuffer[BufferObject]
  private val _bufferObjectsToBeLoaded = new ArrayBuffer[BufferObject] with SynchronizedBuffer[BufferObject]
  private val _bufferObjectsToBeUnloaded = new ArrayBuffer[BufferObject] with SynchronizedBuffer[BufferObject]

  private val _lock : AnyRef = new Object()


  def clear() = _lock.synchronized {
    _bufferObjectsToBeLoaded.clear()
    _bufferObjectsLoaded.clear()
    _bufferObjectsManaged.clear()
  }

  def loadBufferObject(obj: BufferObject) = _lock.synchronized {
    if (obj != null) {
      if (_bufferObjectsManaged.contains(obj)) {
        _bufferObjectsToBeUnloaded -= obj
      } else{
        _bufferObjectsManaged += obj
        _bufferObjectsToBeLoaded += obj
      }
    }
  }

  def unloadBufferObject(obj: BufferObject) = _lock.synchronized {
    if (obj != null) {
      if (_bufferObjectsManaged.contains(obj)) {
        if (_bufferObjectsLoaded.contains(obj)) {
          _bufferObjectsToBeUnloaded += obj
        } else if (_bufferObjectsToBeLoaded.contains(obj)) {
          _bufferObjectsToBeLoaded -= obj
          _bufferObjectsManaged -= obj
        }
      }
    }
  }

  def loadBufferObjects(objs: BufferObject*) = objs.foreach(loadBufferObject)

  def unloadBufferObjects(objs: BufferObject*) = objs.foreach(unloadBufferObject)

  def reloadBufferObjects() = _lock.synchronized {
    _bufferObjectsLoaded.foreach(_.markToReload)
    _bufferObjectsToBeLoaded ++= _bufferObjectsLoaded
    _bufferObjectsLoaded.clear()
  }

  def updateBufferObjects(gl: GL11) = _lock.synchronized {
    _bufferObjectsToBeLoaded.
      filterNot(_.isLoadedToHardware).
      foreach(obj => {
        obj.loadToHardware(gl)
        obj.setHardwareBufferNeedsUpdate()
      })

    _bufferObjectsLoaded ++= _bufferObjectsToBeLoaded
    _bufferObjectsToBeLoaded.clear()

    _bufferObjectsToBeUnloaded.
      filter(_.isLoadedToHardware).
      foreach(obj => {
        obj.unloadFromHardware(gl)
      })

    _bufferObjectsLoaded --= _bufferObjectsToBeUnloaded
    _bufferObjectsManaged --= _bufferObjectsToBeUnloaded
  }
}
