package com.arbonik.helper.helprequest

import com.arbonik.helper.auth.User
import com.google.firebase.firestore.FirebaseFirestore

class RequestManager {

    companion object {
        val USERS_TAG = "USERS"
        const val REQUEST_TAG = "requests"
        const val MY_REQUEST_TAG = "myrequests"
    }

    val db = FirebaseFirestore.getInstance()

    var requests: Array<RequestData> = arrayOf()

    fun addRequest(request: RequestData)
    {
         db.collection(REQUEST_TAG).add(request).addOnSuccessListener {
              var id = it.id
         }
    }

    fun deleteRequest(uid: String)
    {
        db.collection(REQUEST_TAG).document(uid).delete()
    }

    fun updateUser(user: User)
    {
        user.uid?.let {
            db.collection(USERS_TAG).document(it)
                .set(user)
                .addOnSuccessListener {}
                .addOnFailureListener {}
        }
    }
}