package com.arbonik.helper


import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.arbonik.helper.HelperApplication.Companion.globalContext
import com.arbonik.helper.notifications.NotificationService


class ActivationDevice : BroadcastReceiver()
{
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) /** вызывается после загрузки телефона*/
    {
        //Log.d("on", "start!")
        Toast.makeText(context.applicationContext, "Загрузка завершена", Toast.LENGTH_LONG).show()
//        context.startActivity(Intent(context, MainActivity::class.java))
        context.startService(Intent(context, NotificationService::class.java))
    }
}