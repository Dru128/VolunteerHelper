package com.arbonik.helper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.preference.PreferenceManager
import com.arbonik.helper.ui.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        var i = Intent(this, MainActivity::class.java)

        if (!(PreferenceManager.getDefaultSharedPreferences(HelperApplication.globalContext)
            .getString(SettingsFragment.key_phone,"") == "" ||
            PreferenceManager.getDefaultSharedPreferences(HelperApplication.globalContext)
            .getString(SettingsFragment.key_address,"") == "" ||
        PreferenceManager.getDefaultSharedPreferences(HelperApplication.globalContext)
            .getString(SettingsFragment.key_name,"") == ""
                    )  )
            startActivity(i)

        val editor = PreferenceManager.getDefaultSharedPreferences(HelperApplication.globalContext).edit()
        phone.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editor.putString(SettingsFragment.key_phone, s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                editor.commit()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
        })
        name.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editor.putString(SettingsFragment.key_name, s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                editor.commit()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
        })
        adress.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                editor.putString(SettingsFragment.key_address, s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                editor.commit()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
        })

        role.setOnCheckedChangeListener{buttonView, isChecked ->
            editor.putBoolean(SettingsFragment.key_role, isChecked)
            editor.commit()
        }

        button.setOnClickListener {
            v ->
            startActivity(i)
        }
    }
}
