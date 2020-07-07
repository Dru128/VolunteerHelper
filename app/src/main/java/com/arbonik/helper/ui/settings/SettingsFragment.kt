package com.arbonik.helper.ui.settings

import android.os.Bundle
import androidx.preference.*
import com.arbonik.helper.HelperApplication
import com.arbonik.helper.R
import com.arbonik.helper.auth.User

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey)
        setPreferenceListener(findPreference<EditTextPreference>(User.NAME_TAG)!!)
        setPreferenceListener(findPreference<EditTextPreference>(User.TAG_PHONE)!!)
        setPreferenceListener(findPreference<EditTextPreference>(User.TAG_ADDRESS)!!)
        setPreferenceListener(findPreference<EditTextPreference>(User.TAG_CATEGORY)!!)
    }
    companion object{
        fun setPreferenceListener(p : Preference){
            p.onPreferenceChangeListener = listener
            if (p is EditTextPreference) {
                listener.onPreferenceChange(
                    p,
                    PreferenceManager.getDefaultSharedPreferences(HelperApplication.globalContext)
                        .getString(p.key, "")
                )
            }
        }
        val listener = Preference.OnPreferenceChangeListener { preference, newValue ->
            val value = newValue.toString()
                if (preference is EditTextPreference) {
                    preference.setSummary(value)
                    when (preference.key){
//                        key_name -> LocalUserData.name = preference.summary.toString()
//                        key_phone -> LocalUserData.number = preference.summary.toString()
//                        key_address -> LocalUserData.address = preference.summary.toString()
                    }
                } else{
                    if (preference is SwitchPreferenceCompat) {
                       // LocalUserData.type = preference.isEnabled
                    }
                }
            return@OnPreferenceChangeListener true
        }
    }
}