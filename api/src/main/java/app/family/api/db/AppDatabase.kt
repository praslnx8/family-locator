package app.family.api.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.family.api.db.daos.MessageDao
import app.family.api.models.MessageDto

@Database(
    entities = [MessageDto::class],
    version = 1
)
@TypeConverters(ModelConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
}