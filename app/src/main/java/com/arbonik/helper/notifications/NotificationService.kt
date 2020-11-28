package com.arbonik.helper.notifications
/*
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.arbonik.helper.MainActivity
import com.arbonik.helper.R
import com.arbonik.helper.auth.SharedPreferenceUser
import com.arbonik.helper.auth.USER_CATEGORY
import com.arbonik.helper.helprequest.RequestData
import com.arbonik.helper.helprequest.RequestManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore


class NotificationService : Service()
{
    private var NOTIFVATION_ID: Int = 2

    override fun onBind(intent: Intent): IBinder?
    {
        Log.i("LOG_TAG", "onBind")
        return null
    }

    override fun onCreate()
    {
        startForegroundServise()
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int
    {
        updateListener()
            //Toast.makeText(this, "onStartCommand", Toast.LENGTH_LONG).show()
        return START_STICKY
//        return super.onStartCommand(intent, flags, startId)
    }

    private fun updateListener()
    {
       val db = FirebaseFirestore.getInstance()
    //    Toast.makeText(HelperApplication.globalContext, "updateListener", Toast.LENGTH_SHORT).show()
        db.collection(RequestManager.REQUEST_TAG).addSnapshotListener { data, error ->
            if (error != null) return@addSnapshotListener
            if (data != null)
            {
                for (change in data.documentChanges)
                {
                    when (change.type)
                    {
                        DocumentChange.Type.ADDED ->
                        {
                            val request = change.document.toObject(RequestData::class.java)
                            if (SharedPreferenceUser.currentUser?.category == USER_CATEGORY.VOLONTEER)
                            {
                                var message: String
                                request.let {
                                    message =
                                        "${it.master.name} оставил(а) заявку:\nТип помощи: ${it.title}\n" +
                                                "Время испольнения: ${it.date}\nАдресс: ${it.master.address}\nТелефон: ${it.master.phone}"
                                    if (request.comment.isNotEmpty()) message += "\n${it.master.name} оставил(а) комментарий: ${it.comment}"
                                }
                           //     Toast.makeText(HelperApplication.globalContext, "Новая завявка!", Toast.LENGTH_SHORT).show()
                                  sendNotification(getString(R.string.notify_for_volonter), message)
                            }
                        }
                        DocumentChange.Type.MODIFIED ->
                        {
                            val request = change.document.toObject(RequestData::class.java)
                            if (SharedPreferenceUser.currentUser?.category == USER_CATEGORY.VETERAN
                                && request.master.uid == SharedPreferenceUser.currentUser?.uid && request.status)
                            {
                              //  Toast.makeText(HelperApplication.globalContext, "завявка принята!", Toast.LENGTH_SHORT).show()
                                  sendNotification(getString(R.string.notify_for_veteran), "Волонтёр ... принял вашу заявку")
                            }
                        }
                        DocumentChange.Type.REMOVED -> change.document.toObject(RequestData::class.java)
                    }
                }
            }
        }
    }

    private fun sendNotification(title: String, text: String)
    {

        val resultIntent = Intent(this, MainActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(
            this, 0, resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
//            .setDefaults(Notification.DEFAULT_ALL)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFVATION_ID, builder.build())

        NOTIFVATION_ID++
    }

    private fun startForegroundServise()
    {

        val resultIntent = Intent(this, MainActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(
            this, 0, resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("Сервис запущен!")
            .setContentText("Сервис запущен!")
            .setContentIntent(resultPendingIntent)
            .setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)

        startForeground(1, builder.build())
    }

    override fun onDestroy()
    {
        super.onDestroy()
    }
}*/