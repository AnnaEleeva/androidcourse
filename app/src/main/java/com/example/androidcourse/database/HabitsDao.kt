package com.example.androidcourse.database

import com.example.androidcourse.core.Habit
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.androidcourse.UUIDConverter
import java.util.*

@Dao
@TypeConverters(UUIDConverter::class)
interface HabitsDao {
    @get:Query("SELECT * FROM habits")
    val habits: LiveData<List<Habit>>?

    @Query("SELECT * FROM habits WHERE id=:id ")
    fun findById(id: UUID): Habit?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(habit: Habit): Long

    @Update
    fun update(habit: Habit)

    @Transaction
    fun upsert(habit: Habit) {
        val id: Long = insert(habit)
        if (id == -1L) {
            update(habit)
        }
    }
}