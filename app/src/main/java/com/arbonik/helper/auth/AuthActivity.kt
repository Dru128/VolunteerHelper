package com.arbonik.helper.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arbonik.helper.MainActivity
import com.google.firebase.auth.FirebaseAuth

open class AuthActivity : AppCompatActivity() {
    val mAuth = FirebaseAuth.getInstance()
    val sharedPreferenceUser = SharedPreferenceUser()
    val userDataFirebase = UserDataFirebase()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(sharedPreferenceUser.checkAuth()){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}