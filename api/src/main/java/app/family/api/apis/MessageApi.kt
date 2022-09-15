package app.family.api.apis

import android.util.Log
import app.family.api.db.daos.MessageDao
import app.family.api.models.MessageDto
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

class MessageApi(
    private val familyReference: DatabaseReference,
    private val messageDao: MessageDao
) {

    fun sendMessage(
        familyId: String,
        senderId: String,
        senderName: String,
        message: String,
        time: Long
    ): Flow<Boolean> = callbackFlow {
        val messageReference = familyReference.child(familyId).child("messages")
        messageReference.push()
            .setValue(
                MessageDto(
                    senderId = senderId,
                    senderName = senderName,
                    message = message,
                    time = time
                )
            )
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

    fun listenToMessage(familyId: String): Flow<List<MessageDto>> = callbackFlow {
        val messageReference = familyReference.child(familyId).child("messages")
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("Chat API", "Message received")
                val messages = mutableListOf<MessageDto>()
                snapshot.children.forEach { messageSnapShot ->
                    messageSnapShot.getValue(MessageDto::class.java)?.let {
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
        awaitClose {
            Log.w("Message API", "Message listen closed")
            messageReference.removeEventListener(valueEventListener)
        }
    }

    fun storeMessages(messages: List<MessageDto>): Flow<Unit> = flow {
        messageDao.insertAll(messages)
        emit(Unit)
    }

    fun fetchMessages(): Flow<List<MessageDto>> {
        return messageDao.fetchMessages()
    }
}