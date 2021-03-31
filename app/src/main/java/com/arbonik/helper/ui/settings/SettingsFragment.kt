package com.arbonik.helper.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.preference.*
import com.arbonik.helper.HelperApplication
import com.arbonik.helper.R
import com.arbonik.helper.auth.SharedPreferenceUser
import com.arbonik.helper.auth.USER_CATEGORY
import com.arbonik.helper.auth.User
import com.arbonik.helper.helprequest.RequestManager
import com.arbonik.helper.system.Format
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint


class SettingsFragment : PreferenceFragmentCompat()
{
    var requestManager = RequestManager()
    var user: User? = null
    val text_name by lazy { findPreference<EditTextPreference>(User.NAME_TAG)!! }
    val text_phone by lazy { findPreference<EditTextPreference>(User.TAG_PHONE)!! }
    val text_inf by lazy { findPreference<EditTextPreference>(User.TAG_INF)!! }
    val text_user_category by lazy { findPreference<EditTextPreference>(User.TAG_CATEGORY)!! }
    val button_address by lazy { findPreference<Preference>(User.TAG_LOCATION)!! }
//    val switch_notification by lazy { findPreference<SwitchPreference>(User.TAG_NOTFICATION)!! }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?)
    {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey)
        user = SharedPreferenceUser.currentUser
        //        switch_notification = findPreference(TAG_NOTFICATION)
        when (user?.category)
        {
            USER_CATEGORY.VOLONTEER ->
            {
                text_inf.isVisible = false
                button_address.isVisible = false
            }
        }
        setPreferenceListener(text_name)
        setPreferenceListener(text_inf)
        text_phone.summary = Format.makeMaskTextView(text_phone.text.toString())
        //        setPreferenceListener(switch_notification!!)

        button_address.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val navController = this.view?.findNavController()
            val bundle = Bundle()
            bundle.putParcelable("location", Format.geoPoint_to_latLng(user?.location!!))

            navController?.navigate(R.id.action_navigation_notifications_to_map_veteran_fragment2, bundle)
            navController?.addOnDestinationChangedListener { controller, destination, arguments ->
                if (destination.id == R.id.navigation_notifications)
                {
                    val arg = arguments?.getParcelable(User.TAG_LOCATION) as LatLng?
                    if (arg != null && user != null)
                    {
                        val sharedPreferenceUser = SharedPreferenceUser()
                        user?.location = Format.latLng_to_geoPoint(arg)
                        sharedPreferenceUser.authInDevice(user!!)
                        requestManager.updateUser(user!!)
                    }
                }
            }
            true
        }
    }

    fun setPreferenceListener(p: Preference)
    {
        p.onPreferenceChangeListener = listener
        when (p)
        {
            is EditTextPreference -> listener.onPreferenceChange(p, PreferenceManager.getDefaultSharedPreferences(HelperApplication.globalContext).getString(p.key, ""))
            is SwitchPreference -> listener.onPreferenceChange(p, PreferenceManager.getDefaultSharedPreferences(HelperApplication.globalContext).getBoolean(p.key, true))
        }
    }

    val listener = Preference.OnPreferenceChangeListener { preference, newValue ->
        when (preference)
        {
            is EditTextPreference ->
            {
                val value = newValue.toString()
                preference.setSummary(value)
                when (preference.key)
                {
                    User.NAME_TAG -> user?.name = value
                    User.TAG_INF -> user?.inf = value
//                    User.TAG_PHONE -> user?.phone = value
                }
            }
            is SwitchPreference ->
            {
                when (preference.key)
                {
                    //                    TAG_NOTFICATION -> user?.notification = value.toBoolean()
                }
//                 LocalUserData.type = preference.isEnabled
            }
        }
        user?.let { requestManager.updateUser(it) }
        return@OnPreferenceChangeListener true
    }

}