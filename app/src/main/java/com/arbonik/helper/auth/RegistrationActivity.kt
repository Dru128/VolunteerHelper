package com.arbonik.helper.auth

import android.os.Bundle
import com.arbonik.helper.R


class RegistrationActivity : AuthBase()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val regFragment = RegistrationFragment()//.newInstance(5, "Васька")
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.activity_layout_reg, regFragment)
            .commit()
    }
}
