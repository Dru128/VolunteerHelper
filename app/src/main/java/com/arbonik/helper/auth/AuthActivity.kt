package com.arbonik.helper.auth

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.arbonik.helper.MainActivity
import com.arbonik.helper.helprequest.RequestManager
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import java.util.*


class AuthActivity : AuthBase()
{
    private var db = FirebaseFirestore.getInstance()
    private val RC_SIGNIN: Int = 1635769

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        checkAuthUser()
    }


    private fun checkAuthUser()
    {
        if (authUI != null)
        {
            if (sharedPreferenceUser.checkAuth())
            {
                // already signed in
                startActivity(Intent(this, MainActivity::class.java))
            }
            else
            {
                startActivity(Intent(this, RegistrationActivity::class.java))
            }
            finish()
        }
        else
        {
            // not signed in
            val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())
            startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).setIsSmartLockEnabled(false).build(), RC_SIGNIN
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGNIN)
        {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK && authUI != null)
            {
                // Successfully signed in
                getDataUser(authUI!!)
            }
            else
            {
                if (authUI == null) checkAuthUser()
                else
                AlertDialog.Builder(this)
                    .setMessage("ошибка: ${response?.error?.errorCode}")
                    .show()
            }
        }
    }


    private fun getDataUser(user: FirebaseUser)
    {
        val ref = db.collection(RequestManager.USERS_TAG)
        val query = ref.document(user.uid)

        query.get()
            .addOnSuccessListener(this) {
                val result = it.data
                if (result != null)
                {
                    result.let {
                        val returnUser = User(
                            name = result[User.NAME_TAG.toLowerCase(Locale.ROOT)].toString(),
                            phone = result[User.TAG_PHONE.toLowerCase(Locale.ROOT)].toString(),
                            inf = result[User.TAG_INF.toLowerCase(Locale.ROOT)].toString(),
                            rating = 0f/*result[User.RATING_TAG.toLowerCase(Locale.ROOT)].toString().toFloat()*/,
                            category = USER_CATEGORY_CREATER(result[User.TAG_CATEGORY.toLowerCase(
                                Locale.ROOT)].toString()),
                            uid = result[User.TAG_UID.toLowerCase(Locale.ROOT)].toString(),
                            notification = result[User.TAG_NOTFICATION.toLowerCase(Locale.ROOT)].toString().toBoolean(),
                            location = result[User.TAG_LOCATION.toLowerCase(Locale.ROOT)] as GeoPoint?
                        )
                        sharedPreferenceUser.authInDevice(returnUser)
                    }
                    startActivity(Intent(this, MainActivity::class.java))
                }
                else
                {
                    startActivity(Intent(this, RegistrationActivity::class.java))
                }
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Ошибка: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }
}

