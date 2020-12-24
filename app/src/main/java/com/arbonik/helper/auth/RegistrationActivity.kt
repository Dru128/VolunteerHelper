package com.arbonik.helper.auth

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.children
import com.arbonik.helper.R
import com.arbonik.helper.othertools.CheckValidate.Companion.checkDataInput
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.concurrent.TimeUnit


class RegistrationActivity : AuthBase()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        singInBotton.setOnClickListener { v ->
            val allView = viewGroupRegistration.children
            if (checkDataInput(allView))
            {
                authUser(phone.text.toString(), Aim.register)
            } else
                Toast.makeText(this, getString(R.string.inputAllView), Toast.LENGTH_LONG).show()
        }
    }
}
