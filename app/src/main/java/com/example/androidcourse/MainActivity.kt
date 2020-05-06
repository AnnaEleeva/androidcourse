package com.example.androidcourse

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.tabs.TabLayout
import com.example.androidcourse.core.Habit
import com.example.androidcourse.core.HabitType
import com.example.androidcourse.viewmodels.HabitsViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import com.example.androidcourse.core.EXTRA
import android.widget.RadioGroup


interface IHabitsObservable {
    fun addHabitsObserver(observer: IHabitsObserver)
}

class MainActivity : AppCompatActivity(), IHabitsObservable, NavigationView.OnNavigationItemSelectedListener {
    private lateinit var habitsPagerAdapter: HabitsListPagerAdapter
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var mToolbar: Toolbar

    private val habitsWatchersByType: MutableMap<HabitType, IHabitsObserver> = mutableMapOf()
    private val model: HabitsViewModel by viewModels()

    private lateinit var sheetBehavior: BottomSheetBehavior<View>
    private var isCollapsedFromBackPress: Boolean = false

    private val bottomSliderCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {

        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_COLLAPSED && isCollapsedFromBackPress) {
                isCollapsedFromBackPress = false
            }

            if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                val imm: InputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                val view: View = currentFocus ?: View(this@MainActivity)
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mToolbar = toolbar as Toolbar
        setSupportActionBar(mToolbar)

        habitsPagerAdapter = HabitsListPagerAdapter(this)
        pager.adapter = habitsPagerAdapter

        TabLayoutMediator(tab_layout, pager) { tab, position ->
            tab.text = if (position == 0) getString(R.string.tab_name_good) else getString(R.string.tab_name_bad)
        }.attach()

        var currentHabitType = HabitType.Good
        tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                currentHabitType = if (tab?.position == 0) HabitType.Good else HabitType.Bad
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                currentHabitType = if (tab?.position == 0) HabitType.Good else HabitType.Bad
            }
        })

        addHabitButton.setOnClickListener {
            val sendIntent = Intent(applicationContext, EditHabitActivity::class.java)
            sendIntent.putExtra(EXTRA.HABIT_TYPE, currentHabitType.value)
            startActivity(sendIntent)
        }

        nameRadio.setOnCheckedChangeListener(nameSortListener)
        periodicityRadio.setOnCheckedChangeListener(periodicitySortListener)

        sheetBehavior = BottomSheetBehavior.from(filterBottomSheet)
        sheetBehavior.addBottomSheetCallback(bottomSliderCallback)
        bottom_sheet_header.setOnClickListener {
            if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        searchEdit.doAfterTextChanged { text -> model.searchWord = text.toString() }
        searchEdit.setText(model.searchWord)

        drawerToggle = ActionBarDrawerToggle(this, navDrawerLayout, mToolbar, R.string.open_drawer, R.string.close_drawer)
        drawerToggle.isDrawerIndicatorEnabled = true
        navDrawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        navDrawer.setNavigationItemSelectedListener(this)
    }

    override fun addHabitsObserver(observer: IHabitsObserver) {
        habitsWatchersByType[observer.habitType] = observer
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_about -> startActivity(Intent(applicationContext, AboutActivity::class.java))
        }

        navDrawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (sheetBehavior.state != BottomSheetBehavior.STATE_COLLAPSED) {
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            isCollapsedFromBackPress = true
        } else {
            super.onBackPressed()
        }
    }

    private val periodicitySortListener = RadioGroup.OnCheckedChangeListener { _, checkedId ->
        when (checkedId) {
            R.id.radio_periodicity_asc -> model.sortBy(Habit::periodicity)
            R.id.radio_periodicity_desc -> model.sortByDesc(Habit::periodicity)
            R.id.radio_periodicity_none -> model.clearSortBy(Habit::periodicity)
        }
    }

    private val nameSortListener = RadioGroup.OnCheckedChangeListener { _, checkedId ->
        when (checkedId) {
            R.id.radio_name_asc -> model.sortBy(Habit::name)
            R.id.radio_name_desc -> model.sortByDesc(Habit::name)
            R.id.radio_name_none -> model.clearSortBy(Habit::name)
        }
    }
}