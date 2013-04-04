package com.immediatus.graphics.camera

import javax.microedition.khronos.opengles.GL10

import com.immediatus.graphics.utils.GL10Wrapper
import com.immediatus.graphics.GraphicsUnit

class Camera(var x: Float, var y: Float, val _width: Float, val _height: Float) {
  protected final val VERTEX_INDEX_X = 0
  protected final val VERTEX_INDEX_Y = 1
  protected final val VERTICES_TOUCH_BUFFER = Array[Float](2)

  private final val DEG_TO_RAD = Math.PI / 180f

  private var _chaseUnit: GraphicsUnit = null

  private var _minX = x
  private var _maxX = x + _width
  private var _minY = y
  private var _maxY = y + _height
  private var _nearZ = -1f
  private var _farZ = 1f

  protected var _rotation = 0f
  protected var _cameraRotation = 0f
  protected var _canvasX = 0f
  protected var _canvasY = 0f
  protected var _canvasWidth = 0f
  protected var _canvasHeight = 0f

  def minX = _minX
  def maxX = _maxX
  def minY = _minY
  def maxY = _maxY
  def canvasX = _canvasX
  def canvasY = _canvasY
  def width = _width
  def height = _height
  def canvasWidth = _canvasWidth
  def canvasHeight = _canvasHeight
  def centerX = _minX + (_maxX - _minX) * 0.5f
  def centerY = _minY + (_maxY - _minY) * 0.5f
  def isRotated = _rotation != 0
  def rotation = _rotation
  def cameraRotation = _cameraRotation
  def nearZClippingPlane = _nearZ
  def farZClippingPlane =  _farZ

  def nearZClippingPlane_=(nearZClippingPlane: Float): Unit = _nearZ = nearZClippingPlane
  def farZClippingPlane_=(farZClippingPlane: Float): Unit = _farZ = farZClippingPlane

  def setChaseUnit(unit: GraphicsUnit) = _chaseUnit = unit

  def setZClippingPlanes(nearZClippingPlane: Float, farZClippingPlane: Float) {
    _nearZ = nearZClippingPlane
    _farZ = farZClippingPlane
  }

  def setCenter(x: Float, y: Float): Unit = {
    val dX = x - centerX
    val dY = y - centerY

    _minX += dX
    _maxX += dX
    _minY += dY
    _maxY += dY
  }

  def offsetCenter(x: Float, y: Float): Unit = setCenter(centerX + x, centerY + y)

  def rotate(rotation: Float): Unit = _rotation = rotation

  def cameraRotate(rotation: Float): Unit = _cameraRotation = rotation

  def setSurfaceSize(x: Float, y: Float, w: Float, h: Float): Unit = {
    _canvasX = x
    _canvasY = y
    _canvasWidth = w
    _canvasHeight = h
  }

  def updateChaseEntity() {
    if (_chaseUnit != null) {
      val x :: y :: _ = _chaseUnit.getLayerCenterCoordinates().toList
      setCenter(x, y)
    }
  }

  def onApplyLayerMatrix(gl: GL10) {
    GL10Wrapper(gl).setProjectionIdentityMatrix
    gl.glOrthof(_minX, _maxX, _maxY, _minY, _nearZ, _farZ)
    if (_rotation != 0) applyRotation(gl, centerX, centerY, _rotation)
  }

  def onApplyLayerBackgroundMatrix(gl: GL10) {
    GL10Wrapper(gl).setProjectionIdentityMatrix
    gl.glOrthof(0, _width, _height, 0, _nearZ, _farZ)
    if(_rotation != 0) applyRotation(gl, _width * 0.5f, _height * 0.5f, _rotation);
  }

  def onApplyCameraLayerMatrix(gl: GL10) {
    GL10Wrapper(gl).setProjectionIdentityMatrix
    gl.glOrthof(0, _width, _height, 0, _nearZ, _farZ)
    if(_cameraRotation != 0) applyRotation(gl, _width * 0.5f, _height * 0.5f, _cameraRotation)
  }


  private def applyRotation(gl: GL10, x: Float, y: Float, angle: Float) {
    gl.glTranslatef(x, y, 0)
    gl.glRotatef(angle, 0, 0, 1)
    gl.glTranslatef(-x, -y, 0)
  }
}
