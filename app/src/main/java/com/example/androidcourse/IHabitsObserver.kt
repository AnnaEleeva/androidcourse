package com.example.androidcourse

import com.example.androidcourse.core.HabitType
import java.util.*
interface IHabitsObserver {
    val habitType: HabitType
    fun onHabitEdit(id: UUID)
    fun onHabitDelete(id: UUID)
    fun notifyDataSetHasChanged()
}