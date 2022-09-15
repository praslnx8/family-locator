package app.family.api.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.firebase.database.Exclude
import com.google.firebase.database.PropertyName

@Entity(
    tableName = "message",
    indices = [Index(value = ["sender_name", "message", "time"], unique = true)]
)
data class MessageDto(

    @Exclude
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    @ColumnInfo(name = "sender_name")
    @get:PropertyName("sender_name")
    @set:PropertyName("sender_name")
    var senderName: String = "",

    @ColumnInfo(name = "message")
    @get:PropertyName("message")
    @set:PropertyName("message")
    var message: String = "",

    @ColumnInfo(name = "time")
    @get:PropertyName("time")
    @set:PropertyName("time")
    var time: Long = 0L
)