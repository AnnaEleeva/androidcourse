package com.example.androidcourse.core

import android.content.Context
import com.example.androidcourse.R

enum class HabitType(val value: Int) {
    Good(0),
    Bad(1);

    companion object {
        private val values = values();

        fun getByValue(value: Int): HabitType {
            return values.firstOrNull { it.value == value }
                ?: throw Exception("Class HabitType doesn't contain value $value")
        }
    }



    fun toLocalString(context: Context): String {
        return context.getString(getStringId())
    }

    private fun getStringId(): Int {
        return when (this) {
            Bad -> R.string.bad
            Good -> R.string.good
        }
    }
}