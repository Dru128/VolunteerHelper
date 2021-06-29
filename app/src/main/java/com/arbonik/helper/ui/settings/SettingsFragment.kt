package com.arbonik.helper.ui.settings

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.preference.*
import com.arbonik.helper.R
import com.arbonik.helper.auth.SharedPreferenceUser
import com.arbonik.helper.auth.USER_CATEGORY
import com.arbonik.helper.auth.User
import com.arbonik.helper.auth.UserDataFirebase
import com.arbonik.helper.system.Format
import com.google.android.gms.maps.model.LatLng


class SettingsFragment : PreferenceFragmentCompat()
{
    private var userDataFirebase = UserDataFirebase()
    private var user: User? = SharedPreferenceUser.currentUser
    private val name_text by lazy { findPreference<Preference>(User.NAME_TAG)!! }
    private val phone_text by lazy { findPreference<Preference>(User.TAG_PHONE)!! }
    private val inf_text by lazy { findPreference<Preference>(User.TAG_INF)!! }
    private val user_category_text by lazy { findPreference<Preference>(User.TAG_CATEGORY)!! }
    private val address_button by lazy { findPreference<Preference>(User.TAG_LOCATION)!! }
//    val switch_notification by lazy { findPreference<SwitchPreference>(User.TAG_NOTFICATION)!! }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?)
    {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey)
//        switch_notification = findPreference(TAG_NOTFICATION)
        when (user?.category)
        {
            USER_CATEGORY.VOLONTEER, USER_CATEGORY.ADMIN ->
            {
                inf_text.isVisible = false
                address_button.isVisible = false
            }
        }
        phone_text.summary = Format.makeMaskPhone(user?.phone.toString())
        name_text.summary = user?.name.toString()
        user_category_text.summary = user?.category.toString()

        //        setPreferenceListener(switch_notification!!)

        address_button.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val navController = this.view?.findNavController()
            val bundle = Bundle()
            bundle.putParcelable(User.TAG_LOCATION, Format.geoPointTolatLng(user?.location!!))

            navController?.navigate(R.id.action_navigation_notifications_to_map_veteran_fragment2, bundle)
            navController?.addOnDestinationChangedListener { controller, destination, arguments ->
                if (destination.id == R.id.navigation_notifications)
                {
                    val arg = arguments?.getParcelable(User.TAG_LOCATION) as LatLng?
                    if (arg != null && user != null)
                    {
//                        val sharedPreferenceUser = SharedPreferenceUser()
//                        user?.location = Format.latLngTogeoPoint(arg)
//                        sharedPreferenceUser.authInDevice(user!!)
//                        userDataFirebase.addUser(user!!)
                    }
                }
            }
            true
        }
    }

/*    fun setPreferenceListener(p: Preference)
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
                    User.TAG_PHONE -> user?.phone = value
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
        userDataFirebase.addUser(user!!)
        return@OnPreferenceChangeListener true
    }*/

}