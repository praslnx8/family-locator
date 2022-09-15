package app.family.locator.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import app.family.locator.R
import app.family.locator.ui.ColorGenerator
import app.family.locator.ui.UIUtils
import app.family.presentation.models.MessageState

@Composable
fun ChatBubbleView(messageState: MessageState) {

    Column(modifier = Modifier.fillMaxWidth(1f)) {
        val backgroundBubbleColor = if (messageState.isCurrentUser) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        }

        val alignment = if (messageState.isCurrentUser) {
            Alignment.End
        } else {
            Alignment.Start
        }
        Surface(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.default_padding))
                .align(alignment)
                .background(backgroundBubbleColor),
            color = backgroundBubbleColor
        ) {
            if (messageState.isCurrentUser) {
                Column(
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.default_padding)),
                ) {
                    Text(
                        modifier = Modifier
                            .padding(end = dimensionResource(id = R.dimen.default_padding))
                            .align(Alignment.Start),
                        text = messageState.message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = UIUtils.getRelativeTime(messageState.time),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.End),
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.End
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.default_padding)),
                ) {
                    Text(
                        text = messageState.name,
                        color = Color(ColorGenerator.MATERIAL.getColor(messageState.name)),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        modifier = Modifier
                            .padding(end = dimensionResource(id = R.dimen.default_padding)),
                        text = messageState.message,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Start
                    )
                    Text(
                        text = UIUtils.getRelativeTime(messageState.time),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }

        }
    }
}

@Preview
@Composable
private fun PreviewChatBubbleView() {
    ChatBubbleView(
        messageState = MessageState(
            name = "Prasi",
            message = "Hello",
            time = System.currentTimeMillis(),
            isCurrentUser = false
        )
    )
}