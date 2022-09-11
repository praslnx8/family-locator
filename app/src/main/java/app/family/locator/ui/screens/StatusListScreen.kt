package app.family.locator.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun StatusListScreen() {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        MyStatusScreen()
        FamilyStatusScreen()
    }
}