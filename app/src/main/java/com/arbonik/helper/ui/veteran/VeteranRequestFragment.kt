package com.arbonik.helper.ui.veteran

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.arbonik.helper.*
import com.arbonik.helper.auth.SharedPreferenceUser
import com.arbonik.helper.helprequest.RequestData
import com.arbonik.helper.helprequest.RequestManager
import com.google.firebase.firestore.FirebaseFirestore

//import com.arbonik.helper.notifications.Notification

class VeteranRequestFragment : Fragment()
{
    var requestManager = RequestManager()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        retainInstance = true // сохранение состояния при перевороте
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val root = inflater.inflate(R.layout.fragment_create_request, container, false)
        val context = root.context

        val categories = root.findViewById<Spinner>(R.id.spinner)
        val commentText = root.findViewById<TextView>(R.id.comment)
        val timeHelp = root.findViewById<TimePicker>(R.id.timeHelp)
        val datePicker = root.findViewById<DatePicker>(R.id.calendarView)
        lateinit var adapter: ArrayAdapter<String>
        timeHelp.setIs24HourView(true)
        var selected_category: String? = null


        FirebaseFirestore.getInstance().collection("TYPE_HELP") // Reference
            .get()
             .addOnSuccessListener { result ->
                 var data:MutableList<String> = mutableListOf()
                 for (document in result)
                     data.add(document.get("type").toString())
                 adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, data)
                 adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                 categories?.adapter = adapter

                 categories?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener
                 {
                     override fun onNothingSelected(parent: AdapterView<*>?) {}
                     override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long)
                     {
                                 selected_category = parent!!.getItemAtPosition(position).toString()
                     }
                 }
                 root.findViewById<ConstraintLayout>(R.id.layout)
                     .removeView(root.findViewById<ProgressBar>(R.id.progressBar))
             }

        root.findViewById<Button>(R.id.toAuth).setOnClickListener { v ->
            if (selected_category != null)
                {
                    var request = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        RequestData(
                            selected_category!!,
                            commentText.text.toString(),
                            SharedPreferenceUser.currentUser!!,
                            "${datePicker.dayOfMonth}.${datePicker.month + 1} в ${timeHelp.hour}:${timeHelp.minute}",
                            false
                        )
                    }
                    else
                    {
                        TODO("VERSION.SDK_INT < M")
                    }
                    requestManager.addRequest(request)
                    val t = Toast.makeText(HelperApplication.globalContext, "Ваша заявка принята!", Toast.LENGTH_LONG)
                    t.setGravity(Gravity.CENTER, 0, 0)
                    t.show()

                    adapter.notifyDataSetChanged()
                    //============================================
                    /*                        var message: String
                    SharedPreferenceUser.currentUser?.let {
                        message =
                            "${it.name} оставил(а) заявку:\nТип помощи: ${c.category}\n" +
                                    "Время испольнения: ${datePicker.dayOfMonth}.${datePicker.month} в ${timeHelp.hour}:${timeHelp.minute}" +
                                    "\nАдресс: ${it.address}\nТелефон: ${it.phone}"
                        if (commentText.text.isNotEmpty()) message += "\n${it.name} оставил(а) комментарий: ${commentText.text}"
                        Notification.sendNotification_server("Новая заявка!", message, Notification.TOPIC_FOR_VOLONTER)*/
                    //============================================
                }
        }
        return root
    }
}