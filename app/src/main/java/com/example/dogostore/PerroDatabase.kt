package com.example.dogostore

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database (entities = [Perro::class], version = 1, exportSchema = false)
abstract class PerroDatabase : RoomDatabase() {

    abstract fun perroDao(): PerroDao

    companion object {
        @Volatile
        private var INSTANCE: PerroDatabase? = null

        fun getDatabase(context: Context): PerroDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PerroDatabase::class.java,
                    "dogostore_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}