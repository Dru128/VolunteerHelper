package com.arbonik.helper

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.arbonik.helper.auth.SharedPreferenceUser
import com.arbonik.helper.auth.AuthActivity
import com.arbonik.helper.auth.USER_CATEGORY
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity()
{
    var sharedPreferenceUser = SharedPreferenceUser()
    private lateinit var appBarConfiguration: AppBarConfiguration


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        if (!sharedPreferenceUser.checkAuth())
        {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout_activity_main)
        val drawerNavView: NavigationView = findViewById(R.id.drawer_nav_view_activity_main)
        val bottomNavView: BottomNavigationView = findViewById(R.id.bottom_nav_view)

        val navController = findNavController(R.id.nav_host_fragment_main)
        val inflater = navController.navInflater
        val graph = when (SharedPreferenceUser.currentUser?.category)
        {
            USER_CATEGORY.VETERAN ->
            {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                bottomNavView.inflateMenu(R.menu.bottom_nav_menu_vet)
                inflater.inflate(R.navigation.bottom_navigation_veteran)
            }
            USER_CATEGORY.VOLONTEER ->
            {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                bottomNavView.inflateMenu(R.menu.bottom_nav_menu_vol)
                inflater.inflate(R.navigation.bottom_navigation_volonteer)
            }
            USER_CATEGORY.ADMIN ->
            {
                drawerNavView.inflateMenu(R.menu.drawer_nav_menu_admin)
                bottomNavView.inflateMenu(R.menu.bottom_nav_menu_admin)

                appBarConfiguration = AppBarConfiguration( setOf(
                    R.id.navigation_volonteer_fragment, R.id.navigation_request_vol, R.id.navigation_notifications_vol
                ), drawerLayout)
                setupActionBarWithNavController(navController, appBarConfiguration)
                drawerNavView.setupWithNavController(navController)

                inflater.inflate(R.navigation.bottom_navigation_admin)
            }
            else -> TODO()
        }

        navController.graph = graph
        bottomNavView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean
    {
        val navController = findNavController(R.id.nav_host_fragment_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        menuInflater.inflate(R.menu.main_activity_options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
            when(item.itemId)
            {
                R.id.logout ->
                {
                    sharedPreferenceUser.loginOut()
                    startActivity(Intent(this, AuthActivity::class.java))
                    finish()
                }
//                android.R.id.home -> onBackPressed() // эмуляция кнопки назад
            }
        return super.onOptionsItemSelected(item)
    }
}
