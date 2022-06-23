package io.syslogic.colorpicker.compose

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.asImageBitmap

@Composable
fun BitmapImage(bitmap: Bitmap, description: String) {
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = description
    )
}