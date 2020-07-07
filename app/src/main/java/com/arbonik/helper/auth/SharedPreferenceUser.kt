package com.arbonik.helper.auth

import android.util.Log
import androidx.preference.PreferenceManager
import com.arbonik.helper.HelperApplication

class SharedPreferenceUser {

    var editor = PreferenceManager
        .getDefaultSharedPreferences(HelperApplication.globalContext)
        .edit()

    fun authInDevice(user: User?){
        user?.let {
            with(editor) {
                putBoolean(User.TAG_AUTH, true)
                putString(User.NAME_TAG, user.name)
                putString(User.TAG_UID, user.UID)
                putString(User.TAG_ADDRESS, user.address)
                putString(User.TAG_PHONE, user.phone)
                putString(User.TAG_CATEGORY, user.CATEGORY.toString())
                Log.d("AUTH", user.CATEGORY.toString())
                apply()
            }
        }
    }

    fun checkAuth():Boolean{
        return PreferenceManager.getDefaultSharedPreferences(HelperApplication.globalContext)
            .getBoolean(User.TAG_AUTH, false)
    }

    fun loginOut(){
            editor.putBoolean(User.TAG_AUTH, true)
            editor.apply()
    }

}
