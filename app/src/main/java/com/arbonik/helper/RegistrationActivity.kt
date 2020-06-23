package com.arbonik.helper

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.arbonik.helper.ui.settings.SettingsFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import kotlinx.android.synthetic.main.activity_registration.*


class RegistrationActivity : AppCompatActivity() {

    private var mAuth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        mAuth = FirebaseAuth.getInstance();

        var i = Intent(this, MainActivity::class.java)

//        if (!(PreferenceManager.getDefaultSharedPreferences(HelperApplication.globalContext)
//            .getString(SettingsFragment.key_phone,"") == "" ||
//            PreferenceManager.getDefaultSharedPreferences(HelperApplication.globalContext)
//            .getString(SettingsFragment.key_address,"") == "" ||
//        PreferenceManager.getDefaultSharedPreferences(HelperApplication.globalContext)
//            .getString(SettingsFragment.key_name,"") == ""
//                    ))
//            startActivity(i)

        val editor = PreferenceManager.getDefaultSharedPreferences(HelperApplication.globalContext).edit()
        phone.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editor.putString(SettingsFragment.key_phone, s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                editor.commit()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
        })
        name.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editor.putString(SettingsFragment.key_name, s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                editor.commit()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
        })
        adress.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editor.putString(SettingsFragment.key_address, s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                editor.commit()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
        })

//        role.setOnCheckedChangeListener{buttonView, isChecked ->
//            editor.putBoolean(SettingsFragment.key_role, isChecked)
//            editor.commit()
//        }

        button.setOnClickListener { v ->
//            startActivity(i)

        }
    }



    fun registerUser(){
        mAuth!!.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { p0 ->
                if (p0.isSuccessful) {
//                    Toast.makeText(this, "Пользователь успешно зарегестрирован!", Toast.LENGTH_LONG).show()
                    FireDatabase.addUser(createUser(mAuth?.currentUser?.uid!!))
                    Toast.makeText(this, mAuth?.currentUser?.uid!!, Toast.LENGTH_LONG).show()

                } else {
                    Toast.makeText(this, p0.exception.toString(), Toast.LENGTH_LONG).show()
                }
            }
        //FireDatabase.addUser(mAuth.currentUser)
        clearAllTextView()

    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth!!.currentUser
        Log.d("TAG", currentUser.toString())
    }

    fun createUser(uid : String) = User(
            name.text.toString(),
            phone.text.toString(),
            adress.text.toString(),
            getUserCategory(),
            uid
        )

    fun getUserCategory() : USER_CATEGORY =
        when (role_radio_group.checkedRadioButtonId){
            R.id.radioButtonVeteran -> USER_CATEGORY.VETERAN
            R.id.radioButtonVolonteer -> USER_CATEGORY.VOLONTEER
            else -> USER_CATEGORY.ADMIN
        }
    fun clearAllTextView(){
        email.text.clear()
        password.text.clear()
        name.text.clear()
        phone.text.clear()
        adress.text.clear()
    }
}
