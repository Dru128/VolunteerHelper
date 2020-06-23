package com.arbonik.helper

import android.app.Application
import android.content.Context

class HelperApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        globalContext = applicationContext
    }

    companion object{
        lateinit var globalContext : Context
    }
    fun qwer(){

    }
}