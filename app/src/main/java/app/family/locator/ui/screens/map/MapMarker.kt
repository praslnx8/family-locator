package app.family.locator.ui.screens.map

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import app.family.locator.custom.TextDrawable
import app.family.locator.ui.ColorGenerator
import app.family.locator.ui.UIUtils
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun MapMarker(
    position: LatLng,
    title: String,
    name: String
) {
    val icon = bitmapDescriptorFromVector(name)
    Marker(
        state = MarkerState(position = position),
        title = title,
        icon = icon,
    )
}

private fun bitmapDescriptorFromVector(
    name: String
): BitmapDescriptor? {

    // retrieve the actual drawable
    val drawable = TextDrawable.builder()
        .buildRound(UIUtils.getAvatarText(name), ColorGenerator.MATERIAL.getColor(name))
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    // draw it onto the bitmap
    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bm)
}
