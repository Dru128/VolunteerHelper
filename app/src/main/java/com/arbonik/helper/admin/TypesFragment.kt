package com.arbonik.helper.admin

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arbonik.helper.R


class TypesFragment : Fragment()
{
    private lateinit var recyclerView: RecyclerView
    private var db = FireStore()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_types_admin, container, false)
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)


        recyclerView = root.findViewById(R.id.recyclerView_admin)
        recyclerView.adapter = RecyclerView_admin()
        recyclerView.layoutManager = LinearLayoutManager(context)
        updateRecycler()

        root.findViewById<Button>(R.id.addType_button).setOnClickListener{
            AlertDialog.Builder(context).apply{
                val editText = EditText(context) // текст для ввода типа заявки
                editText.filters = arrayOf(InputFilter.LengthFilter(30)) // максимальная длина текста (в символах)
                setView(editText)
                setTitle(R.string.title_dialog_admin) // заголовок
                setMessage(R.string.message_dialog_admin) // сообщение
                setIcon(android.R.drawable.ic_dialog_info) // иконка
                setNegativeButton("отмена") { _, _ -> }  // кнопка нейтрального ответа
                setPositiveButton("создать")
                { _, _ ->
                    db.addTypeHelp(editText.text.toString(), context) // добавить новый тип помощи
                    updateRecycler()
                } // кнопка положительного ответа
                show() // создаем диалог
            }
        }

        return root
    }

     fun updateRecycler() // прочитать коллекцию типы помощи и обновить RecyclerView_admin
    {
        var data: MutableList<TypeRequest> = mutableListOf()
        FireStore.ref
            .get()
            .addOnSuccessListener { result ->
                for (document in result)
                    data.add(TypeRequest(document.get("type").toString(), document.id))
                RecyclerView_admin.Dataset = data
                (recyclerView.adapter as RecyclerView_admin).notifyDataSetChanged()
            }
    }
}