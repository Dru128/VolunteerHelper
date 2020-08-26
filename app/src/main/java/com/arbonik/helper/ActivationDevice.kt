package com.arbonik.helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class ActivationDevice : BroadcastReceiver()
{
    override fun onReceive(context: Context, intent: Intent) /** вызывается после загрузки телефона*/
    {
        if (intent.action.equals(Intent.ACTION_BOOT_COMPLETED))
        {
//            Toast.makeText(context, "Загрузка завершена", Toast.LENGTH_LONG).show()
            context.startActivity(Intent(context, MainActivity::class.java))
//            if (SharedPreferenceUser.currentUser!!.notification!!) context.startService(Intent(context, NotificationService::class.java))
        }
    }
}