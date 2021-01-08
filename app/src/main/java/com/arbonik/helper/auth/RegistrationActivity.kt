package com.arbonik.helper.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.arbonik.helper.Map.MapsFragment
import com.arbonik.helper.R
import kotlinx.android.synthetic.main.activity_registration.*


class RegistrationActivity : AuthBase()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        makeMask(phone_reg)
        role_radio_group.setOnCheckedChangeListener{ _, id ->
            if (id == radioButtonVolonteer.id) container_location.visibility = View.GONE
            else container_location.visibility = View.VISIBLE
        }
        adressButton.setOnClickListener {
            var locationDialog = ChooseLocationDialog()
            locationDialog.show(supportFragmentManager, "tag_dialog") //вызов диалога, в котром должен быть MapsFragment()
        }
        singInBotton.setOnClickListener { v ->
//            if (Format.format_number(phone_reg.text.toString()).length == 12 && name_reg.text.toString() != "") // проверка на ввод данных
//            {
//                if (radioButtonVolonteer.isChecked && geoPoint != null || radioButtonVolonteer.isChecked) // проверка на выбор адреса, если пользователь ветеран
//                    authUser(Format.format_number(phone_reg.text.toString()), Aim.register)
//                else
//                    Toast.makeText(this, getString(R.string.choose_location), Toast.LENGTH_LONG).show()
//            } else
//                Toast.makeText(this, getString(R.string.inputAllView), Toast.LENGTH_LONG).show()
        }
    }
}
