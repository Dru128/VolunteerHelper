package com.arbonik.helper.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.Preference
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arbonik.helper.FireDatabase
import com.arbonik.helper.HelpRequest.DataHelpRequest
import com.arbonik.helper.MainActivity
import com.arbonik.helper.Profile
import com.arbonik.helper.R

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true // сохранение сострояния при перевороте
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val data: RecyclerView = root.findViewById(R.id.fragmentRecycler)

        val linear = LinearLayoutManager(context)
        data.layoutManager = linear
            var ca = CategoryAdapter()
            ca.categories = mutableListOf(
                CategoryWidget(Category.HELP, false),
                CategoryWidget(Category.PETS, false),
                CategoryWidget(Category.PRODUCT, false),
                CategoryWidget(Category.COMMUNITY, false)
            )
            data.adapter = ca

        root.findViewById<Button>(R.id.toAuth).setOnClickListener {
            v ->
            for (c in ca.categories){
                if (c.choise)
                FireDatabase.createReques(
                    DataHelpRequest(
                        Profile?.name ?: "Имя не указано",
                        Profile?.number ?: "Телефон не указан",
                        Profile?.address ?: "Адресс не указан",
                        c.category))
            }
        }
        return root
    }
}