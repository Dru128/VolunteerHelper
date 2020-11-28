package com.arbonik.helper.notifications

import android.view.textclassifier.ConversationActions
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.arbonik.helper.HelperApplication
import com.google.firebase.messaging.Constants.MessagePayloadKeys.SENDER_ID
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage


class Notification
{
    companion object
    {
        const val TOPIC_FOR_VOLONTER = "NEW_REQUEST"

        fun subscribeTopic(topic: String) // подписаться на тему
        {
                    val registrationTokens: List<String> =
                        listOf(PreferenceManager.getDefaultSharedPreferences
                            (HelperApplication.globalContext).getString("current_token", "not_found")!!)
                        if (registrationTokens[0] != "not_found")
                            FirebaseMessaging.getInstance().subscribeToTopic(/*registrationTokens.toString(),*/ topic)
//            Message.getInstance().subscribeToTopic(topic) // подписывает на тему
//                .addOnCompleteListener { task ->
//                    if (!task.isSuccessful)
//                    {
//                        /* error */
//                    }
//                }
            Toast.makeText(HelperApplication.globalContext, "subscribeTopic", Toast.LENGTH_LONG)
                .show()
        }

        fun sendNotification_server(title: String, message: String, topic: String)
        {
            //val _message = Message.builder().putData("message", message).setTopic(topic) // темa
            //    .build()
            //                .putData()
            //        val condition = "'topic1' in topics || 'topic2' in topics"
            //                .setCondition(condition) // условие
            //FirebaseMessaging.getInstance()
            //                .send(_message) // отправить сообщение, на устройства, подписанную на тему }
            val fm = FirebaseMessaging.getInstance()
            fm.send(
                RemoteMessage.Builder("$SENDER_ID@fcm.googleapis.com")
//                .setMessageId(Integer.toString(messageId))
                .addData("my_message", "Hello World")
                .addData("my_action", "SAY_HELLO")
                .build())

        }
    }
}