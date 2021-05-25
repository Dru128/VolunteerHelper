package com.arbonik.helper.auth

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.arbonik.helper.R
import com.arbonik.helper.system.Format
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseUser

class RegistrationFragment() : Fragment()
{
    private val PHONE_NUMBER_RC: Int = 1274756
    private val RC_AUTH: Int = 3902478
    private val navController by lazy { this.requireView().findNavController() }
    private lateinit var textPhone: TextView
    private lateinit var textAccount: TextView
    var authUI: FirebaseUser? = null
        @SuppressLint("RestrictedApi") get() = AuthUI.getInstance().auth.currentUser
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
            textAccount = findViewById(R.id.text_account)
            textPhone = findViewById(R.id.text_phone)
            textName.doAfterTextChanged { RegData.name = it.toString() }
            textInf.doAfterTextChanged { RegData.inf = it.toString() }

            if (RegData.name != null) textName!!.text.insert(0, RegData.name)
            if (RegData.inf != null) textInf!!.text.insert(0, RegData.inf.toString())
            if (RegData.location != null) location_status_text!!.text = getString(R.string.selected)

            textAccount.text = authUI!!.email

            requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN) // скрыть клавиатуру при создании фрагмента

            textPhone.setOnClickListener {
                requestPhoneNumber()
            }
            textAccount.setOnClickListener {
                AuthUI.getInstance().signOut(context).addOnSuccessListener {
                    auth_userUI()
                }
            }

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
                    if (!RegData.name.isNullOrEmpty()) // проверка на ввод имени
                    {
                        if (!RegData.phone.isNullOrEmpty()) // проверка на ввод номера телефона
                        {
                            if (RegData.typeUser == USER_CATEGORY.VETERAN && RegData.location != null || RegData.typeUser == USER_CATEGORY.VOLONTEER)
                            { // проверка на выбор адреса, если пользователь ветеран
                                (requireActivity() as RegistrationActivity).registration_user()
                            }
                            else Toast.makeText(context, getString(R.string.choose_location), Toast.LENGTH_LONG).show()
                        }
                        else  Toast.makeText(context, getString(R.string.choose_phone_number), Toast.LENGTH_LONG).show()
                    }
                    else Toast.makeText(context, getString(R.string.choose_name), Toast.LENGTH_LONG).show()
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

    private fun requestPhoneNumber()
    {
        val hintRequest = HintRequest.Builder().setPhoneNumberIdentifierSupported(true).build()
        val intent = Auth.CredentialsApi.getHintPickerIntent(GoogleApiClient.Builder(requireContext()).addApi(Auth.CREDENTIALS_API).build(), hintRequest)
        startIntentSenderForResult(intent.intentSender, PHONE_NUMBER_RC, null, 0, 0, 0, null)
    }

    private fun auth_userUI()
    {
        val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).setIsSmartLockEnabled(false).build(), RC_AUTH
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PHONE_NUMBER_RC)
        {
            if (resultCode == AppCompatActivity.RESULT_OK && data != null)
            {
                val credential: Credential? = data.getParcelableExtra(Credential.EXTRA_KEY)
                val phoneNumber = credential?.id.toString()
                RegData.phone = phoneNumber
                textPhone.text = Format.makeMaskTextView(phoneNumber)
            }
        }
        else if (requestCode == RC_AUTH)
        {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK && authUI != null)
            {
                textAccount.text = authUI!!.email
            }
            else
            {
                if (authUI == null) auth_userUI()
                else
                    AlertDialog.Builder(context)
                        .setMessage("ошибка: ${response?.error?.errorCode}")
                        .show()
            }
        }
    }
}          