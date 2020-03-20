package com.arbonik.helper

import com.arbonik.helper.HelpRequest.DataHelpRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FireDatabase {
    var database : FirebaseDatabase
    val requestReference : DatabaseReference
    init {
        database = FirebaseDatabase.getInstance()
        requestReference = database.getReference("REQUESTION")
    }
    fun createReques(dataHelpRequest: DataHelpRequest){
        requestReference.push().setValue(dataHelpRequest)
    }
}