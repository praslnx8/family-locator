package app.family.api.apis

import android.util.Log
import app.family.api.models.MessageDto
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class MessageApi(private val familyReference: DatabaseReference) {

    fun sendMessage(
        familyId: String,
        senderName: String,
        message: String,
        time: Long
    ): Flow<Boolean> = callbackFlow {
        val messageReference = familyReference.child(familyId).child("messages")
        messageReference.push().setValue(MessageDto(senderName, message, time))
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.i("Chat Api", "Success sending message")
                    trySend(true)
                } else {
                    Log.e("Chat Api", "Error sending message " + it.exception?.message)
                    trySend(false)
                }
            }
        awaitClose()
    }

    fun listenToMessages(familyId: String): Flow<List<MessageDto>> = callbackFlow {
        val messageReference = familyReference.child(familyId).child("messages")
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("Chat API", "Message received")
                val messages = mutableListOf<MessageDto>()
                snapshot.children.forEach { snapshot ->
                    snapshot.getValue(MessageDto::class.java)?.let {
                        messages.add(it)
                    }
                }
                trySend(messages)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Chat API", "Listening to chat cancelled " + error.message)
            }
        }
        messageReference.limitToLast(100).addValueEventListener(valueEventListener)
        awaitClose { messageReference.removeEventListener(valueEventListener) }
    }
}