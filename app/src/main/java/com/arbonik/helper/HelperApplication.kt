package com.arbonik.helper

import android.app.Application
import android.content.Context
import android.content.Intent
import android.widget.Button
import com.arbonik.helper.auth.AuthActivity
import com.arbonik.helper.auth.SharedPreferenceUser

class HelperApplication : Application()
{
    override fun onCreate()
    {
        super.onCreate()
        globalContext = applicationContext
    }

    companion object
    {
        lateinit var globalContext : Context
    }

}