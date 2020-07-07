package com.arbonik.helper.auth

import com.google.firebase.auth.FirebaseUser

data class User(
    var name : String? = null,
    var phone : String? = null,
    var address : String? = null,
    val CATEGORY : USER_CATEGORY? = null,
    val UID : String? = null
){
    companion object{
        var currentUser: FirebaseUser? = null;
        const val TAG_PHONE : String = "PHONE"
        const val NAME_TAG = "NAME"
        const val TAG_ADDRESS : String = "ADDRESS"
        const val TAG_CATEGORY : String = "CATEGORY"
        const val TAG_UID = "UID"
        const val TAG_AUTH = "AUTH"

    }
}

fun USER_CATEGORY_CREATER(s : String) = when(s){
    "VETERAN" -> USER_CATEGORY.VETERAN
    "VOLONTEER"-> USER_CATEGORY.VOLONTEER
    "ADMIN"    -> USER_CATEGORY.ADMIN
    else -> USER_CATEGORY.VETERAN
}

enum class USER_CATEGORY{
    VETERAN,
    VOLONTEER,
    ADMIN
}
