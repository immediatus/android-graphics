package com.immediatus.graphics.texture

import javax.microedition.khronos.opengles.GL10

sealed abstract class TextureOptions(val magFilter: Int, val minFilter: Int, val wrapT: Float, val wrapS: Float, val preMultiplyAlpha: Boolean) {
  def apply(gl: GL10) {
    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, minFilter)
    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, magFilter)
    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, wrapS)
    gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, wrapT)
    gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE)
  }
}

object NEAREST extends TextureOptions(GL10.GL_NEAREST, GL10.GL_NEAREST, GL10.GL_CLAMP_TO_EDGE, GL10.GL_CLAMP_TO_EDGE, false)
object BILINEAR extends TextureOptions(GL10.GL_LINEAR, GL10.GL_LINEAR, GL10.GL_CLAMP_TO_EDGE, GL10.GL_CLAMP_TO_EDGE, false)
object REPEATING_NEAREST extends TextureOptions(GL10.GL_NEAREST, GL10.GL_NEAREST, GL10.GL_REPEAT, GL10.GL_REPEAT, false)
object REPEATING_BILINEAR extends TextureOptions(GL10.GL_LINEAR, GL10.GL_LINEAR, GL10.GL_REPEAT, GL10.GL_REPEAT, false)

object NEAREST_PREMULTIPLYALPHA extends TextureOptions(GL10.GL_NEAREST, GL10.GL_NEAREST, GL10.GL_CLAMP_TO_EDGE, GL10.GL_CLAMP_TO_EDGE, true)
object BILINEAR_PREMULTIPLYALPHA extends TextureOptions(GL10.GL_LINEAR, GL10.GL_LINEAR, GL10.GL_CLAMP_TO_EDGE, GL10.GL_CLAMP_TO_EDGE, true)
object REPEATING_NEAREST_PREMULTIPLYALPHA extends TextureOptions(GL10.GL_NEAREST, GL10.GL_NEAREST, GL10.GL_REPEAT, GL10.GL_REPEAT, true)
object REPEATING_BILINEAR_PREMULTIPLYALPHA extends TextureOptions(GL10.GL_LINEAR, GL10.GL_LINEAR, GL10.GL_REPEAT, GL10.GL_REPEAT, true)

object DEFAULT extends TextureOptions(GL10.GL_NEAREST, GL10.GL_NEAREST, GL10.GL_CLAMP_TO_EDGE, GL10.GL_CLAMP_TO_EDGE, true)
