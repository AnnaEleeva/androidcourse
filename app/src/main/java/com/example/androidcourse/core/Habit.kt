package com.example.androidcourse.core
import android.os.Parcelable
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize
import java.util.*
@Parcelize
@Entity(tableName = "habits")
class Habit(
    val name: String,
    val description: String,
    val priority: Priority = Priority.Low,
    val type: HabitType = HabitType.Good,
    val repetitions: Int = 10,
    val periodicity: Int = 2,
    val color: String = "#388E3C",
    val id: UUID = UUID.randomUUID(), //уникальный id назначается автоматически
    val creationDate: Calendar = Calendar.getInstance()
) : Parcelable {
}
