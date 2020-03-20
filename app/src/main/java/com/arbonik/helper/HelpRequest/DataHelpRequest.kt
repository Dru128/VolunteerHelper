package com.arbonik.helper.HelpRequest

import android.util.Log
import com.arbonik.helper.ui.home.Category
import com.google.firebase.database.DataSnapshot
import java.util.*

class DataHelpRequest() {

    var name :String = ""
    var phone :String = ""
    var address :String = ""
    var category = Category.HELP
    var answered = false
    var unic = ""
    constructor(name : String, phone : String, address: String, category: Category, ans: Boolean = false) : this() {
        this.name = name
        this.phone = phone
        this.address = address
        this.category = category
    }
    companion object {
        fun fromBD(ds: DataSnapshot): DataHelpRequest {
            var dataHelpRequest = DataHelpRequest()
            dataHelpRequest.unic = ds.key!!
            Log.d("ID", dataHelpRequest.unic.toString())
            dataHelpRequest.answered = ds.child("answered").value.toString().toBoolean()
             dataHelpRequest.name = ds.child("name").value.toString()
             dataHelpRequest.phone = ds.child("phone").value.toString()
             dataHelpRequest.address = ds.child("address").value.toString()
             dataHelpRequest.category = when (ds.child("category").value.toString()){
                 "HELP" -> Category.HELP
                 "PETS" -> Category.PETS
                 "COMMUNITY" -> Category.COMMUNITY
                 "PRODUCT" -> Category.PRODUCT
                 else -> Category.HELP
             }
            return dataHelpRequest
        }
    }
}
