package com.arbonik.helper.ui.settings

import android.os.Bundle
import android.widget.Toast
import androidx.preference.*
import com.arbonik.helper.HelperApplication
import com.arbonik.helper.R
import com.arbonik.helper.auth.SharedPreferenceUser
import com.arbonik.helper.auth.User
import com.arbonik.helper.auth.User.Companion.NAME_TAG
import com.arbonik.helper.auth.User.Companion.TAG_ADDRESS
import com.arbonik.helper.auth.User.Companion.TAG_PHONE
import com.arbonik.helper.helprequest.RequestManager

class SettingsFragment : PreferenceFragmentCompat()
{
    var user: User? = null
    var requestManager = RequestManager()
    var text_name :EditTextPreference? = null
    var text_phone :EditTextPreference? = null
    var text_address :EditTextPreference? = null
    var text_user_category :EditTextPreference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?)
    {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey)

        text_name = findPreference(User.NAME_TAG)
        text_phone = findPreference(User.TAG_PHONE)
        text_address = findPreference(User.TAG_ADDRESS)
        text_user_category = findPreference(User.TAG_CATEGORY)
        setPreferenceListener(text_name!!)
        setPreferenceListener(text_phone!!)
        setPreferenceListener(text_address!!)
        setPreferenceListener(text_user_category!!)
        user = SharedPreferenceUser.currentUser
    }

    fun setPreferenceListener(p : Preference)
    {
        p.onPreferenceChangeListener = listener
        if (p is EditTextPreference)
        {
            listener.onPreferenceChange(
                p,
                PreferenceManager.getDefaultSharedPreferences(HelperApplication.globalContext)
                    .getString(p.key, "")
            )
        }
    }

    val listener = Preference.OnPreferenceChangeListener { preference, newValue ->
        val value = newValue.toString()
        if (preference is EditTextPreference)
        {
            preference.setSummary(value)
            when (preference.key)
            {
                NAME_TAG -> user?.name = newValue.toString()
                TAG_ADDRESS -> user?.address = newValue.toString()
                TAG_PHONE -> user?.phone = newValue.toString()
            }
        }
        else
        {
            if (preference is SwitchPreferenceCompat)
            {
                // LocalUserData.type = preference.isEnabled
            }
        }
        user?.let { requestManager.updateUser(it) }
        return@OnPreferenceChangeListener true
    }

}