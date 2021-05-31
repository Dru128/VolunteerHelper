package com.arbonik.helper.auth

import com.google.firebase.firestore.GeoPoint


data class User(
    var name: String? = null,
    var phone: String? = null,
    var inf: String? = null,
    val category: USER_CATEGORY? = null,
    var rating: Float? = null,
    val uid: String? = null,
    var notification: Boolean? = null,
    var location: GeoPoint? = null,
    var status_account: STATUS_ACCOUNT? = null)
{
    companion object
    {
        const val TAG_PHONE : String = "PHONE"
        const val NAME_TAG = "NAME"
        const val TAG_INF : String = "INF"
        const val TAG_CATEGORY : String = "CATEGORY"
        const val TAG_STATUS_ACCOUNT : String = "STATUS_ACCOUNT"
        const val RATING_TAG : String = "RATING"
        const val TAG_UID = "UID"
        const val TAG_AUTH = "AUTH"
        const val TAG_NOTFICATION = "NOTIFICATION"
        const val TAG_LOCATION = "LOCATION"
        const val TAG_LOCATION_LAT = "LOCATION_LAT"
        const val TAG_LOCATION_LON = "LOCATION_LON"
    }
}

fun USER_CATEGORY_CREATER(s : String) = when(s)
{
    "VETERAN" -> USER_CATEGORY.VETERAN
    "VOLONTEER"-> USER_CATEGORY.VOLONTEER
    "ADMIN"    -> USER_CATEGORY.ADMIN
    else -> USER_CATEGORY.VETERAN
}

fun STATUS_ACCOUNT_CREATER(s : String) = when(s)
{
    "REG_CHECKED" -> STATUS_ACCOUNT.REG_CHECKED
    "ACTIVE"-> STATUS_ACCOUNT.ACTIVE
    "LOCKED"    -> STATUS_ACCOUNT.LOCKED
    "DELETED" -> STATUS_ACCOUNT.DELETED
    else -> null
}

enum class USER_CATEGORY
{
    VETERAN,
    VOLONTEER,
    ADMIN
}

enum class STATUS_ACCOUNT
{
    REG_CHECKED,
    ACTIVE,
    LOCKED,
    DELETED
}
