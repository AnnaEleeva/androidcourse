package com.example.androidcourse.core

import android.graphics.Color

enum class Priority(val value: Int) {
    Low(0),
    Middle(1),
    High(2);

    companion object {
        private val values = values();
        fun getByValue(value: Int): Priority {
            return values.firstOrNull { it.value == value }
                ?: throw Exception("Class Priority doesn't contain value $value")
        }

        fun getColor(value: Int): String {
            when (value) {
                0 -> return "#ffd95b"
                1 -> return "#fb8c00"
                else -> return "#f4511e"
            }
        }
    }

}