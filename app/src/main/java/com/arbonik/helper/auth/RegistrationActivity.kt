package com.arbonik.helper.auth

import android.os.Bundle
import androidx.navigation.Navigation
import com.arbonik.helper.R


class RegistrationActivity : AuthBase()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        var navController = Navigation.findNavController(this, R.id.nav_host_fragment_auth)
    }
}


