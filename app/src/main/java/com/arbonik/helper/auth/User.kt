package com.arbonik.helper.auth


data class User(
    var name : String? = null,
    var phone : String? = null,
    var address : String? = null,
    val category : USER_CATEGORY? = null,
    val uid : String? = null,
    var notification: Boolean? = null
){
    companion object
    {
        const val TAG_PHONE : String = "PHONE"
        const val NAME_TAG = "NAME"
        const val TAG_ADDRESS : String = "ADDRESS"
        const val TAG_CATEGORY : String = "CATEGORY"
        const val TAG_UID = "UID"
        const val TAG_AUTH = "AUTH"
        const val TAG_NOTFICATION = "NOTIFICATION"
    }
}

fun USER_CATEGORY_CREATER(s : String) = when(s)
{
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
