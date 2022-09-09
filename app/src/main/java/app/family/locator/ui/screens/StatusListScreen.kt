package app.family.locator.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable

@Composable
fun StatusListScreen() {
    LazyColumn {
        item {
            MyStatusScreen()
        }
    }
}