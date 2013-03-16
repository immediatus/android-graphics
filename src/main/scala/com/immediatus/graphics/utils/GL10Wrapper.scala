package com.immediatus.graphics.utils

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLException
import android.os.Build

import javax.microedition.khronos.opengles.GL10
import javax.microedition.khronos.opengles.GL11

import java.nio.ByteBuffer
import java.nio.IntBuffer
import java.nio.ByteOrder

import scala.collection.mutable.HashMap
import scala.collection.mutable.SynchronizedMap

object GL10Wrapper {

  private var _glWrappers = new HashMap[GL10,GL10Wrapper] with SynchronizedMap[GL10,GL10Wrapper]

  def apply(gl: GL10) = {
    _glWrappers.getOrElseUpdate(gl, new GL10Wrapper(gl))
  }
}

class GL10Wrapper(val gl: GL10) {
    val IS_LITTLE_ENDIAN = (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN)

    def EXTENSIONS_VERTEXBUFFEROBJECTS = _extensionsVertexBufferObjects
    def EXTENSIONS_DRAWTEXTURE = _extensionsDrawTexture
    def EXTENSIONS_TEXTURE_NON_POWER_OF_TWO = _extensionTextureNonPowerOfTwo

    private val  HARDWARETEXTUREID_CONTAINER = Array[Int](1)
    private val HARDWAREBUFFERID_CONTAINER = Array[Int](1)

    private var _extensionsVertexBufferObjects = false
    private var _extensionsDrawTexture = false
    private var _extensionTextureNonPowerOfTwo = false

    private var _currentHardwareBufferID = -1
    private var _currentHardwareTextureID = -1
    private var _currentMatrix = -1
    private var _currentSourceBlendMode = -1
    private var _currentDestinationBlendMode = -1

    private var _currentVertexBuffer: ByteBuffer= null
    private var _currentTextureBuffer: ByteBuffer = null

    private var _enableDither = true
    private var _enableLightning = true
    private var _enableDepthTest = true
    private var _enableMultisample = true
    private var _enableScissorTest = false
    private var _enableBlend = false
    private var _enableCulling = false
    private var _enableTextures = false
    private var _enableTexCoordArray = false
    private var _enableVertexArray = false
    private var _depthMaskEnabled = false
    private var _lineWidth = 1.0F
    private var _red = -1.0F
    private var _green = -1.0F
    private var _blue = -1.0F
    private var _alpha = -1.0F

    def reset: this.type = {
      _currentHardwareBufferID = -1
      _currentHardwareTextureID = -1
      _currentMatrix = -1
      _currentSourceBlendMode = -1
      _currentDestinationBlendMode = -1
      _currentVertexBuffer = null
      _currentTextureBuffer = null

      enableDither
      enableLightning
      enableDepthTest
      enableMultisample
      disableBlend
      disableCulling
      disableTextures
      disableTexCoordArray
      disableVertexArray

      _lineWidth = 1
      _red = -1
      _green = -1
      _blue = -1
      _alpha = -1

      _extensionsVertexBufferObjects = false
      _extensionsDrawTexture = false
      _extensionTextureNonPowerOfTwo = false

      this
    }

    def enableExtensions(disableExtensionVertexBufferObjects: Boolean): this.type = {

      val version = gl.glGetString(GL10.GL_VERSION)
      val renderer = gl.glGetString(GL10.GL_RENDERER)
      val extensions = gl.glGetString(GL10.GL_EXTENSIONS)

      val isOpenGL10 = version.contains("1.0")
      val isOpenGL2X = version.contains("2.")
      val isSoftwareRenderer = renderer.contains("PixelFlinger")
      val isVBOCapable = extensions.contains("_vertex_buffer_object")
      val isDrawTextureCapable = extensions.contains("draw_texture")
      val isTextureNonPowerOfTwoCapable = extensions.contains("texture_npot")

      _extensionsVertexBufferObjects = !disableExtensionVertexBufferObjects && !isSoftwareRenderer && (isVBOCapable || !isOpenGL10)
      _extensionsDrawTexture = !disableExtensionVertexBufferObjects && (isDrawTextureCapable || !isOpenGL10)
      _extensionTextureNonPowerOfTwo = isTextureNonPowerOfTwoCapable || isOpenGL2X

      if(Build.PRODUCT.contains("morrison")) _extensionsVertexBufferObjects = false

      this
    }

    def setColor(r: Float, g: Float, b: Float, a: Float): this.type = {
      if (a != _alpha || r != _red || g != _green || b != _blue) {
        _alpha = a
        _red = r
        _green = g
        _blue = b
        gl.glColor4f(r, g, b, a)
      }

      this
    }

    def enableVertexArray: this.type = {
      if (!_enableVertexArray) {
          _enableVertexArray = true
          gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)
      }

      this
    }

    def disableVertexArray: this.type = {
      if (_enableVertexArray) {
          _enableVertexArray = false
          gl.glDisableClientState(GL10.GL_VERTEX_ARRAY)
      }

      this
    }

    def enableTexCoordArray: this.type = {
      if (!_enableTexCoordArray) {
          _enableTexCoordArray = true
          gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY)
      }

      this
    }

    def disableTexCoordArray: this.type = {
      if (_enableTexCoordArray) {
          _enableTexCoordArray = false
          gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY)
      }

      this
    }

    def enableScissorTest: this.type = {
      if (!_enableScissorTest) {
          _enableScissorTest = true
          gl.glEnable(GL10.GL_SCISSOR_TEST)
      }

      this
    }

    def disableScissorTest: this.type = {
      if (_enableScissorTest) {
          _enableScissorTest = false
          gl.glDisable(GL10.GL_SCISSOR_TEST)
      }

      this
    }

    def enableBlend: this.type = {
      if (!_enableBlend) {
          _enableBlend = true
          gl.glEnable(GL10.GL_BLEND)
      }

      this
    }

    def disableBlend: this.type = {
      if (_enableBlend) {
          _enableBlend = false
          gl.glDisable(GL10.GL_BLEND)
      }

      this
    }

    def enableCulling: this.type = {
      if (!_enableCulling) {
          _enableCulling = true
          gl.glEnable(GL10.GL_CULL_FACE)
      }

      this
    }

    def disableCulling: this.type = {
      if (_enableCulling) {
          _enableCulling = false
          gl.glDisable(GL10.GL_CULL_FACE)
      }

      this
    }

    def enableTextures: this.type = {
      if (!_enableTextures) {
          _enableTextures = true
          gl.glEnable(GL10.GL_TEXTURE_2D)
      }

      this
    }

    def disableTextures: this.type = {
      if (_enableTextures) {
          _enableTextures = false
          gl.glDisable(GL10.GL_TEXTURE_2D)
      }

      this
    }

    def enableLightning: this.type = {
      if (!_enableLightning) {
          _enableLightning = true
          gl.glEnable(GL10.GL_LIGHTING)
      }

      this
    }

    def disableLightning: this.type = {
      if (_enableLightning) {
          _enableLightning = false
          gl.glDisable(GL10.GL_LIGHTING)
      }

      this
    }

    def enableDither: this.type = {
      if (!_enableDither) {
          _enableDither = true
          gl.glEnable(GL10.GL_DITHER)
          GLES20.glEnable(GLES20.GL_DITHER)
      }

      this
    }

    def disableDither: this.type = {
      if (_enableDither) {
          _enableDither = false
          gl.glDisable(GL10.GL_DITHER)
      }

      this
    }

    def enableDepthTest: this.type = {
      if (!_enableDepthTest) {
          _enableDepthTest = true
          gl.glEnable(GL10.GL_DEPTH_TEST)
      }

      this
    }

    def disableDepthTest: this.type = {
      if (_enableDepthTest) {
          _enableDepthTest = false
          gl.glDisable(GL10.GL_DEPTH_TEST)
      }

      this
    }

    def enableMultisample: this.type = {
      if (!_enableMultisample) {
          _enableMultisample = true
          gl.glEnable(GL10.GL_MULTISAMPLE)
      }

      this
    }

    def disableMultisample: this.type = {
      if (_enableMultisample) {
          _enableMultisample = false
          gl.glDisable(GL10.GL_MULTISAMPLE)
      }

      this
    }

    def enableDepthMask: this.type = {
      if (!_depthMaskEnabled) {
          gl.glDepthMask(true)
          _depthMaskEnabled = true
      }

      this
    }

    def disableDepthMask: this.type = {
      if (_depthMaskEnabled) {
          gl.glDepthMask(false)
          _depthMaskEnabled = false
      }

      this
    }

    def lineWidth(lineWidth: Float): this.type = {
      if (_lineWidth != lineWidth) {
          _lineWidth = lineWidth
          gl.glLineWidth(lineWidth)
      }

      this
    }

    def switchToModelViewMatrix: this.type = {
      if (_currentMatrix != GL10.GL_MODELVIEW) {
          _currentMatrix = GL10.GL_MODELVIEW
          gl.glMatrixMode(GL10.GL_MODELVIEW)
      }

      this
    }

    def switchToProjectionMatrix: this.type = {
      if (_currentMatrix != GL10.GL_PROJECTION) {
          _currentMatrix = GL10.GL_PROJECTION
          gl.glMatrixMode(GL10.GL_PROJECTION)
      }

      this
    }

    def setProjectionIdentityMatrix: this.type = {
        switchToProjectionMatrix
        gl.glLoadIdentity()

      this
    }

    def setModelViewIdentityMatrix: this.type = {
        switchToModelViewMatrix
        gl.glLoadIdentity()

      this
    }

    def setShadeModelFlat: this.type = {
        gl.glShadeModel(GL10.GL_FLAT)

      this
    }

    def setPerspectiveCorrectionHintFastest: this.type = {
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST)

      this
    }

    def checkGLError: this.type = {
        val err = gl.glGetError();
        if (err != GL10.GL_NO_ERROR){
            throw new GLException(err);
        }

      this
    }


    def bindTexture(hardwareTextureID: Int): this.type = {
      if (_currentHardwareTextureID != hardwareTextureID) {
          _currentHardwareTextureID = hardwareTextureID
          gl.glBindTexture(GL10.GL_TEXTURE_2D, hardwareTextureID);
      }

      this
    }

    def forceBindTexture(hardwareTextureID: Int): this.type = {
      _currentHardwareTextureID = hardwareTextureID
      gl.glBindTexture(GL10.GL_TEXTURE_2D, hardwareTextureID)

      this
    }

    def deleteTexture(hardwareTextureID: Int): this.type = {
      HARDWARETEXTUREID_CONTAINER(0) = hardwareTextureID
      gl.glDeleteTextures(1, HARDWARETEXTUREID_CONTAINER, 0)

      this
    }

    def texCoordPointer(textureBuffer: ByteBuffer): this.type = {
      if (_currentTextureBuffer != textureBuffer) {
          _currentTextureBuffer = textureBuffer
          gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer)
      }

      this
    }

    def vertexPointer(vertexBuffer: ByteBuffer): this.type = {
      if (_currentVertexBuffer != vertexBuffer) {
          _currentVertexBuffer = vertexBuffer
          gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexBuffer)
      }

      this
    }

    def blendFunction(sourceBlendMode: Int, destinationBlendMode: Int): this.type = {
      if (_currentSourceBlendMode != sourceBlendMode || _currentDestinationBlendMode != destinationBlendMode) {
          _currentSourceBlendMode = sourceBlendMode
          _currentDestinationBlendMode = destinationBlendMode
          gl.glBlendFunc(sourceBlendMode, destinationBlendMode)
      }

      this
    }

    def glTexImage2D(target: Int, level: Int, bitmap: Bitmap, border: Int,  pixelFormat: PixelFormat): this.type = {
        val pixelBuffer = getPixels(bitmap, pixelFormat)
        gl.glTexImage2D(target, level, pixelFormat.glFormat, bitmap.getWidth, bitmap.getHeight, border, pixelFormat.glFormat, pixelFormat.glType, pixelBuffer)

        this
    }

    def glTexSubImage2D(target: Int, level: Int, xOffset: Int, yOffset: Int, bitmap: Bitmap, pixelFormat: PixelFormat): this.type = {
        val pixelBuffer = getPixels(bitmap, pixelFormat)
        gl.glTexSubImage2D(target, level, xOffset, yOffset, bitmap.getWidth, bitmap.getHeight, pixelFormat.glFormat, pixelFormat.glType, pixelBuffer);

        this
    }

    private def getPixels(bitmap: Bitmap, pixelFormat: PixelFormat ) = {
      val pixelsARGB_8888 = getPixelsARGB_8888(bitmap)

      pixelFormat match {
        case PixelFormat.RGB_565 => ByteBuffer.wrap(convertARGB_8888toRGB_565(pixelsARGB_8888))
        case PixelFormat.RGBA_8888 => IntBuffer.wrap(convertARGB_8888toRGBA_8888(pixelsARGB_8888))
        case PixelFormat.RGBA_4444 => ByteBuffer.wrap(convertARGB_8888toARGB_4444(pixelsARGB_8888))
        case PixelFormat.A_8 => ByteBuffer.wrap(convertARGB_8888toA_8(pixelsARGB_8888))
        case _ => ByteBuffer.wrap(Array[Byte](0))
      }
    }

    private def convertARGB_8888toRGBA_8888(pixelsARGB8888: Array[Int]) = {
      pixelsARGB8888.map(p => {
        if (IS_LITTLE_ENDIAN) p & 0xFF00FF00 | (p & 0x000000FF) << 16 | (p & 0x00FF0000) >> 16
        else (p & 0x00FFFFFF) << 8 | (p & 0xFF000000) >> 24
      }).toArray
    }

    private def convertARGB_8888toRGB_565(pixelsARGB8888: Array[Int]) = {
      pixelsARGB8888.flatMap(p => {
        val red = ((p >> 16) & 0xFF)
        val green = ((p >> 8) & 0xFF)
        val blue = (p & 0xFF)

        if (IS_LITTLE_ENDIAN) (((green << 3) & 0xE0) | (blue >> 3)).toByte :: ((red & 0xF8) | (green >> 5)).toByte :: Nil
        else ((red & 0xF8) | (green >> 5)).toByte :: (((green << 3) & 0xE0) | (blue >> 3)).toByte :: Nil
      }).toArray
    }

    private def convertARGB_8888toARGB_4444(pixelsARGB8888: Array[Int]) = {
      pixelsARGB8888.flatMap(p => {
        val alpha = ((p >> 28) & 0x0F)
        val red = ((p >> 16) & 0xF0)
        val green = ((p >> 8) & 0xF0)
        val blue = (p & 0x0F)

        if (IS_LITTLE_ENDIAN) (green | blue).toByte :: (alpha | red).toByte :: Nil
        else (alpha | red).toByte :: (green | blue).toByte :: Nil
      }).toArray
    }

    private def convertARGB_8888toA_8(pixelsARGB8888: Array[Int]) = {
      pixelsARGB8888.map(p => (if (IS_LITTLE_ENDIAN) p >> 24 else p & 0xFF).toByte).toArray
    }

    private def getPixelsARGB_8888(bitmap: Bitmap): Array[Int] = {
        val w = bitmap.getWidth
        val h = bitmap.getHeight

        val pixelsARGB_8888 = new Array[Int](w * h)
        bitmap.getPixels(pixelsARGB_8888, 0, w, 0, 0, w, h)

        pixelsARGB_8888
    }

}
