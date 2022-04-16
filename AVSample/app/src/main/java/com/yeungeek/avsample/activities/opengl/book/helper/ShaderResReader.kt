package com.yeungeek.avsample.activities.opengl.book.helper

import android.content.res.Resources

object ShaderResReader {
    /**
     * load from asset file
     */
    fun loadFromAssetsFile(fileName: String, res: Resources): String {
        val result = StringBuilder()
        try {
            val inputStream = res.assets.open(fileName)
            var ch: Int
            val buffer = ByteArray(1024)
            while (-1 != inputStream.read(buffer).also { ch = it }) {
                result.append(String(buffer, 0, ch))
            }
        } catch (e: Exception) {
            return ""
        }
        return result.toString().replace("\\r\\n".toRegex(), "\n")
    }
}