package com.arbonik.helper.notifications

/*//import com.google.firebase.messaging.Message
import android.app.Notification
import android.app.NotificationManager
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.arbonik.helper.MainActivity
import com.arbonik.helper.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService : FirebaseMessagingService()
{
    var Preference: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

    override fun onCreate()
    {
        super.onCreate()
        Toast.makeText(this, "on create FirebaseMessagingService", Toast.LENGTH_LONG).show()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage)
    {
        if (remoteMessage.data.isNotEmpty())
        {
            *//**здесь получаю сообщение из БД, затем из объекта remoteMessage
             * извлекаю данные и отправляю соообщение на устройство*//*
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.logo))
                .setContentTitle(*//*this.getString(R.string.app_name)*//* remoteMessage.notification!!.title)
                .setContentText(remoteMessage.notification!!.body)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(1, notificationBuilder.build())
        }
        // Check if message contains a notification payload
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    override fun onNewToken(token: String)
    {
        super.onNewToken(token)
        val editor: SharedPreferences.Editor = Preference.edit()
            .putString("current_token", token)
        editor.apply() // save token (key = token)
//        if (SharedPreferenceUser.currentUser.category == USER_CATEGORY.VOLONTEER)
    }
}*/
