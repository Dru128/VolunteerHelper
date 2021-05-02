package com.arbonik.helper.auth

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import com.arbonik.helper.HelperApplication
import com.google.firebase.firestore.GeoPoint

class SharedPreferenceUser
{
    companion object
    {
        var currentUser: User? = null
            get()
            {
                if (field == null)
                {
                    var sharedPreferenceUser = SharedPreferenceUser()
                    if (sharedPreferenceUser.checkAuth())
                    {
                        currentUser = sharedPreferenceUser.restoreUser()
                    }
                }
                return field
            }
    }

    var sharedPreference = PreferenceManager.getDefaultSharedPreferences(HelperApplication.globalContext)

    var editor = sharedPreference.edit()

    fun authInDevice(user: User?)
    {
        currentUser = user
        with(editor) {
            putBoolean(User.TAG_AUTH, true)
            putString(User.NAME_TAG, user?.name)
            putString(User.TAG_UID, user?.uid)
            putString(User.TAG_INF, user?.inf)
            user?.location?.latitude?.let { putDouble(User.TAG_LOCATION_LAT, it) }
            user?.location?.longitude?.let { putDouble(User.TAG_LOCATION_LON, it) }
            putString(User.TAG_PHONE, user?.phone)
            putString(User.TAG_CATEGORY, user?.category.toString())
            putFloat(User.RATING_TAG, user?.rating.toString().toFloat())
            putBoolean(User.TAG_NOTFICATION, user?.notification.toString().toBoolean())
            Log.d("TESTTEXT", "LOGININ")
            apply()
        }
    }

    fun checkAuth(): Boolean
    {
        Log.d("TESTTEXT", "LOGINCHECK")
        return PreferenceManager.getDefaultSharedPreferences(HelperApplication.globalContext)
            .getBoolean(User.TAG_AUTH, false)
    }

    fun loginOut()
    {
        Log.d("TESTTEXT", "LOGINOUT")
        currentUser = null
        editor.putBoolean(User.TAG_AUTH, false)
        editor.apply()
        HelperApplication.clearDataApp()
    }

    fun restoreUser(): User
    {
        var sharedPreference =
            PreferenceManager.getDefaultSharedPreferences(HelperApplication.globalContext)
        with(sharedPreference) {
            return@restoreUser User(
                getString(User.NAME_TAG, ""),
                getString(User.TAG_PHONE, ""),
                getString(User.TAG_INF, ""),
                USER_CATEGORY_CREATER(getString(User.TAG_CATEGORY, "")!!),
                getFloat(User.RATING_TAG, -1f),
                getString(User.TAG_UID, ""),
                getBoolean(User.TAG_NOTFICATION, true),
                GeoPoint(
                    getDouble(User.TAG_LOCATION_LAT, 0.0), getDouble(User.TAG_LOCATION_LON, 0.0)
                )
            )
        }
    }

    fun SharedPreferences.Editor.putDouble(key: String, double: Double) =
        putLong(key, java.lang.Double.doubleToRawLongBits(double))

    fun SharedPreferences.getDouble(key: String, default: Double) =
        java.lang.Double.longBitsToDouble(getLong(key, java.lang.Double.doubleToRawLongBits(default)))
}