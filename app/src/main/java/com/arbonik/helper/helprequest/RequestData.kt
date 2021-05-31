package com.arbonik.helper.helprequest

import com.arbonik.helper.auth.User

data class RequestData(
    var title: String = "",
    var comment: String = "",
    var master: User = User(),
    var date: String = "",
    var status: Boolean = false
)