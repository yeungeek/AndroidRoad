package com.yeungeek.avsample.activities.opengl.helper

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20.*
import android.opengl.GLUtils
import timber.log.Timber

object TextureHelper {
    fun loadTexture(context: Context, resourceId: Int): Int {
        val textureObjects = IntArray(1)

        glGenTextures(1, textureObjects, 0)

        if (0 == textureObjects[0]) {
            Timber.e("##### could not generate a new opengl texture object")
            return 0
        }

        val options = BitmapFactory.Options()
        options.inScaled = false

        val bitmap =
            BitmapFactory.decodeResource(context.resources, resourceId, options) ?: return 0

        // bind to the texture irequestRendern OpenGL
        glBindTexture(GL_TEXTURE_2D, textureObjects[0])

        // set filter
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        // load the bitmap into the bound texture
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0)
        glGenerateMipmap(GL_TEXTURE_2D)
        bitmap.recycle()

        glBindTexture(GL_TEXTURE_2D, 0)

        return textureObjects[0]
    }
}