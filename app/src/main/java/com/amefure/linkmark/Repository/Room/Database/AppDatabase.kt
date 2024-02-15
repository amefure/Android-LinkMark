package com.amefure.linkmark.Repository.Room.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.amefure.linkmark.Repository.Room.Dao.CategoryDao
import com.amefure.linkmark.Repository.Room.Dao.LocatorDao
import com.amefure.linkmark.Model.Category
import com.amefure.linkmark.Model.DateConverters
import com.amefure.linkmark.Model.Locator

@Database(entities = [Category::class, Locator::class], version = 1, exportSchema = false)
@TypeConverters(DateConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun locatorDao(): LocatorDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "linkmark_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}