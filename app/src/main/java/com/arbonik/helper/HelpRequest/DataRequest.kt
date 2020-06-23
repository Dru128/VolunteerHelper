package com.arbonik.helper.HelpRequest

import com.arbonik.helper.ui.home.Category
import com.google.firebase.database.DataSnapshot


data class DataRequest (var name: String = "",
                        var phone: String = "",
                        var address: String = "",
                        var category: Category = Category.HELP,
                        var answered: Boolean = false ) {

    companion object {
        fun fromBD(ds: DataSnapshot): DataRequest {
            var dataHelpRequest = DataRequest()
            dataHelpRequest.answered = ds.child("answered").value.toString().toBoolean()
            dataHelpRequest.name = ds.child("name").value.toString()
            dataHelpRequest.phone = ds.child("phone").value.toString()
            dataHelpRequest.address = ds.child("address").value.toString()
            dataHelpRequest.category = when (ds.child("category").value.toString()) {
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
