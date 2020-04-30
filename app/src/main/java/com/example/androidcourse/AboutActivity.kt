package com.example.androidcourse

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerToggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.title = getString(R.string.aboutScreen_barTitle)
        drawerToggle = ActionBarDrawerToggle(this, navDrawerLayout, toolbar as Toolbar, R.string.open_drawer, R.string.close_drawer)
        drawerToggle.isDrawerIndicatorEnabled = true
        navDrawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        navDrawer.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_home -> startActivity(Intent(applicationContext, MainActivity::class.java))
        }

        navDrawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}