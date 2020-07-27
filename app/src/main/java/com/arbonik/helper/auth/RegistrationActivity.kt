package com.arbonik.helper.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.children
import com.arbonik.helper.R
import kotlinx.android.synthetic.main.activity_registration.*


class RegistrationActivity : AuthActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        singInBotton.setOnClickListener { v ->
            if (checkDataInput()) {
                registerUser()

            }
        }
    }

    fun checkDataInput() : Boolean{
        val allView = viewGroupRegistration.children
        for (i in allView){
            if (i is EditText){
                if (i.text.isEmpty()) {
                    Toast.makeText(this, getString(R.string.inputAllView), Toast.LENGTH_LONG).show()
                    return false
                }
            }
        }
        return true
    }

    fun registerUser(){
        mAuth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { p0 ->
                if (p0.isSuccessful) {
                    Toast.makeText(this, mAuth.currentUser?.uid!!, Toast.LENGTH_LONG).show()
                    userDataFirebase.addUser(createUser(mAuth.currentUser?.uid!!))
                    startActivity(Intent(this, SignIn::class.java))
                    finish()
                }
            }
            .addOnFailureListener {it ->
                Toast.makeText(this, "Ошибка регестации: ${it.message}", Toast.LENGTH_LONG).show()
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
}
