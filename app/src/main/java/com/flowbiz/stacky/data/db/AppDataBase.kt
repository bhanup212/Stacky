package com.flowbiz.stacky.data.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.flowbiz.stacky.data.model.local.Question

@Database(entities = [Question::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao

    companion object {
        private lateinit var db: AppDataBase

        fun getDb(application: Application): AppDataBase {
            if (!::db.isInitialized) {
                db = Room.databaseBuilder(application, AppDataBase::class.java, "stacky_db").build()
            }
            return db
        }
    }
}