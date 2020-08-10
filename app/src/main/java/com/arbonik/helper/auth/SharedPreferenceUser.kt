package com.arbonik.helper.auth

import android.util.Log
import androidx.preference.PreferenceManager
import com.arbonik.helper.HelperApplication

class SharedPreferenceUser
{
    companion object
    {
        var currentUser :User? = null
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

    var sharedPreference = PreferenceManager
        .getDefaultSharedPreferences(HelperApplication.globalContext)

    var editor = sharedPreference.edit()

    fun authInDevice(user: User)
    {
        currentUser = user
        with(editor)
        {
            putBoolean(User.TAG_AUTH, true)
            putString(User.NAME_TAG, user.name)
            putString(User.TAG_UID, user.uid)
            putString(User.TAG_ADDRESS, user.address)
            putString(User.TAG_PHONE, user.phone)
            putString(User.TAG_CATEGORY, user.category.toString())
            Log.d("TESTTEXT", "LOGININ")
            apply()
        }
    }

    fun checkAuth():Boolean
    {
        Log.d("TESTTEXT","LOGINCHECK")
        return PreferenceManager.getDefaultSharedPreferences(HelperApplication.globalContext)
            .getBoolean(User.TAG_AUTH, false)
    }

    fun loginOut()
    {
        Log.d("TESTTEXT","LOGINOUT")
        currentUser = null
            editor.putBoolean(User.TAG_AUTH, false)
            editor.apply()
    }

    fun restoreUser():User{
       var sharedPreference= PreferenceManager.getDefaultSharedPreferences(HelperApplication.globalContext)
            with(sharedPreference)
            {
            return@restoreUser User(
                getString(User.NAME_TAG,""),
                getString(User.TAG_PHONE,""),
                getString(User.TAG_ADDRESS,""),
                USER_CATEGORY_CREATER(getString(User.TAG_CATEGORY,"")!!),
                getString(User.TAG_UID,""))
            }
        }
}
