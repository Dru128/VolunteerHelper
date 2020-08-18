package com.arbonik.helper.notifications

import android.app.IntentService
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.arbonik.helper.HelperApplication
import com.arbonik.helper.MainActivity
import com.arbonik.helper.R
import com.arbonik.helper.auth.SharedPreferenceUser
import com.arbonik.helper.auth.USER_CATEGORY
import com.arbonik.helper.helprequest.RequestData
import com.arbonik.helper.helprequest.RequestManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore


class NotificationService(name: String) : IntentService(name)
{
    val db = FirebaseFirestore.getInstance()

    override fun onBind(intent: Intent): IBinder
    {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d("LOG_TAG", "onHandleIntent end ")
    }

    override fun onCreate()
    {
        Log.d("LOG_TAG", "onCreate")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int
    {
        updateListener()
        return super.onStartCommand(intent, flags, startId)
    }

    fun updateListener()
    {
        Log.d("LOG_TAG", "start Listener")

        db.collection(RequestManager.REQUEST_TAG).addSnapshotListener { data, error ->
            if (error != null) return@addSnapshotListener
            if (data != null && SharedPreferenceUser.currentUser?.category == USER_CATEGORY.VOLONTEER)
            {
                for (change in data.documentChanges)
                {
                    when (change.type)
                    {
                        DocumentChange.Type.ADDED ->
                        {
                            sendNotification("Появилась новая заявка!", change.document.toObject(RequestData::class.java))
                        }
                        DocumentChange.Type.MODIFIED ->  change.document.toObject(RequestData::class.java)
                        DocumentChange.Type.REMOVED -> change.document.toObject(RequestData::class.java)
                    }
                }
            }
              //  Toast.makeText(HelperApplication.globalContext, "Новая завявка!", Toast.LENGTH_SHORT).show()

            }
        }




    fun sendNotification(title: String, request: RequestData)
    {
        var message: String
        val resultIntent = Intent(this, MainActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(
            this, 0, resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        request.let{
            message = "${it.master.name} оставил(а) заявку!\nТип помощи: ${it.title}\n" +
                    "Время испольнения: ${it.date}\nАдресс: ${it.master.address}\nТелефон: ${it.master.phone}"
            if (request.comment.isNotEmpty()) message += "\n${it.master.name} оставил(а) комментарий: ${it.comment}"
        }

        val builder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(resultPendingIntent)
        Notification.DEFAULT_ALL

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, builder.build()) // вынестси ид в переменную
    }

    override fun onDestroy()
    {
        super.onDestroy()
    }
}