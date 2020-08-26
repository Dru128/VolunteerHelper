package com.arbonik.helper.ui.settings

import android.os.Bundle
import androidx.preference.*
import com.arbonik.helper.HelperApplication
import com.arbonik.helper.R
import com.arbonik.helper.auth.SharedPreferenceUser
import com.arbonik.helper.auth.User
import com.arbonik.helper.auth.User.Companion.NAME_TAG
import com.arbonik.helper.auth.User.Companion.TAG_ADDRESS
import com.arbonik.helper.auth.User.Companion.TAG_CATEGORY
import com.arbonik.helper.auth.User.Companion.TAG_NOTFICATION
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
    var switch_notification: SwitchPreference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?)
    {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey)

        text_name = findPreference(NAME_TAG)
        text_phone = findPreference(TAG_PHONE)
        text_address = findPreference(TAG_ADDRESS)
        text_user_category = findPreference(TAG_CATEGORY)
        switch_notification = findPreference(TAG_NOTFICATION)
        setPreferenceListener(text_name!!)
        setPreferenceListener(text_phone!!)
        setPreferenceListener(text_address!!)
        setPreferenceListener(text_user_category!!)
        setPreferenceListener(switch_notification!!)
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
        else if (p is SwitchPreference)
        {
            listener.onPreferenceChange(
                p,
                PreferenceManager.getDefaultSharedPreferences(HelperApplication.globalContext)
                    .getBoolean(p.key, true)
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
                NAME_TAG -> user?.name = value
                TAG_ADDRESS -> user?.address = value
                TAG_PHONE -> user?.phone = value
            }
        }
        else if (preference is SwitchPreference)
        {
            when (preference.key)
            {
                TAG_NOTFICATION -> user?.notification = value.toBoolean()
            }
                // LocalUserData.type = preference.isEnabled
        }
        user?.let { requestManager.updateUser(it) }
        return@OnPreferenceChangeListener true
    }

}