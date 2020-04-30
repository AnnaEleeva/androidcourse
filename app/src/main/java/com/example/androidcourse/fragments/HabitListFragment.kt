package com.example.androidcourse.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcourse.IHabitsObservable
import com.example.androidcourse.IHabitsObserver
import com.example.androidcourse.MyHabitRecyclerViewAdapter
import com.example.androidcourse.R
import com.example.androidcourse.core.Habit
import com.example.androidcourse.core.HabitType
import com.example.androidcourse.viewmodels.HabitsViewModel
import kotlinx.android.synthetic.main.fragment_habit_list.*
import java.util.*

private const val ARGS_HABIT_TYPE = "HABIT_TYPE"

class HabitListFragment : Fragment(), IHabitsObserver {
    private var habits: MutableList<Habit> = mutableListOf()
    private val model: HabitsViewModel by activityViewModels()

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override lateinit var habitType: HabitType

    companion object {
        fun newInstance(type: HabitType): HabitListFragment {
            val fragment = HabitListFragment()
            val bundle = Bundle()
            bundle.putSerializable(ARGS_HABIT_TYPE, type)
            fragment.arguments = bundle
            return fragment
        }
    }
//вызываем в HabitsListPagerAdapter newInstance, но когда вот это?
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_habit_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        habits = model.getHabits(habitType).toMutableList()
        viewManager = LinearLayoutManager(context)
        viewAdapter = MyHabitRecyclerViewAdapter(habits)

        habitsRecyclerView.apply {
            layoutManager = viewManager

            adapter = viewAdapter
        }
        togglePlaceHolder()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        arguments?.getSerializable(ARGS_HABIT_TYPE)?.let {
            habitType = it as HabitType
        }

        (activity as IHabitsObservable).addHabitsObserver(this)
    }
    override fun onHabitDelete(id: UUID) {
        habits.withIndex().find { it.value.id == id }?.let { (index, _) ->
            habits.removeAt(index)
            viewAdapter.notifyItemRemoved(index)
        }
    }


    override fun notifyDataSetHasChanged() {
        habits.clear()
        model.getHabits(habitType).map {
            habits.add(it)

        }
        viewAdapter.notifyDataSetChanged()
        togglePlaceHolder()
    }
    override fun onHabitEdit(id: UUID) {
        val existingHabit = habits.withIndex().find { it.value.id == id }
        if (existingHabit != null) {
            updateHabit(id, existingHabit.index)
        } else addNewHabit(id)

    }
    private fun updateHabit(id: UUID, position: Int) {
        habits[position] = model.findById(id)!!
        viewAdapter.notifyItemChanged(position)

    }
    private fun addNewHabit(id: UUID) {
        habits.add(model.findById(id)!!)
        viewAdapter.notifyItemInserted(habits.size - 1)

    }

    fun togglePlaceHolder() {
        list_empty.visibility = if (habits.isEmpty()) View.VISIBLE else View.GONE
    }
}