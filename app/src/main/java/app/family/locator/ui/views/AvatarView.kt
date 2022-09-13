package app.family.locator.ui.views

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import app.family.locator.ui.ColorGenerator

@Composable
fun AvatarView(name: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier
            .drawBehind {
                drawCircle(
                    color = Color(ColorGenerator.MATERIAL.getColor(name)),
                    radius = this.size.maxDimension
                )
            },
        text = getAvatarText(name),
        style = MaterialTheme.typography.body1
    )
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