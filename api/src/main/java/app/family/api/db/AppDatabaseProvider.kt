package app.family.api.db

import android.content.Context
import androidx.room.Room

class AppDatabaseProvider(private val context: Context) {

    fun getDatabase(): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, "family_db")
            .build()
    }
}
