package com.immediatus.graphics.view

import android.content.Context
import android.util.AttributeSet
import android.opengl.GLSurfaceView

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import com.immediatus.graphics.utils.GL10Wrapper
import com.immediatus.graphics.engine.Engine

class RenderView(ctx: Context, attrs: AttributeSet) extends GLSurfaceView(ctx, attrs) {
  private var _engine: Engine = null

  def this(ctx: Context) = this(ctx, null)

  def setRenderer(engine: Engine) {
    _engine = engine
    setOnTouchListener(_engine)
    setRenderer(Renderer)
  }

  protected override def onMeasure(w: Int, h: Int) {
    //_engine.getEngineOptions().getResolutionPolicy().onMeasure(this, w_, h_);
  }

  def setMeasuredDimensionProxy(w: Int, h: Int) {
    setMeasuredDimension(w, h);
  }

  object Renderer extends GLSurfaceView.Renderer {

   def onSurfaceChanged(gl: GL10, w: Int, h: Int) {
      _engine.setCanvasSize(w, h)
      gl.glViewport(0, 0, w, h)
      gl.glLoadIdentity()
    }

    override def onSurfaceCreated(gl: GL10, config: EGLConfig) {
      GL10Wrapper(gl).
        reset.
        setPerspectiveCorrectionHintFastest.
        setShadeModelFlat.
        disableLightning.
        disableDither.
        disableDepthTest.
        disableMultisample.
        enableBlend.
        enableTextures.
        enableTexCoordArray.
        enableVertexArray.
        enableCulling

      gl.glFrontFace(GL10.GL_CCW);
      gl.glCullFace(GL10.GL_BACK);

      //OpenGLWrapper.enableExtensions(gl, this._engine.getEngineOptions().getRenderOptions());
    }

    override def onDrawFrame(gl: GL10) {
      _engine.onDrawFrame(gl)
    }
  }
}
