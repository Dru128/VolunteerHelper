package com.arbonik.helper.auth

import com.google.firebase.firestore.GeoPoint

object RegData
{
    var name: String? = null
    var typeUser: USER_CATEGORY? = USER_CATEGORY.VETERAN
    var location: GeoPoint? = null
    var inf: String? = null
}