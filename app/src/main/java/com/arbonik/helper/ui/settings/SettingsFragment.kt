package com.arbonik.helper.ui.settings

import android.os.Bundle
import androidx.preference.*
import com.arbonik.helper.HelperApplication
import com.arbonik.helper.Profile
import com.arbonik.helper.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey)
        setPreferenceListener(findPreference<EditTextPreference>(key_name)!!)
        setPreferenceListener(findPreference<EditTextPreference>(key_phone)!!)
        setPreferenceListener(findPreference<EditTextPreference>(key_address)!!)
        setPreferenceListener(findPreference<SwitchPreferenceCompat>(key_role)!!)
    }

    companion object{
        val key_name = "key_name"
        val key_phone = "key_phone"
        val key_address = "key_address"
        val key_role = "key_role"

        fun setPreferenceListener(p : Preference){
            p.onPreferenceChangeListener = listener
            if (p is EditTextPreference) {
                listener.onPreferenceChange(
                    p,
                    PreferenceManager.getDefaultSharedPreferences(HelperApplication.globalContext)
                        .getString(p.key, "")
                )
            if (p is SwitchPreferenceCompat){
                listener.onPreferenceChange(p, PreferenceManager.getDefaultSharedPreferences(HelperApplication.globalContext)
                    .getBoolean(key_role, false))
            }
            }
        }
        val listener = Preference.OnPreferenceChangeListener { preference, newValue ->
            val value = newValue.toString()
                if (preference is EditTextPreference) {
                    preference.setSummary(value)
                    when (preference.key){
                        key_name -> Profile.name = preference.summary.toString()
                        key_phone -> Profile.number = preference.summary.toString()
                        key_address -> Profile.address = preference.summary.toString()
                    }
                } else{
                    if (preference is SwitchPreferenceCompat) {
                        Profile.type = preference.isEnabled
                    }
                }
            return@OnPreferenceChangeListener true
        }
    }
}