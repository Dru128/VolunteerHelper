package com.arbonik.helper.auth

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

open class AuthActivity : AppCompatActivity() {
    val mAuth = FirebaseAuth.getInstance()
    val sharedPreferenceUser = SharedPreferenceUser()
    val userDataFirebase = UserDataFirebase()
}