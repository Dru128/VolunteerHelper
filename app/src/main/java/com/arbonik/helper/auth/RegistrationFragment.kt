package com.arbonik.helper.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.arbonik.helper.R
import com.arbonik.helper.system.Format

class RegistrationFragment() : Fragment()
{
    val navController by lazy { this.requireView().findNavController() }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        var root = inflater.inflate(R.layout.fragment_registration, container, false)
        root.apply {

            val container_location = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.container_location)
            val radioButtonVolonteer = findViewById<RadioButton>(R.id.radioButtonVolonteer)
            val radioButtonVeteran = findViewById<RadioButton>(R.id.radioButtonVeteran)
            val location_status_text = findViewById<TextView>(R.id.location_status_text)
            val textName = findViewById<EditText>(R.id.name_reg)
            val textInf = findViewById<EditText>(R.id.info_reg)
            textName.doAfterTextChanged { RegData.name = it.toString() }
            textInf.doAfterTextChanged { RegData.inf = it.toString() }

            if (RegData.name != null) textName!!.text.insert(0, RegData.name)
            if (RegData.inf != null) textInf!!.text.insert(0, RegData.inf.toString())
            if (RegData.location != null) location_status_text!!.text = getString(R.string.selected)

            requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN) // скрыть клавиатуру при создании фрагмента

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
            findViewById<Button>(R.id.registration_button)
                .setOnClickListener {
                    if (!RegData.name.isNullOrEmpty()) // проверка на ввод номера телефона
                    {
                        if (RegData.typeUser == USER_CATEGORY.VETERAN && RegData.location != null || RegData.typeUser == USER_CATEGORY.VOLONTEER) // проверка на выбор адреса, если пользователь ветеран
                        {
                            (activity as RegistrationActivity).registration_user()
                        }
                        else
                            Toast.makeText(context, getString(R.string.choose_location), Toast.LENGTH_LONG).show()
                    }
                    else
                        Toast.makeText(context, getString(R.string.choose_name), Toast.LENGTH_LONG).show()
                }
            findViewById<Button>(R.id.adressButton)
                .setOnClickListener{
                    val bundle = Bundle()
                    if (RegData.location != null)
                    {
                        bundle.putParcelable("location", Format.geoPoint_to_latLng(RegData.location!!))
                    }
                    navController.navigate(R.id.action_registration_fragment_to_map_veteran_fragment, bundle)
                }
        }
        return root
    }
}          