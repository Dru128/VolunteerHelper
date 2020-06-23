package com.arbonik.helper


import com.arbonik.helper.HelpRequest.DataRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FireDatabase {

    var database : FirebaseDatabase
    val requestReference : DatabaseReference

    init {
        database = FirebaseDatabase.getInstance()
        requestReference = database.getReference("REQUESTION")

    }

    fun addUser(user : User){
        val usersReference : DatabaseReference = database.getReference("USERS")
        usersReference.push().setValue(user)
        usersReference.lis
    }

    fun createReques(dataHelpRequest: DataRequest){
        requestReference.push().setValue(dataHelpRequest)
    }
}