package com.arbonik.helper.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.Navigation
import com.arbonik.helper.MainActivity
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
        val user = createUser(authUI!!.uid)
        userDataFirebase.addUser(user)
        sharedPreferenceUser.authInDevice(user)
        startActivity(Intent(this, MainActivity::class.java))
        Toast.makeText(this, "регистрация завершена", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun createUser(uid: String) = User(
        name = RegData.name,
        phone = RegData.phone,
        inf = RegData.inf,
        rating = null,
        category = RegData.typeUser,
        uid = uid,
        location = if (RegData.typeUser == USER_CATEGORY.VETERAN) RegData.location else /*GeoPoint(0.0, 0.0)*/ null,
        notification = true
    )
}


