package com.immediatus.graphics.engine

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import com.immediatus.graphics.utils.GL10Wrapper

class Engine extends OnTouchListener {

  private var _canvasWidth = 1
  private var _canvasHeight = 1

  override def onTouch(view: View, event: MotionEvent) = {
    true
  }

  def setCanvasSize(w: Int, h: Int) {
    _canvasWidth = w
    _canvasHeight = h
    onUpdateCameraSurface();
  }

  def onDrawFrame(gl: GL10) {
    val threadLocker = State

    threadLocker.waitUntilCanDraw()

//    _textureManager.updateTextures(gl_)
//    _fontManager.updateFonts(gl_)
//    if (GL10Wrapper.EXTENSIONS_VERTEXBUFFEROBJECTS) _bufferObjectManager.updateBufferObjects(gl.asInstanceOf[GL11])
    onDrawCanvas(gl)

    threadLocker.notifyCanUpdate()
  }



  protected def onUpdateCameraSurface() {
//    _camera.setSurfaceSize(0, 0, _canvasWidth, _canvasHeight)
  }

  private def onDrawCanvas(gl: GL10) {
//    if (_canvas != null) _canvas.onDraw(gl, getCamera)
  }

  private object State {
    private val _lock : AnyRef = new Object()
    private var _drawing = false

    def notifyCanDraw() = _lock.synchronized {
      _drawing = true
      _lock.notifyAll
    }

    def notifyCanUpdate() = _lock.synchronized {
      _drawing = false
      _lock.notifyAll
    }

    def waitUntilCanDraw() = _lock.synchronized { while (!_drawing) _lock.wait }

    def waitUntilCanUpdate() = _lock.synchronized { while (_drawing) _lock.wait }
  }
}
