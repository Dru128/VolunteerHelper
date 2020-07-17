package com.arbonik.helper.helprequest

import androidx.databinding.BaseObservable

class RequestViewModel(requestManager: RequestManager) : BaseObservable() {
    var request: RequestData? = null
        set(value) {
            field = value
            notifyChange()
        }
    fun onClick(){

    }
}