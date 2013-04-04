package com.immediatus.graphics

import javax.microedition.khronos.opengles.GL10

import com.immediatus.graphics.utils.Transformation
import com.immediatus.graphics.utils.ConditionalApplicative._


trait GraphicsUnit {

  private var _visible = true
  private var _ignoreUpdate = false
  private var _zIndex = 0

  private var _red = 1f
  private var _green = 1f
  private var _blue = 1f
  private var _alpha = 1f

  private var _x = 0f
  private var _y = 0f

  private var _rotation = 0f

  private var _rotationCenterX = 0f
  private var _rotationCenterY = 0f

  private var _scaleX = 1f
  private var _scaleY = 1f

  private var  _scaleCenterX = 0f
  private var  _scaleCenterY = 0f

  private var _localToParentTransformationDirty = true
  private var _parentToLocalTransformationDirty = true

  private var _localToParentTransformation = Transformation()
  private var _parentToLocalTransformation = Transformation()

  private var _localToLayerTransformation = Transformation()
  private var _layerToLocalTransformation = Transformation()

  def isVisible = _visible
  def isIgnoreUpdate = _ignoreUpdate
  def zIndex = _zIndex
  def x = _x
  def y = _y
  def rotationCenterX = _rotationCenterX
  def rotationCenterY = _rotationCenterY
  def rotation = _rotation
  def isRotated = _rotation != 0;
  def isScaled = _scaleX != 1 || _scaleY != 1
  def scaleX = _scaleX
  def scaleY = _scaleY
  def scaleCenterX = _scaleCenterX
  def scaleCenterY = _scaleCenterY
  def red = _red
  def green = _green
  def blue = _blue
  def alpha = _alpha

  def onDraw(gl: GL10) = if (_visible) onManagedDraw(gl)

  def setPosition(x: Float, y: Float): Unit = {
    _x = x
    _y = y
    _localToParentTransformationDirty = true
    _parentToLocalTransformationDirty = true
  }

  def scale(s: Float): Unit = scale(s, s)

  def setColor(red: Float, green: Float, blue: Float, alpha:Float): Unit = {
    _red = red
    _green = green
    _blue = blue
    _alpha=  alpha
  }

  def setColor(red: Float, green: Float, blue: Float): Unit = setColor(red, green, blue, 1f)

  def setRotationCenter(x: Float, y: Float): Unit = {
    _rotationCenterX = x
    _rotationCenterY = y
    _localToParentTransformationDirty = true
    _parentToLocalTransformationDirty = true
  }

  def setScaleCenter(x: Float, y: Float): Unit = {
    _scaleCenterX = x
    _scaleCenterY = y
    _localToParentTransformationDirty = true
    _parentToLocalTransformationDirty = true
  }

  def getLocalToParentTransformation(): Transformation = {
    if(_localToParentTransformationDirty) {
      _localToParentTransformationDirty = false
       _red = red
   _localToParentTransformation = Transformation().
        $if(_scaleX != 1 || _scaleY != 1) {
          _.postTranslate(-_scaleCenterX, -_scaleCenterY).
          postScale(_scaleX, _scaleY).
          postTranslate(_scaleCenterX, _scaleCenterY)
        }.$if(rotation != 0) {
          _.postTranslate(-_rotationCenterX, -_rotationCenterY).
          postRotate(_rotation).
          postTranslate(_rotationCenterX, _rotationCenterY)
        }.postTranslate(_x, _y)
    }

    _localToParentTransformation
  }

  def rotate(rotation: Float){
    _rotation = rotation
    _localToParentTransformationDirty = true
    _parentToLocalTransformationDirty = true
  }

  def scale(x: Float, y: Float): Unit = {
    _scaleX = x
    _scaleY = y
    _localToParentTransformationDirty = true
    _parentToLocalTransformationDirty = true
  }

  def getLayerCenterCoordinates() = convertLocalToLayerCoordinates(0, 0)

  def getParentToLocalTransformation(): Transformation = {
    if(_parentToLocalTransformationDirty) {
    _parentToLocalTransformationDirty = false;
    _parentToLocalTransformation = Transformation().
      postTranslate(_x, _y).
      $if(_rotation != 0) {
        _.postTranslate(-_rotationCenterX, -_rotationCenterY).
        postRotate(-_rotation).
        postTranslate(_rotationCenterX, _rotationCenterY)
      }.$if(_scaleX != 1 || _scaleY != 1) {
        _.postTranslate(-scaleCenterX, -scaleCenterY).
        postScale(1 / _scaleX, 1 / _scaleY).
        postTranslate(_scaleCenterX, _scaleCenterY)
      }
    }

    _parentToLocalTransformation
  }

  def doDraw(gl: GL10): Unit
  def getLocalToLayerTransformation(): Transformation
  def getLayerToLocalTransformation(): Transformation

//  def getLocalToLayerTransformation(): Transformation = {
//    _localToLayerTransformation = getLocalToParentTransformation().
//      $if(parent != null) {
//        _.postConcat(parent.getLocalToLayerTransformation())
//      }
//
//    _localToLayerTransformation
//  }
//
//  def getLayerToLocalTransformation(): Transformation = {
//    _layerToLocalTransformation = getParentToLocalTransformation().
//      $if(parent != null) {
//        _.postConcat(parent.getLayerToLocalTransformation())
//      }
//
//    _layerToLocalTransformation
//  }

  def convertLocalToLayerCoordinates(x: Float, y: Float) = getLocalToLayerTransformation().transform(x, y)

  def convertLocalToLayerCoordinates(coord: Float*) = getLocalToLayerTransformation().transform(coord: _*)

  def convertLayerToLocalCoordinates(x: Float, y: Float) = getLayerToLocalTransformation().transform(x, y)

  def convertLayerToLocalCoordinates(coord: Float*) = getLayerToLocalTransformation().transform(coord: _*)

  protected def onApplyTransformations(gl: GL10){
    applyTranslation(gl)
    applyRotation(gl)
    applyScale(gl)
  }

  protected def applyTranslation(gl: GL10) {
    gl.glTranslatef(_x, _y, 0)
  }

  protected def applyRotation(gl: GL10) {
    if (_rotation != 0) {
      gl.glTranslatef(_rotationCenterX, _rotationCenterY, 0)
      gl.glRotatef(_rotation, 0, 0, 1)
      gl.glTranslatef(-_rotationCenterX, -_rotationCenterY, 0)
    }
  }

  protected def applyScale(gl: GL10) {
    if (_scaleX != 1 || _scaleY != 1) {
      gl.glTranslatef(_scaleCenterX, _scaleCenterY, 0)
      gl.glScalef(_scaleX, _scaleY, 1)
      gl.glTranslatef(-_scaleCenterX, -_scaleCenterY, 0)
    }
  }

  protected def onManagedDraw(gl: GL10) {
    gl.glPushMatrix()
    onApplyTransformations(gl)
    doDraw(gl)
    gl.glPopMatrix()
  }
}
