package app.family.locator.utils

import android.content.Context
import android.content.Intent

object ShareUtils {
    fun shareInviteKey(context: Context, inviteKey: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, inviteKey)
            type = "text/plain"
        }
        context.startActivity(sendIntent)
    }
}