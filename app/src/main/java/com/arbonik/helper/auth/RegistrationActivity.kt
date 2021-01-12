package com.arbonik.helper.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.arbonik.helper.Map.MapsFragment
import com.arbonik.helper.R
import kotlinx.android.synthetic.main.activity_registration.*


class RegistrationActivity : AuthBase()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
    }
}
