package com.immediatus.graphics.utils

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLException
import android.os.Build

import javax.microedition.khronos.opengles.GL10
import javax.microedition.khronos.opengles.GL11

import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.IntBuffer

case class GL11Wrapper(gl: GL11) {
//    private static final int[] HARDWAREBUFFERID_CONTAINER = new int[1]

    private var _currentHardwareBufferID = -1


//    def bindBuffer(final GL11 gl11_, final int hardwareBufferID_){
//        if (_currentHardwareBufferID != hardwareBufferID_){
//            _currentHardwareBufferID = hardwareBufferID_;
//            gl11_.glBindBuffer(GL11.GL_ARRAY_BUFFER, hardwareBufferID_);
//        }
//    }
//
//    def deleteBuffer(final GL11 gl11_, final int hardwareBufferID_){
//        HARDWAREBUFFERID_CONTAINER[0] = hardwareBufferID_;
//        gl11_.glDeleteBuffers(1, OpenGLWrapper.HARDWAREBUFFERID_CONTAINER, 0);
//    }
//
//
//    def texCoordZeroPointer(final GL11 gl11_){
//        gl11_.glTexCoordPointer(2, GL10.GL_FLOAT, 0, 0);
//    }
//
//    def vertexZeroPointer(final GL11 gl11_){
//        gl11_.glVertexPointer(2, GL10.GL_FLOAT, 0, 0);
//    }
//
//    def bufferData(final GL11 gl11_, final ByteBuffer byteBuffer_, final int usage_){
//        gl11_.glBufferData(GL11.GL_ARRAY_BUFFER, byteBuffer_.capacity(), byteBuffer_, usage_);
//    }

}
