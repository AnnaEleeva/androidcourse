package com.example.androidcourse

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.androidcourse.core.HabitType
import com.example.androidcourse.fragments.HabitListFragment


class HabitsListPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HabitListFragment.newInstance(HabitType.Good)
            else -> HabitListFragment.newInstance(HabitType.Bad)
        }
    }

    override fun getItemCount() = 2
}