package com.immediatus.graphics

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

  private val _initialX = _x
  private val _initialY = _y

  private var _rotation = 0

  private var _rotationCenterX = 0f
  private var _rotationCenterY = 0f

  private var _scaleX = 1f
  private var _scaleY = 1f

  private var  _scaleCenterX = 0f
  private var  _scaleCenterY = 0f

  private var _localToParentTransformationDirty = true
  private var _parentToLocalTransformationDirty = true

//  private val _localToParentTransformation = new Transformation()
//  private val _parentToLocalTransformation = new Transformation()

//  private val _localToLayerTransformation = new Transformation()
//  private val _layerToLocalTransformation = new Transformation()

  def isVisible = _visible
  def isIgnoreUpdate = _ignoreUpdate
  def zIndex = _zIndex
  def X = _x
  def Y = _y
  def initialX = _initialX
  def initialY = _initialY;
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

//  def setPosition(x: Float, y: Float) {
//    _x = x
//    _y = y
//    _localToParentTransformationDirty = true
//    _parentToLocalTransformationDirty = true
//  }
//
//  def setInitialPosition() {
//    _x = _initialX
//    _y = _initialY
//    _localToParentTransformationDirty = true
//    _parentToLocalTransformationDirty = true
//  }
//
//  def rotate(rotation: Float){
//    _rotation = rotation
//    _localToParentTransformationDirty = true
//    _parentToLocalTransformationDirty = true
//  }
//
//
//    public Transformation getLocalToParentTransformation(){
//        final Transformation localToParentTransformation = this._localToParentTransformation;
//        if (this._localToParentTransformationDirty){
//            localToParentTransformation.setToIdentity();
//
//            final float scaleX = this._scaleX;
//            final float scaleY = this._scaleY;
//            if (scaleX != 1 || scaleY != 1){
//                final float scaleCenterX = this._scaleCenterX;
//                final float scaleCenterY = this._scaleCenterY;
//                localToParentTransformation.postTranslate(-scaleCenterX, -scaleCenterY);
//                localToParentTransformation.postScale(scaleX, scaleY);
//                localToParentTransformation.postTranslate(scaleCenterX, scaleCenterY);
//            }
//
//            final float rotation = this._rotation;
//            if (rotation != 0){
//                final float rotationCenterX = this._rotationCenterX;
//                final float rotationCenterY = this._rotationCenterY;
//                localToParentTransformation.postTranslate(-rotationCenterX, -rotationCenterY);
//                localToParentTransformation.postRotate(rotation);
//                localToParentTransformation.postTranslate(rotationCenterX, rotationCenterY);
//            }
//            localToParentTransformation.postTranslate(this._x, this._y);
//            this._localToParentTransformationDirty = false;
//        }
//        return localToParentTransformation;
//    }
//
//    public Transformation getParentToLocalTransformation(){
//        final Transformation parentToLocalTransformation = this._parentToLocalTransformation;
//        if (this._parentToLocalTransformationDirty){
//            parentToLocalTransformation.setToIdentity();
//            parentToLocalTransformation.postTranslate(-this._x, -this._y);
//            final float rotation = this._rotation;
//            if (rotation != 0){
//                final float rotationCenterX = this._rotationCenterX;
//                final float rotationCenterY = this._rotationCenterY;
//                parentToLocalTransformation.postTranslate(-rotationCenterX, -rotationCenterY);
//                parentToLocalTransformation.postRotate(-rotation);
//                parentToLocalTransformation.postTranslate(rotationCenterX, rotationCenterY);
//            }
//            final float scaleX = this._scaleX;
//            final float scaleY = this._scaleY;
//            if (scaleX != 1 || scaleY != 1){
//                final float scaleCenterX = this._scaleCenterX;
//                final float scaleCenterY = this._scaleCenterY;
//                parentToLocalTransformation.postTranslate(-scaleCenterX, -scaleCenterY);
//                parentToLocalTransformation.postScale(1 / scaleX, 1 / scaleY);
//                parentToLocalTransformation.postTranslate(scaleCenterX, scaleCenterY);
//            }
//            this._parentToLocalTransformationDirty = false;
//        }
//        return parentToLocalTransformation;
//    }
//
//    public Transformation getLocalToLayerTransformation(){
//        final Transformation localToLayerTransformation = this._localToLayerTransformation;
//        localToLayerTransformation.setTo(this.getLocalToParentTransformation());
//
//        final IUnit parent = this._parent;
//        if (parent != null){
//            localToLayerTransformation.postConcat(parent.getLocalToLayerTransformation());
//        }
//
//        return localToLayerTransformation;
//    }
//
//    public Transformation getLayerToLocalTransformation(){
//        final Transformation layerToLocalTransformation = this._layerToLocalTransformation;
//        layerToLocalTransformation.setTo(this.getParentToLocalTransformation());
//
//        final IUnit parent = this._parent;
//        if (parent != null){
//            layerToLocalTransformation.postConcat(parent.getLayerToLocalTransformation());
//        }
//
//        return layerToLocalTransformation;
//    }
//
//    public float[] convertLocalToLayerCoordinates(final float x_, final float y_){
//        return this.convertLocalToLayerCoordinates(x_, y_, VERTICES_LOCAL_TO_SCENE_TMP);
//    }
//
//    public float[] convertLocalToLayerCoordinates(final float x_, final float y_, final float[] reuse_){
//        reuse_[VERTEX_INDEX_X] = x_;
//        reuse_[VERTEX_INDEX_Y] = y_;
//        this.getLocalToLayerTransformation().transform(reuse_);
//        return reuse_;
//    }
//
//    public float[] convertLocalToLayerCoordinates(final float[] coord_){
//        return this.convertLayerToLocalCoordinates(coord_, VERTICES_LOCAL_TO_SCENE_TMP);
//    }
//
//    public float[] convertLocalToLayerCoordinates(final float[] coord_, final float[] reuse_){
//        reuse_[VERTEX_INDEX_X] = coord_[VERTEX_INDEX_X];
//        reuse_[VERTEX_INDEX_Y] = coord_[VERTEX_INDEX_Y];
//        this.getLocalToLayerTransformation().transform(reuse_);
//        return reuse_;
//    }
//
//    public float[] convertLayerToLocalCoordinates(final float x_, final float y_){
//        return this.convertLayerToLocalCoordinates(x_, y_, VERTICES_SCENE_TO_LOCAL_TMP);
//    }
//
//    public float[] convertLayerToLocalCoordinates(final float x_, final float y_, final float[] reuse_){
//        reuse_[VERTEX_INDEX_X] = x_;
//        reuse_[VERTEX_INDEX_Y] = y_;
//        this.getLayerToLocalTransformation().transform(reuse_);
//        return reuse_;
//    }
//
//    public float[] convertLayerToLocalCoordinates(final float[] coord_){
//        return this.convertLayerToLocalCoordinates(coord_, VERTICES_SCENE_TO_LOCAL_TMP);
//    }
//
//    public float[] convertLayerToLocalCoordinates(final float[] coord_, final float[] reuse_){
//        reuse_[VERTEX_INDEX_X] = coord_[VERTEX_INDEX_X];
//        reuse_[VERTEX_INDEX_Y] = coord_[VERTEX_INDEX_Y];
//        this.getLayerToLocalTransformation().transform(reuse_);
//        return reuse_;
//    }
//
//  def reset(){
//    _visible = true
//    _ignoreUpdate = false
//    _x = this._initialX
//    _y = this._initialY
//    _rotation = 0
//    _scaleX = 1
//    _scaleY = 1
//    _red = 1.0f
//    _green = 1.0f
//    _blue = 1.0f
//    _alpha = 1.0f
//  }
//
//
//    protected void onApplyTransformations(final GL10 gl_){
//        this.applyTranslation(gl_);
//        this.applyRotation(gl_);
//        this.applyScale(gl_);
//    }
//
//    protected void applyTranslation(final GL10 gl_){
//        gl_.glTranslatef(this._x, this._y, 0);
//    }
//
//    protected void applyRotation(final GL10 gl_){
//        final float rotation = this._rotation;
//
//        if (rotation != 0){
//            final float rotationCenterX = this._rotationCenterX;
//            final float rotationCenterY = this._rotationCenterY;
//
//            gl_.glTranslatef(rotationCenterX, rotationCenterY, 0);
//            gl_.glRotatef(rotation, 0, 0, 1);
//            gl_.glTranslatef(-rotationCenterX, -rotationCenterY, 0);
//        }
//    }
//
//    protected void applyScale(final GL10 gl_){
//        final float scaleX = this._scaleX;
//        final float scaleY = this._scaleY;
//
//        if (scaleX != 1 || scaleY != 1){
//            final float scaleCenterX = this._scaleCenterX;
//            final float scaleCenterY = this._scaleCenterY;
//
//            gl_.glTranslatef(scaleCenterX, scaleCenterY, 0);
//            gl_.glScalef(scaleX, scaleY, 1);
//            gl_.glTranslatef(-scaleCenterX, -scaleCenterY, 0);
//        }
//    }
//
//  def onManagedDraw(final GL10 gl_, final Camera camera_){
//        gl_.glPushMatrix();
//        {
//            this.onApplyTransformations(gl_);
//            this.doDraw(gl_, camera_);
//        }
//        gl_.glPopMatrix();
//    }
}
