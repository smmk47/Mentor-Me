package com.example.myapplication1


import android.content.ContentResolver
import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.widget.Toast

class ScreenshotObserver(
    private val contentResolver: ContentResolver,
    private val context: Context,
    handler: Handler
) : ContentObserver(handler) {

    companion object {
        private const val SCREENSHOTS_DIR = "Screenshots"
    }

    private val screenshotUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)
        if (isScreenshot(uri)) {
            // Notify that a screenshot has been taken
            showToast()
        }
    }

    private fun isScreenshot(uri: Uri?): Boolean {
        uri ?: return false
        val path = uri.path ?: return false
        return path.contains(SCREENSHOTS_DIR, ignoreCase = true)
    }

    private fun showToast() {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(
                context,
                "Screenshot captured!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
