package app.family.api.db.daos

import android.database.sqlite.SQLiteDatabase
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import app.family.api.models.MessageDto
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Insert(onConflict = SQLiteDatabase.CONFLICT_REPLACE)
    suspend fun insertAll(messages: List<MessageDto>)

    @Query("SELECT * FROM `message` ORDER BY time DESC LIMIT 50")
    fun fetchMessages(): Flow<List<MessageDto>>
}
