package com.example.androidcourse

import androidx.room.TypeConverter
import com.example.androidcourse.core.HabitType
import com.example.androidcourse.core.Priority
import java.util.*

class PriorityConverter {
    @TypeConverter
    fun fromPriority(priority: Priority): Int {
        return priority.value
    }

    @TypeConverter
    fun toPriority(priority: Int): Priority {
        return Priority.getByValue(priority)
    }
}

class HabitTypeConverter {
    @TypeConverter
    fun fromHabitType(habitType: HabitType): Int {
        return habitType.value
    }

    @TypeConverter
    fun toHabitType(habitType: Int): HabitType {
        return HabitType.getByValue(habitType)
    }
}

class CalendarConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Calendar? = value?.let {
        GregorianCalendar().also { calendar ->
            calendar.timeInMillis = it
        }
    }

    @TypeConverter
    fun toTimestamp(timestamp: Calendar?): Long? = timestamp?.timeInMillis
}

class UUIDConverter {
    @TypeConverter
    fun fromUUID(value: UUID): String = value.toString()

    @TypeConverter
    fun toUUID(value: String): UUID = UUID.fromString(value)
}