package com.arbonik.helper.auth

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.children
import com.arbonik.helper.MainActivity
import com.arbonik.helper.R
import com.arbonik.helper.othertools.CheckValidate.Companion.checkDataInput
import com.arbonik.helper.system.Format
import com.arbonik.helper.system.Format.Companion.makeMask
import kotlinx.android.synthetic.main.activity_auth.*


class SignInActivity : AuthBase()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        if(sharedPreferenceUser.checkAuth())
        {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        val phone_sign = findViewById<EditText>(R.id.phone_sign)
        makeMask(phone_sign)
        singInBotton.setOnClickListener { v ->
            val allView = auth_activity_container.children
            if (checkDataInput(allView))
            {
                authUser(Format.format_number(phone_sign.text.toString()), Aim.signIn)
            }
            else
                Toast.makeText(this, getString(R.string.inputAllView), Toast.LENGTH_LONG).show()
        }
        registration.setOnClickListener { v ->
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }
}

