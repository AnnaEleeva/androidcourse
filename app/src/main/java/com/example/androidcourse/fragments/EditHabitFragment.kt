package com.example.androidcourse.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.androidcourse.core.Habit
import com.example.androidcourse.R
import com.example.androidcourse.core.HabitType
import com.example.androidcourse.databinding.FragmentEditHabitBinding
import com.example.androidcourse.viewmodels.EditableHabitViewModel
import kotlinx.android.synthetic.main.fragment_edit_habit.*
import java.util.*


class EditHabitFragment : Fragment() {
    //контент
    private val model: EditableHabitViewModel by viewModels()
    //не знаю что это, что-то с данными и hml
    private lateinit var binding: FragmentEditHabitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // от Fragment сохранение состояния
        retainInstance = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
 //inflate  создает View из Layout
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_edit_habit, container, false
        )
        binding.model = model
        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        habitName.doAfterTextChanged { text -> saveHabitButton.isEnabled = !TextUtils.isEmpty(text) }
        habitName.onFocusChangeListener = View.OnFocusChangeListener { textView, hasFocus ->
            if (!hasFocus)
                habitName.error =
                    if (TextUtils.isEmpty((textView as EditText).text)) getString(
                        R.string.required_field_error
                    ) else null
        }


        habitPriority.onItemSelectedListener = model.priorityUpdater


    }

    fun update(habitType: HabitType) {
        model.type = habitType
        update()
    }

    fun update(habitId: UUID) {
        model.update(habitId)
        update()
    }

    private fun update() {
        binding.invalidateAll()

        habitTypeRadio.check(
            when (model.type) {
                HabitType.Bad -> R.id.radio_bad
                HabitType.Good -> R.id.radio_good
            }
        )

        habitPriority.setSelection(model.priority.value)
    }
    fun saveHabit() = model.saveHabit()


}