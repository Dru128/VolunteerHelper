package com.arbonik.helper

data class User(
    var name : String? = null,
    var number : String? = null,
    var address : String? = null,
    val CATEGORY : USER_CATEGORY? = null,
    val UID : String? = null
)

enum class USER_CATEGORY{
    VETERAN,
    VOLONTEER,
    ADMIN
}