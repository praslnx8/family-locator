package app.family.locator.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.family.locator.ui.ColorGenerator

@Composable
fun AvatarView(
    name: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium
) {
    Box(contentAlignment = Alignment.Center,
        modifier = modifier
            .background(color = Color(ColorGenerator.MATERIAL.getColor(name)), shape = CircleShape)
            .layout() { measurable, constraints ->
                // Measure the composable
                val placeable = measurable.measure(constraints)

                //get the current max dimension to assign width=height
                val currentHeight = placeable.height
                val currentWidth = placeable.width
                val newDiameter = maxOf(currentHeight, currentWidth)

                //assign the dimension and the center position
                layout(newDiameter, newDiameter) {
                    // Where the composable gets placed
                    placeable.placeRelative(
                        (newDiameter - currentWidth) / 2,
                        (newDiameter - currentHeight) / 2
                    )
                }
            }) {

        Text(
            text = getAvatarText(name),
            textAlign = TextAlign.Center,
            style = textStyle,
            color = Color.White,
            modifier = Modifier.padding(4.dp),
        )
    }
}

private fun getAvatarText(name: String): String {
    val stringBuilder = StringBuilder()
    name.split(" ").forEach {
        stringBuilder.append(it.firstOrNull() ?: "")
    }
    return stringBuilder.toString()
}

@Preview
@Composable
private fun PreviewAvatarView() {
    AvatarView(name = "Prasi")
}