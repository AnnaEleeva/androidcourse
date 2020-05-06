package com.example.androidcourse

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.androidcourse.core.EXTRA
import com.example.androidcourse.core.HabitType
import com.example.androidcourse.core.Habit
import com.example.androidcourse.fragments.EditHabitFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_edit_habit.*
import com.example.androidcourse.viewmodels.EditableHabitViewModel
import java.util.*

class EditHabitActivity : AppCompatActivity() {
    private lateinit var editHabitFragment: EditHabitFragment;
    private val model: EditableHabitViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_habit)
        editHabitFragment =
            supportFragmentManager.findFragmentById(R.id.editHabitView) as EditHabitFragment;
        saveHabitButton.apply {
            setOnClickListener {
                editHabitFragment.saveHabit()
                val sendIntent = Intent(applicationContext, MainActivity::class.java)
                startActivity(sendIntent)
            }
        }

        fillForEdit()
    }

    private fun fillForEdit() {
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);

        if (!intent.hasExtra(EXTRA.HABIT_ID)) {
            supportActionBar?.title = getString(R.string.newHabitActivity_barTitle)
            val type = intent.getIntExtra(EXTRA.HABIT_TYPE, 0)
            editHabitFragment.update(HabitType.getByValue(type))
            return
        }

        supportActionBar?.title = getString(R.string.editHabitActivity_barTitle)

        val habitId = intent.getSerializableExtra(EXTRA.HABIT_ID) ?: return
        editHabitFragment.update(habitId as UUID)
    }
}