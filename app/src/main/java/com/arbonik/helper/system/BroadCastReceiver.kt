package com.arbonik.helper.system

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsMessage
import android.widget.Toast


class Receiver : BroadcastReceiver()
{
    override fun onReceive(context: Context, intent: Intent)
    {/*
        val bundle = intent.extras
        if (bundle != null)
        {
            val pdus = bundle["pdus"] as Array<*>?
            val format = bundle.getString("format")
            val messages = arrayOfNulls<SmsMessage>(pdus!!.size)
            for (i in pdus.indices)
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray, format)
                }
                else
                {
                    messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                }
                val senderPhoneNo = messages[i]?.displayOriginatingAddress
                Toast.makeText(context, "Message " + (messages[0]?.messageBody) + ", from " + senderPhoneNo, Toast.LENGTH_SHORT).show()
            }
        }*/
    }
}