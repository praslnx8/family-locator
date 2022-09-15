package app.family.locator.ui.screens.chat

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import app.family.locator.R
import app.family.locator.ui.UIUtils
import app.family.locator.ui.views.AvatarView
import app.family.presentation.models.MessageState

@Composable
fun ChatBubbleView(messageState: MessageState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.default_padding))
    ) {
        Row(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.default_padding)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarView(name = messageState.name)
            Text(
                modifier = Modifier
                    .padding(start = dimensionResource(id = R.dimen.default_padding))
                    .weight(1f),
                text = messageState.message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start
            )
            Text(
                text = UIUtils.getRelativeTime(messageState.time),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview
@Composable
private fun PreviewChatBubbleView() {
    ChatBubbleView(messageState = MessageState("Prasi", "Hello", System.currentTimeMillis()))
}