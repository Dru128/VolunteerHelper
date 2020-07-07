package com.arbonik.helper


import com.arbonik.helper.HelpRequest.DataRequest
import com.arbonik.helper.auth.SharedPreferenceUser
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore

object FireDatabase {
    var db = FirebaseFirestore.getInstance()

//    var database : FirebaseDatabase
//    val requestReference : DatabaseReference
//
//    fun userFromDB(p0 : DataSnapshot):User =
//        User(
//            p0.child("${SharedPreferenceUser.NAME_TAG}").toString(),
//            p0.child("${SharedPreferenceUser.TAG_PHONE}").toString(),
//            p0.child("${SharedPreferenceUser.TAG_ADDRESS}").toString(),
//            USER_CATEGORY_CREATER(p0.child("${SharedPreferenceUser.TAG_CATEGORY}").toString()),
//            p0.child("${SharedPreferenceUser.TAG_UID}").toString()
//        )

    fun createRequest(dataHelpRequest: DataRequest){
//        requestReference.push().setValue(dataHelpRequest)
    }
}