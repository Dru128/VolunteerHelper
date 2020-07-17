package com.arbonik.helper

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.arbonik.helper.helprequest.RequestData
import com.arbonik.helper.helprequest.RequestManager
import com.arbonik.helper.auth.SharedPreferenceUser
import com.arbonik.helper.auth.SignIn
import com.arbonik.helper.auth.User
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
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_request, R.id.navigation_notifications
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
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
