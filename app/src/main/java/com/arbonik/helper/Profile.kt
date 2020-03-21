package com.arbonik.helper

object Profile {
    var name : String? = null
    var number : String? = null
    var address : String? = null
    var type : profileType? = null

}

enum class profileType(){ VOLONTEER, NOTVOLONTEER}