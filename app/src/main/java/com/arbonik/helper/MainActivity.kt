package com.arbonik.helper

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.arbonik.helper.helprequest.RequestData
import com.arbonik.helper.helprequest.RequestManager
import com.arbonik.helper.auth.SharedPreferenceUser
import com.arbonik.helper.auth.SignIn
import com.arbonik.helper.auth.USER_CATEGORY
import com.arbonik.helper.auth.User
import com.google.common.graph.Graph
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    var sharedPreferenceUser = SharedPreferenceUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!sharedPreferenceUser.checkAuth()){
            startActivity(Intent(this, SignIn::class.java))
            finish()
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        val inflater = navController.navInflater
        val graph = when (SharedPreferenceUser.currentUser?.category){
            USER_CATEGORY.VETERAN -> {
                navView.inflateMenu(R.menu.bottom_nav_menu)
                inflater.inflate(R.navigation.mobile_navigation)
            }
            USER_CATEGORY.VOLONTEER -> {
                navView.inflateMenu(R.menu.bottom_nav_menu_vol)
                inflater.inflate(R.navigation.mobile_navigation_volonteer)
            }
            USER_CATEGORY.ADMIN -> TODO()
            null -> TODO()
        }

        navController.graph = graph
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_request, R.id.navigation_notifications
            )
        )
//        navHostFragment.navController.
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
        navView.setupWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_options_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when(item.itemId){
                R.id.logout -> {
                    sharedPreferenceUser.loginOut()
                    startActivity(Intent(this, SignIn::class.java))
                    finish()
                }
                R.id.logCheck -> {

                }
            }
        return super.onOptionsItemSelected(item)
    }
}
