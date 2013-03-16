package com.immediatus.graphics.texture

trait ITextureAtlasSource {
  def positionX: Int
  def positionX_=(positionX: Int): Unit

  def positionY: Int
  def positionY_=(positionY: Int): Unit

  def width: Int
  def height: Int

  def copy(): ITextureAtlasSource
}
