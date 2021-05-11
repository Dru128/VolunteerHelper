package com.arbonik.helper.auth

import android.os.Bundle
import android.widget.Toast
import androidx.navigation.Navigation
import com.arbonik.helper.R


class RegistrationActivity : AuthBase()
{
    private val userDataFirebase = UserDataFirebase()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        var navController = Navigation.findNavController(this, R.id.nav_host_fragment_auth)

    }

    fun registration_user()
    {
        val user = auth.currentUser!!.uid
        val phone = auth.currentUser!!.phoneNumber!!
        userDataFirebase.addUser(createUser(user, phone))
        getDataUser(user)
        Toast.makeText(this, "регистрация завершена", Toast.LENGTH_SHORT).show()
    }
}


