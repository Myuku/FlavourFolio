package com.example.flavourfolio.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream
object Converters {
    @TypeConverter
    @JvmStatic
    fun bitmapToBase64(bitmap: Bitmap) : String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    @TypeConverter
    fun base64ToBitmap(base64String: String):Bitmap?{
        val byteArray = Base64.decode(base64String, 0)
        if (byteArray != null) {
            return BitmapFactory.decodeByteArray(byteArray,
                0, byteArray.size)
        }
        return null
    }

}