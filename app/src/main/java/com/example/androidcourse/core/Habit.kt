package com.example.androidcourse.core

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.androidcourse.CalendarConverter
import com.example.androidcourse.HabitTypeConverter
import com.example.androidcourse.PriorityConverter
import com.example.androidcourse.UUIDConverter
import java.util.*

@Entity(tableName = "habits")
@TypeConverters(PriorityConverter::class, HabitTypeConverter::class, CalendarConverter::class, UUIDConverter::class)
class Habit(
    val name: String,
    val description: String,
    val priority: Priority = Priority.Low,
    val type: HabitType = HabitType.Good,
    val repetitions: Int = 10,
    val periodicity: Int = 2,
    val color: String = "#388E3C",
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val creationDate: Calendar = Calendar.getInstance()
)