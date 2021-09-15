package kr.ryan.tenserflowwithimage.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream

/**
 * TenserflowWithImage
 * Class: ControlBitmapImage
 * Created by pyg10.
 * Created On 2021-09-02.
 * Description:
 */
suspend fun String.rotationBitmap(): Bitmap? {
    return withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
        val bounds = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeFile(this@rotationBitmap, bounds)
        val option = BitmapFactory.Options()
        val bitmap = BitmapFactory.decodeFile(this@rotationBitmap, option)

        val exif = runCatching {
            ExifInterface(this@rotationBitmap)
        }.getOrNull()
        exif?.let {
            val orientationString = it.getAttribute(ExifInterface.TAG_ORIENTATION)
            val orientation = orientationString?.toInt() ?: ExifInterface.ORIENTATION_NORMAL
            var rotationAngle = 0f
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotationAngle = 90f
                ExifInterface.ORIENTATION_ROTATE_180 -> rotationAngle = 180f
                ExifInterface.ORIENTATION_ROTATE_270 -> rotationAngle = 270f
            }
            val matrix = Matrix().apply {
                setRotate(rotationAngle, bitmap.width.toFloat() / 2, bitmap.height.toFloat() / 2)
            }
            Bitmap.createBitmap(bitmap, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true)
        }
    }

}

suspend fun Bitmap.changeBitmapScale(): Bitmap? {
    return withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
        runCatching {
            val mediateBitmap = Bitmap.createScaledBitmap(this@changeBitmapScale, 1280, 1600, false)
            val byteArrayOutputStream = ByteArrayOutputStream()
            mediateBitmap.compress(Bitmap.CompressFormat.JPEG, 95, byteArrayOutputStream)
            byteArrayOutputStream.close()
            mediateBitmap
        }.onFailure { Log.e("changeBitmap", "${it.message}") }.getOrNull()
    }

}

suspend fun Bitmap.bitmapToByteArray(): ByteArray {
    return withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        this@bitmapToByteArray.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)

        byteArrayOutputStream.toByteArray()
    }
}

suspend fun ByteArray.byteArrayToBitmap(): Bitmap {
    return withContext(CoroutineScope(Dispatchers.Default).coroutineContext) {
        BitmapFactory.decodeByteArray(this@byteArrayToBitmap, 0, size)
    }
}