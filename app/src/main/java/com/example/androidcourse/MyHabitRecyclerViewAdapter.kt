package com.example.androidcourse

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcourse.core.EXTRA
import com.example.androidcourse.core.Habit
import com.example.androidcourse.core.Priority
import kotlinx.android.synthetic.main.fragment_habit.view.*
import java.util.*

/**
 * [RecyclerView.Adapter] that can display a [Habit]
 */
class MyHabitRecyclerViewAdapter(
    private val habits: List<Habit>
) : RecyclerView.Adapter<MyHabitRecyclerViewAdapter.HabitViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_habit, parent, false)
        return HabitViewHolder(view).listen { position, _ ->
            val habit = habits[position];
            val sendIntent = Intent(view.context, EditHabitActivity::class.java)
            sendIntent.putExtra(EXTRA.NEW_HABIT, habit)
            view.context.startActivity(sendIntent)
        }
    }

    override fun onBindViewHolder(holder: HabitViewHolder, position: Int) {
        holder.bind(habits[position])
    }

    override fun getItemCount(): Int = habits.size

    inner class HabitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), OnItemClickListener {
        private val nameView: TextView = itemView.name
        private val descriptionView: TextView = itemView.description
        private val lowPriorityView: View = itemView.priorityLow
        private val lowPriorityViewBottom: View = itemView.priorityBottom
        private val periodicityView: TextView = itemView.periodicity
        private val repeatsView: TextView = itemView.repeats
        private val typeView: TextView = itemView.type
        private val priorityView: TextView=itemView.priority
        private val context = itemView.context
        private var habitId: UUID? = null


        override fun onClick(view: View?, position: Int) {

        }

        override fun toString(): String {
            return super.toString() + "${nameView.text} (id: ${habitId})"
        }

        fun bind(habit: Habit) {
            habitId = habit.id
            nameView.text = habit.name
            descriptionView.text = habit.description
            val color_text=Color.parseColor(Priority.getColor(habit.priority.value))
            lowPriorityView.setBackgroundColor(color_text)
            lowPriorityViewBottom.setBackgroundColor(color_text)

            val periodicity = "Период(дней): "+habit.periodicity
            periodicityView.text = periodicity
            val repeats = "Повторения: "+habit.repetitions
            repeatsView.text = repeats
            val priority = "Приоритет: "+habit.priority.name
            priorityView.text = priority
            typeView.text = habit.type.toLocalString(context)
        }
    }
}

interface OnItemClickListener {
    fun onClick(view: View?, position: Int)
}

fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, type: Int) -> Unit): T {
    itemView.setOnClickListener {
        event.invoke(adapterPosition, itemViewType)
    }
    return this
}