package com.example.androidcourse.viewmodels


import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import androidx.lifecycle.ViewModel
import com.example.androidcourse.R
import com.example.androidcourse.core.Habit
import com.example.androidcourse.core.HabitType
import com.example.androidcourse.core.Priority
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.androidcourse.database.HabitsDatabase
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class EditableHabitViewModel(application: Application) : AndroidViewModel(application) {
    private val db = HabitsDatabase.getInstance(getApplication<Application>().applicationContext)
    private val habitsDao = db?.habitsDao()

    var name: String = ""
    var description: String = ""
    var priority: Priority = Priority.Low
    var type: HabitType = HabitType.Good
    var repetitions: Int? = null
    var periodicity: Int? = null
    var color: String = "#388E3C"
    private var id: UUID = UUID.randomUUID()
    var creationDate: Calendar = Calendar.getInstance()

    var stringRepetitions: String
        get() = repetitions?.toString() ?: ""
        set(value) {
            if (TextUtils.isEmpty(value))
                return
            val int = Integer.parseInt(value)
            if (int != repetitions)
                repetitions = int
        }

    var stringPeriodicity: String
        get() = periodicity?.toString() ?: ""
        set(value) {
            if (TextUtils.isEmpty(value))
                return
            val int = Integer.parseInt(value)
            if (int != periodicity)
                periodicity = int
        }

    fun setType(checkedRadioId: Int) {
        type = when (checkedRadioId) {
            R.id.radio_bad -> HabitType.Bad
            R.id.radio_good -> HabitType.Good
            else -> throw Exception("You forgot to create new HabitType or handle it here")
        }
    }

    fun update(habitId: UUID) {
        val habit = habitsDao?.findById(habitId) ?: return
        name = habit.name
        description = habit.description
        priority = habit.priority
        type = habit.type
        repetitions = habit.repetitions
        periodicity = habit.periodicity
        color = habit.color
        creationDate = habit.creationDate
        id = habit.id
    }

    private fun getHabit(): Habit {
        return Habit(
            name,
            description,
            priority,
            type,
            repetitions ?: 10,
            periodicity ?: 10,
            color,
            id,
            creationDate
        )
    }

    fun saveHabit() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { habitsDao?.upsert(getHabit()) }
        }
    }

    val priorityUpdater = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (position != priority.value)
                priority = Priority.getByValue(position)
        }
    }
}