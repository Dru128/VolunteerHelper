package com.arbonik.helper.ui.veteran

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arbonik.helper.*
import com.arbonik.helper.auth.SharedPreferenceUser
import com.arbonik.helper.helprequest.RequestData
import com.arbonik.helper.helprequest.RequestManager

class VeteranRequestFragment : Fragment() {
    var requestManager = RequestManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true // сохранение сострояния при перевороте
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_create_request, container, false)

        val data: RecyclerView = root.findViewById(R.id.fragmentRecycler)

        val linear = LinearLayoutManager(context)
        data.layoutManager = linear

        var ca = CategoryAdapter().apply {
            val helpArray = resources.getStringArray(R.array.help_variant)
            categories = MutableList(helpArray.size) { CategoryWidget(helpArray[it]) }
        }
        data.adapter = ca

        val commentText = root.findViewById<TextView>(R.id.comment)
        val timeHelp = root.findViewById<TimePicker>(R.id.timeHelp)
        val datePicker = root.findViewById<DatePicker>(R.id.calendarView)
        timeHelp.setIs24HourView(true)
        root.findViewById<Button>(R.id.toAuth).setOnClickListener {
            v ->
                for (c in ca.categories) {
                    if (c.choise) {
                        var request = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            RequestData(
                                c.category,
                                commentText.text.toString(),
                                SharedPreferenceUser.currentUser!!,
                                "${datePicker.dayOfMonth}.${datePicker.month} в ${timeHelp.hour}:${timeHelp.minute}",
                                false
                            )
                        } else {
                            TODO("VERSION.SDK_INT < M")
                        }
                        requestManager.addRequest(request)
                        c.choise = false
                    }
                }
                ca.notifyDataSetChanged()

                val t = Toast.makeText(HelperApplication.globalContext,"Ваша заявка принята!", Toast.LENGTH_LONG)
                t.setGravity(Gravity.CENTER, 0, 0)
                t.show()
            }
//        }
        return root
    }

}