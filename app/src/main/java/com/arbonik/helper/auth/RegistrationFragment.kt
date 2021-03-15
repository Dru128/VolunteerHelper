package com.arbonik.helper.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.arbonik.helper.R
import com.arbonik.helper.system.Format
import com.arbonik.helper.system.Format.Companion.makeMaskEditTest


class RegistrationFragment() : Fragment()
{
    val regActivity by lazy { activity as RegistrationActivity }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        var root = inflater.inflate(R.layout.fragment_registration, container, false)
        root.apply {

            val container_location = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.container_location)
            val radioButtonVolonteer = findViewById<RadioButton>(R.id.radioButtonVolonteer)
            val radioButtonVeteran = findViewById<RadioButton>(R.id.radioButtonVeteran)
            val location_status_text = findViewById<TextView>(R.id.location_status_text)
            val textPhone = findViewById<EditText>(R.id.phone_reg)
            val textName = findViewById<EditText>(R.id.name_reg)
            val textInf = findViewById<EditText>(R.id.info_reg)
            textName.doAfterTextChanged { RegData.name = it.toString() }
            textPhone.doAfterTextChanged { RegData.phone = Format.format_number(it.toString()) }
            textInf.doAfterTextChanged { RegData.inf = it.toString() }

            if (RegData.name != null) textName!!.text.insert(0, RegData.name)
            if (RegData.phone != null) textPhone!!.text.insert(0, RegData.phone.toString())
            if (RegData.inf != null) textInf!!.text.insert(0, RegData.inf.toString())
            if (RegData.location != null) location_status_text!!.text = getString(R.string.selected)


            findViewById<RadioGroup>(R.id.role_radio_group)
                .setOnCheckedChangeListener { _, id ->
                    if (id == radioButtonVolonteer.id)
                    {
                        RegData.typeUser = USER_CATEGORY.VOLONTEER
                        container_location.visibility = View.GONE
                    }
                    else
                    {
                        RegData.typeUser = USER_CATEGORY.VETERAN
                        container_location.visibility = View.VISIBLE
                    }
                }
            findViewById<Button>(R.id.singInBotton)
                .setOnClickListener {
                    if (RegData.phone?.length == 12 && RegData.name != "") // проверка на ввод данных
                    {
                        if (radioButtonVeteran.isChecked && RegData.location != null || radioButtonVolonteer.isChecked) // проверка на выбор адреса, если пользователь ветеран
                        {
                            regActivity.authUser(RegData.phone.toString(), Aim.register)
                        }
                        else
                            Toast.makeText(context, getString(R.string.choose_location), Toast.LENGTH_LONG).show()
                    }
                    else
                        Toast.makeText(context, getString(R.string.inputAllView), Toast.LENGTH_LONG).show()
                }
            findViewById<Button>(R.id.adressButton)
                .setOnClickListener{ regActivity.setMapFragment() }
            makeMaskEditTest(textPhone!!)
        }
        return root
    }
}          