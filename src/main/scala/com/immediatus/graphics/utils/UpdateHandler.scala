package com.immediatus.graphics.utils

import scala.collection.mutable.ListBuffer


trait UpdateHandler {

  private var _handlers = ListBuffer[UpdateHandler]()


  def onUpdate(secondsElapsed: Float)
  def reset()

  protected def onManagedUpdate(secondsElapsed: Float) {
    onUpdate(secondsElapsed)

    _handlers.foreach(handler => {
      handler.onUpdate(secondsElapsed)
      handler.onManagedUpdate(secondsElapsed)
    })
  }

  def registerUpdateHandler(handler: UpdateHandler) = _handlers += handler

  def unregisterUpdateHandler(handler: UpdateHandler) = _handlers -= handler

  def unregisterUpdateHandlers(f: UpdateHandler => Boolean) = _handlers.filter(f).foreach(unregisterUpdateHandler)

  def clearUpdateHandlers() = _handlers.clear()
}
