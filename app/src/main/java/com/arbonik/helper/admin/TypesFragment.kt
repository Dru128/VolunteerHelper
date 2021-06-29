package com.arbonik.helper.admin

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arbonik.helper.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class TypesFragment : Fragment()
{
    private lateinit var recyclerView: RecyclerView
    private var db = FireBaseTypesRequest()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_types_admin, container, false)
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)


        recyclerView = root.findViewById(R.id.recyclerView_admin)
        recyclerView.adapter = RecyclerAdapterTypesHelpAdmin()
        recyclerView.layoutManager = LinearLayoutManager(context)
        updateRecycler()

        root.findViewById<FloatingActionButton>(R.id.add_type_button_fragment_types_admin).setOnClickListener{
            AlertDialog.Builder(context).apply{
                val editText = EditText(context) // текст для ввода типа заявки
                editText.filters = arrayOf(InputFilter.LengthFilter(30)) // максимальная длина текста (в символах)
                setView(editText)
                setTitle(R.string.title_types_dialog_admin) // заголовок
                setNegativeButton(context.getString(R.string.cancel)) { _, _ -> }  // кнопка нейтрального ответа
                setPositiveButton(context.getString(R.string.add))
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

        db
            .reference
            .get()
            .addOnSuccessListener { result ->
                var data: MutableList<TypeRequest> = mutableListOf()
                for (document in result)
                    data.add(TypeRequest(document.get("type").toString(), document.id))
                (recyclerView.adapter as RecyclerAdapterTypesHelpAdmin).apply {
                    dataset = data
                    notifyDataSetChanged()
                }
            }
    }
}