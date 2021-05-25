package com.arbonik.helper.auth

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseUser


open class AuthBase : AppCompatActivity()
{
    var authUI: FirebaseUser? = null
        @SuppressLint("RestrictedApi") get() = AuthUI.getInstance().auth.currentUser

    var sharedPreferenceUser = SharedPreferenceUser()
}