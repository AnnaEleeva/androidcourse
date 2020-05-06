package com.example.androidcourse.database
import com.example.androidcourse.core.Habit
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Habit::class], version = 1)
abstract class HabitsDatabase : RoomDatabase() {
    abstract fun habitsDao(): HabitsDao

    companion object {
        private var INSTANCE: HabitsDatabase? = null

        fun getInstance(context: Context): HabitsDatabase? {
            if (INSTANCE == null) {
                synchronized(HabitsDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        HabitsDatabase::class.java, "habits.db"
                    )
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}