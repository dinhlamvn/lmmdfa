package vn.dl.lmmdfa.database

import android.app.Application
import androidx.annotation.AnyThread
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import vn.dl.lmmdfa.entity.NoteEntity

@Database(entities = [NoteEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        private var instance: AppDatabase? = null

        private val SYNCHRONIZED = Unit

        @AnyThread
        @JvmStatic
        fun getInstance(application: Application): AppDatabase {
            synchronized(SYNCHRONIZED) {
                return instance ?: Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    "note-database"
                ).build().also { createdInstance ->
                    instance = createdInstance
                }
            }
        }
    }

}