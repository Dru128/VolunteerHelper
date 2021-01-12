package com.arbonik.helper.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.arbonik.helper.R
import com.arbonik.helper.system.Format
import com.arbonik.helper.system.Format.Companion.makeMask
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.raiting_dialog.view.*


class RegistrationFragment : Fragment()
{
    var phone: EditText? = null
    var name: EditText? = null
    var geoPoint: GeoPoint? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        var root = inflater.inflate(R.layout.fragment_registration, container, false)
        root.apply {
            var container_location = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.container_location)
            var radioButtonVolonteer = findViewById<RadioButton>(R.id.radioButtonVolonteer)
            var radioButtonVeteran = findViewById<RadioButton>(R.id.radioButtonVeteran)
            phone = findViewById(R.id.phone_reg)
            findViewById<Button>(R.id.singInBotton)
                .setOnClickListener{
                        if (Format.format_number(phone!!.text.toString()).length == 12 && name!!.text.toString() != "") // проверка на ввод данных
                        {
                            if (radioButtonVeteran.isChecked && geoPoint != null || radioButtonVolonteer.isChecked) // проверка на выбор адреса, если пользователь ветеран
//                                authUser(Format.format_number(phone_reg.text.toString()), Aim.register)
                            else
                                Toast.makeText(context, getString(R.string.choose_location), Toast.LENGTH_LONG).show()
                        } else
                            Toast.makeText(context, getString(R.string.inputAllView), Toast.LENGTH_LONG).show()
                }
            findViewById<Button>(R.id.adressButton)
                .setOnClickListener{

                }
            makeMask(phone!!)
            findViewById<RadioGroup>(R.id.role_radio_group)
                .setOnClickListener {
                    if (this.view == radioButtonVolonteer) container_location.visibility = View.GONE
                    else container_location.visibility = View.VISIBLE
            }
        }
        return root
    }
}