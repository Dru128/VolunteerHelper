package com.arbonik.helper.ui.home

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arbonik.helper.*
import com.arbonik.helper.auth.User

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
            if(PreferenceManager.getDefaultSharedPreferences(HelperApplication.globalContext).getBoolean(
                     User.TAG_AUTH, false)) {
                val t = Toast.makeText(
                    HelperApplication.globalContext,
                    "Волонтер не может размещать заявки :(", Toast.LENGTH_LONG
                )
                t.setGravity(Gravity.CENTER, 0, 0)
                t.show()
            }else {
                // if user - veteeran
                for (c in ca.categories) {
                    if (c.choise)
//                        FireDatabase.createRequest(
//                            DataRequest(
//                                LocalUserData?.name ?: "Имя не указано",
//                                LocalUserData?.number ?: "Телефон не указан",
//                                LocalUserData?.address ?: "Адресс не указан",
//                                c.category
//                            )
//                        )
                    c.choise = false
                }
                ca.notifyDataSetChanged()

                val t = Toast.makeText(
                    HelperApplication.globalContext,
                    "Ваша заявка принята!", Toast.LENGTH_LONG
                )
                t.setGravity(Gravity.CENTER, 0, 0)
                t.show()
            }
        }
        return root
    }
}