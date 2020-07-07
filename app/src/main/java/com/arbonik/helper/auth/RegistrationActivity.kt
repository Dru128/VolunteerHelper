package com.arbonik.helper.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.arbonik.helper.MainActivity
import com.arbonik.helper.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_registration.*


class RegistrationActivity : AppCompatActivity() {

    var mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        var i = Intent(this, MainActivity::class.java)

//        if (SharedPreferenceUser.checkAuth())
//            startActivity(i)

//            startActivity(i)

//        role.setOnCheckedChangeListener{buttonView, isChecked ->
//            editor.putBoolean(SettingsFragment.key_role, isChecked)
//            editor.commit()
//        }

        singInBotton.setOnClickListener { v ->
            registerUser()
        }
    }

    fun registerUser(){
        val authefication = Authefication()

        mAuth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { p0 ->
                if (p0.isSuccessful) {
                    authefication.signUp(createUser(mAuth.currentUser?.uid!!))
                    Toast.makeText(this, mAuth.currentUser?.uid!!, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, p0.exception.toString(), Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = mAuth.currentUser
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
