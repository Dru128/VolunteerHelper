package com.arbonik.helper

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import kotlin.system.exitProcess

class HelperApplication : Application()
{
    override fun onCreate() {
        super.onCreate()
        globalContext = applicationContext
    }

    companion object
    {
        lateinit var globalContext: Context

        fun clearDataApp()
        {
            try
            {
                Runtime.getRuntime().exec("pm clear " + globalContext.packageName.toString() + " HERE")
                val intent = Intent(globalContext, MainActivity::class.java)
                val pendingIntent =
                    PendingIntent.getActivity(globalContext, 2, intent, PendingIntent.FLAG_CANCEL_CURRENT)
                val mgr = globalContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                mgr[AlarmManager.RTC, System.currentTimeMillis()] = pendingIntent
                exitProcess(0)
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }
        }
    }
}