package com.dicoding.courseschedule.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

//TODO 3 : Define room database class
@Database(entities = [Course::class], version = 1)
abstract class CourseDatabase : RoomDatabase() {

    abstract fun courseDao(): CourseDao

    companion object {

        @Volatile
        private var INSTANCE: CourseDatabase? = null

        fun getInstance(context: Context): CourseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CourseDatabase::class.java,
                    "courses.db"
                ).build()
                INSTANCE = instance

                val sharedPref = context.getSharedPreferences("myPref", Context.MODE_PRIVATE)
                val isLoaded = sharedPref.getBoolean("isLoaded", false)
                if(!isLoaded){
                    sharedPref.edit().putBoolean("isLoaded", true).apply()
                    runBlocking{
                        async(Dispatchers.IO){fillWithStartingData(instance.courseDao())}.await()
                    }
                }
                instance
            }
        }
        private fun fillWithStartingData(dao: CourseDao) {
            // TODO FOR TEST
        }
    }
}
