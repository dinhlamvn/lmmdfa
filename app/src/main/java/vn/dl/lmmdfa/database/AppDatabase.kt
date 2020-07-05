package vn.dl.lmmdfa.database

import android.app.Application
import androidx.annotation.AnyThread
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import vn.dl.lmmdfa.entity.TodoEntity

@Database(entities = [TodoEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        private const val DATABASE_NAME = "todo-db"
        
        private var instance: AppDatabase? = null

        @AnyThread
        @JvmStatic
        fun getInstance(application: Application): AppDatabase {
            synchronized(this) {
                return instance ?: Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build().also { createdInstance ->
                    instance = createdInstance
                }
            }
        }
    }

}