package com.immediatus.graphics.shape

//import com.immediatus.engine.collision.RectangularCollisionChecker;
//import com.immediatus.engine.contracts.IShape;
//import com.immediatus.engine.primitive.Line;
//import com.immediatus.graphics.camera.Camera;
//import com.immediatus.graphics.vertex.VertexBuffer;
//
//import javax.microedition.khronos.opengles.GL10;
//
//public abstract class RectangularShape extends Shape {
//
//    private float _baseWidth;
//    private float _baseHeight;
//    private float _width;
//    private float _height;
//    protected final VertexBuffer _VertexBuffer;
//
//    public RectangularShape(final float x_, final float y_, final float w_, final float h_, final VertexBuffer vertexBuffer_){
//        super(x_, y_);
//
//        this._baseWidth = w_;
//        this._baseHeight = h_;
//
//        this._width = w_;
//        this._height = h_;
//
//        this._VertexBuffer = vertexBuffer_;
//
//        float centerX = w_ * 0.5f;
//        float centerY = h_ * 0.5f;
//
//        this.setRotationCenterX(centerX);
//        this.setRotationCenterY(centerY);
//
//        this.setScaleCenterX(centerX);
//        this.setScaleCenterY(centerY);
//    }
//
//    public void setWidth(final float w_){
//        this._width = w_;
//        this.updateVertexBuffer();
//    }
//
//    public void setHeight(final float h_){
//        this._height = h_;
//        this.updateVertexBuffer();
//    }
//
//    public void setSize(final float w_, final float h_){
//        this._width = w_;
//        this._height = h_;
//        this.updateVertexBuffer();
//    }
//
//    public void setBaseSize(){
//        if (this._width != this._baseWidth || this._height != this._baseHeight){
//            this._width = this._baseWidth;
//            this._height = this._baseHeight;
//            this.updateVertexBuffer();
//        }
//    }
//
//    @Override
//    public VertexBuffer getVertexBuffer(){
//        return this._VertexBuffer;
//    }
//
//    @Override
//    public float getWidth(){
//        return this._width;
//    }
//
//    @Override
//    public float getHeight(){
//        return this._height;
//    }
//
//    @Override
//    public float getBaseWidth(){
//        return this._baseWidth;
//    }
//
//    protected void setBaseWidth(float baseWidth_){
//        _baseWidth = baseWidth_;
//    }
//
//    @Override
//    public float getBaseHeight(){
//        return this._baseHeight;
//    }
//
//    protected void setBaseHeight(float baseHeight_){
//        _baseHeight = baseHeight_;
//    }
//
//    @Override
//    public void reset(){
//        super.reset();
//        this.setBaseSize();
//
//        final float baseWidth = this.getBaseWidth();
//        final float baseHeight = this.getBaseHeight();
//
//        final float centerX = baseWidth * 0.5f;
//        final float centerY = baseHeight * 0.5f;
//
//
//        this.setRotationCenterX(centerX);
//        this.setRotationCenterY(centerY);
//
//        this.setScaleCenterX(centerX);
//        this.setScaleCenterY(centerY);
//    }
//
//    @Override
//    public boolean contains(final float x_, final float y_){
//        return RectangularCollisionChecker.checkContains(this, x_, y_);
//    }
//
//    @Override
//    public float[] getLayerCenterCoordinates(){
//        return this.convertLocalToLayerCoordinates(this._width * 0.5f, this._height * 0.5f);
//    }
//
//    @Override
//    public boolean collidesWith(final IShape shape_){
//        if (shape_ instanceof RectangularShape){
//            final RectangularShape rectangularShape = (RectangularShape) shape_;
//            return RectangularCollisionChecker.checkCollision(this, rectangularShape);
//        } else if (shape_ instanceof Line){
//            final Line line = (Line) shape_;
//            return RectangularCollisionChecker.checkCollision(this, line);
//        } else{
//            return false;
//        }
//    }
//
//    @Override
//    protected boolean isCulled(final Camera camera_){
//        final float x = this.getX();
//        final float y = this.getY();
//        return x > camera_.getMaxX() || y > camera_.getMaxY() || x + this.getWidth() < camera_.getMinX() ||
//                y + this.getHeight() < camera_.getMinY();
//    }
//
//    @Override
//    protected void drawVertices(final GL10 gl_, final Camera camera_){
//        gl_.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
//    }
//}
