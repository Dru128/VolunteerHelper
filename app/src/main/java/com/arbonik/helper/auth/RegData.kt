package com.arbonik.helper.auth

import com.google.firebase.firestore.GeoPoint

object RegData
{
    var phone: Int? = null
    var name: String? = null
    var typeUser: USER_CATEGORY? = null
    var location: GeoPoint? = null
}