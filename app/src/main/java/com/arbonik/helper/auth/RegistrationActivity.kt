package com.arbonik.helper.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.children
import com.arbonik.helper.R
import com.arbonik.helper.othertools.CheckValidate.Companion.checkDataInput
import kotlinx.android.synthetic.main.activity_registration.*


class RegistrationActivity : AuthBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        singInBotton.setOnClickListener { v ->
            val allView = viewGroupRegistration.children
            if (checkDataInput(allView)) {
                registerUser()
            } else
                Toast.makeText(this, getString(R.string.inputAllView), Toast.LENGTH_LONG).show()
        }
    }

    fun registerUser(){
        mAuth.createUserWithEmailAndPassword(email.text.toString()+"@mail.ru", password.text.toString())
            .addOnCompleteListener(this) { p0 ->
                if (p0.isSuccessful) {
                    Toast.makeText(this, mAuth.currentUser?.uid!!, Toast.LENGTH_LONG).show()
                    userDataFirebase.addUser(createUser(mAuth.currentUser?.uid!!))
                    startActivity(Intent(this, SignInActivity::class.java))
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
        uid,
        true
    )

    fun getUserCategory() : USER_CATEGORY =
        when (role_radio_group.checkedRadioButtonId){
            R.id.radioButtonVeteran -> USER_CATEGORY.VETERAN
            R.id.radioButtonVolonteer -> USER_CATEGORY.VOLONTEER
            else -> USER_CATEGORY.ADMIN
        }
}
