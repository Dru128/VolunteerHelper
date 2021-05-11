package com.arbonik.helper.auth

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import com.arbonik.helper.MainActivity
import com.arbonik.helper.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse


class AuthActivity : AuthBase()
{
    private val RC_SIGNIN: Int = 1635769

    override fun onCreate(savedInstanceState: Bundle?)
    {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        authUser()
    }

    private fun authUser()
    {
        if (sharedPreferenceUser.checkAuth())
        {
            // already signed in
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        else
        {
            // not signed in
            val providers = arrayListOf(AuthUI.IdpConfig.PhoneBuilder().build())
            startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers)//.setIsSmartLockEnabled(false)
                    .build(), RC_SIGNIN
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGNIN)
        {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK)
            {
                // Successfully signed in
                val user = auth.currentUser?.uid
                getDataUser(user!!)

            } else
            {
                AlertDialog.Builder(this)
                    .setMessage("ошибка: ${response?.error?.errorCode}")
                    .show()
            }
        }
    }
}

