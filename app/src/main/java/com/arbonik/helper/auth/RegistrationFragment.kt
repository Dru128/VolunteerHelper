package com.arbonik.helper.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.arbonik.helper.R
import com.arbonik.helper.system.Format
import com.arbonik.helper.system.Format.Companion.makeMask
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint


class RegistrationFragment() : Fragment()
{
    var textPhone: EditText? = null
    var textName: EditText? = null
    val regActivity by lazy { activity as RegistrationActivity }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        var root = inflater.inflate(R.layout.fragment_registration, container, false)
        root.apply {

            val container_location = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.container_location)
            val radioButtonVolonteer = findViewById<RadioButton>(R.id.radioButtonVolonteer)
            val radioButtonVeteran = findViewById<RadioButton>(R.id.radioButtonVeteran)
            val location_status_text = findViewById<TextView>(R.id.location_status_text)
            textPhone = findViewById(R.id.phone_reg)
            textName = findViewById(R.id.name_reg)

            if (RegData.name != null) textName!!.text.insert(0, RegData.name)
            if (RegData.phone != null) textPhone!!.text.insert(0, RegData.phone.toString())
            if (RegData.location != null) location_status_text!!.text = getString(R.string.selected)


            findViewById<RadioGroup>(R.id.role_radio_group)
                .setOnCheckedChangeListener { _, id ->
                    if (id == radioButtonVolonteer.id) container_location.visibility = View.GONE
                    else container_location.visibility = View.VISIBLE
                }
            findViewById<Button>(R.id.singInBotton)
                .setOnClickListener {
                    if (Format.format_number(textPhone?.text.toString()).length == 12 && textName?.text.toString() != "") // проверка на ввод данных
                        {
                            if (radioButtonVeteran.isChecked && RegData.location != null || radioButtonVolonteer.isChecked) // проверка на выбор адреса, если пользователь ветеран
                            {
                                val geoPoint: GeoPoint = GeoPoint(RegData.location!!.latitude, RegData.location!!.longitude)
                                regActivity.authUser(Format.format_number(textPhone?.text.toString()), Aim.register)
                            }
                                else
                                Toast.makeText(context, getString(R.string.choose_location), Toast.LENGTH_LONG).show()
                        } else
                                Toast.makeText(context, getString(R.string.inputAllView), Toast.LENGTH_LONG).show()
                }
            findViewById<Button>(R.id.adressButton)
                .setOnClickListener{
                        if (RegData.phone != null) RegData.phone = Format.format_number(textPhone!!.text.toString()).toInt()
                        if (RegData.name != null) RegData.name = textName!!.text.toString()
                        if (radioButtonVeteran.isChecked) RegData.typeUser = USER_CATEGORY.VETERAN
                        else RegData.typeUser = USER_CATEGORY.VOLONTEER
                        regActivity.setMapFragment()
                }
            makeMask(textPhone!!)
        }
        return root
    }
}          