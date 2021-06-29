package com.arbonik.helper.auth

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.arbonik.helper.MainActivity
import com.arbonik.helper.R
import com.arbonik.helper.helprequest.RequestManager
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore


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
                getDataUser(authUI!!)
            }
            else
            {
                startActivity(Intent(this, RegistrationActivity::class.java))
                finish()
            }
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
                        var userDataFirebase = UserDataFirebase()
                        val returnUser = userDataFirebase.convertDocumentToUser(result)
                        sharedPreferenceUser.authInDevice(returnUser)


                        when (returnUser.status_account)
                        {
                            STATUS_ACCOUNT.REG_CHECKED ->
                            {
                                // уведомить юзера что его заявка на регистрацию рассматривается
                                setContentView(R.layout.activity_auth_account_status)
                                findViewById<TextView>(R.id.text_activity_auth_account_status)
                                    .setText(getString(R.string.your_account_checked))
                            }
                            STATUS_ACCOUNT.ACTIVE, null ->
                            {
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                            STATUS_ACCOUNT.LOCKED ->
                            {
                                // уведомить юзера что его аккаунт заблокирован
                                setContentView(R.layout.activity_auth_account_status)
                                findViewById<TextView>(R.id.text_activity_auth_account_status)
                                    .setText(getString(R.string.your_account_blocked))
                            }
                        }

                    }

                }
                else
                {
                    startActivity(Intent(this, RegistrationActivity::class.java))
                    finish()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Ошибка: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }
}
