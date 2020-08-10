package com.arbonik.helper.auth

import com.google.firebase.firestore.FirebaseFirestore

class UserDataFirebase {

    val COLLECTION_TAG = "USERS"

    var db = FirebaseFirestore.getInstance()

    fun addUser(user : User)
    {
        db.collection(COLLECTION_TAG).document(user.uid.toString()).set(user)
    }

}