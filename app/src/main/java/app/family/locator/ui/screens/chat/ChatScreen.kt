package app.family.locator.ui.screens.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import app.family.locator.R
import app.family.presentation.models.MessageViewState
import app.family.presentation.vms.ChatViewModel

@Composable
fun ChatScreen(viewModel: ChatViewModel = hiltViewModel()) {
    val chatMessageViewState =
        viewModel.listenToMessage().collectAsState(initial = MessageViewState())
    val messageTextState = rememberSaveable(Unit) { mutableStateOf("") }

    Column {
        Box(modifier = Modifier.weight(1f)) {
            LazyColumn(
                reverseLayout = true
            ) {
                items(items = chatMessageViewState.value.messages, itemContent = { item ->
                    ChatBubbleView(messageState = item)
                })
            }
        }
        Row(modifier = Modifier.padding(dimensionResource(id = R.dimen.default_padding))) {
            TextField(
                label = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Enter Message")
                    }
                },
                maxLines = 1,
                value = messageTextState.value,
                onValueChange = {
                    messageTextState.value = it
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrect = true,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Send,
                ),
                keyboardActions = KeyboardActions(onSend = {
                    if (messageTextState.value.isNotBlank()) {
                        viewModel.addMessage(messageTextState.value)
                        messageTextState.value = ""
                    }
                }),
            )
        }
    }
}