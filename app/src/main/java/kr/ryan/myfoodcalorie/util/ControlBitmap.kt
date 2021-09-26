package kr.ryan.myfoodcalorie.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * MyFoodCalorie
 * Class: ControlBitmap
 * Created by pyg10.
 * Created On 2021-09-26.
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