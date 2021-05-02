package com.arbonik.helper.auth

data class RecaptchaResponce(
    var apk_package_name: String,
    var challenge_ts: String,
    var error_codes: List<String>,
    var success: String
)