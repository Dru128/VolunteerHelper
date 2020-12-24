package com.arbonik.helper.auth

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.children
import com.arbonik.helper.MainActivity
import com.arbonik.helper.R
import com.arbonik.helper.helprequest.RequestManager.Companion.USERS_TAG
//import com.arbonik.helper.notifications.Notification
import com.arbonik.helper.othertools.CheckValidate.Companion.checkDataInput
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_auth.phone
import kotlinx.android.synthetic.main.activity_auth.singInBotton
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.concurrent.TimeUnit

class SignInActivity : AuthBase()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        singInBotton.setOnClickListener { v ->
            val allView = auth_activity_container.children
            if (checkDataInput(allView))
            {
                authUser(phone.text.toString(), Aim.signIn)
            }
            else
                Toast.makeText(this, getString(R.string.inputAllView), Toast.LENGTH_LONG).show()
        }
        registration.setOnClickListener { v ->
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }
}
