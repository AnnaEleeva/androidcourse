package com.example.androidcourse.core

import android.content.Context
import com.example.androidcourse.R

enum class HabitType(val value: Int) {
    Good(0),
    Bad(1);



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