package com.arbonik.helper.auth

import android.util.Log
import androidx.preference.PreferenceManager
import com.arbonik.helper.HelperApplication

class SharedPreferenceUser {
    companion object{
        var currentUser :User? = null
    }
    var editor = PreferenceManager
        .getDefaultSharedPreferences(HelperApplication.globalContext)
        .edit()

    fun authInDevice(user: User){
        currentUser = user
        with(editor) {
            putBoolean(User.TAG_AUTH, true)
            putString(User.NAME_TAG, user.name)
            putString(User.TAG_UID, user.uid)
            putString(User.TAG_ADDRESS, user.address)
            putString(User.TAG_PHONE, user.phone)
            putString(User.TAG_CATEGORY, user.category.toString())
            Log.d("TESTTEXT", user.category.toString())
            apply()
        }
    }

    fun checkAuth():Boolean{
        return PreferenceManager.getDefaultSharedPreferences(HelperApplication.globalContext)
            .getBoolean(User.TAG_AUTH, false)
    }

    fun loginOut(){
        currentUser = null
            editor.putBoolean(User.TAG_AUTH, false)
            editor.apply()
    }

}
