package com.arbonik.helper.helprequest

import android.app.Activity
import android.util.Log
import com.arbonik.helper.auth.SharedPreferenceUser
import com.google.firebase.firestore.FirebaseFirestore

class RequestManager {

    companion object {
        val USERS_TAG = "USERS"
        const val REQUEST_TAG = "requests"
        const val MY_REQUEST_TAG = "myrequests"
    }

    val db = FirebaseFirestore.getInstance()

    var requests: Array<RequestData> = arrayOf()

    fun addRequest(request: RequestData) {
         db.collection(REQUEST_TAG).add(request).addOnSuccessListener {
              var id = it.id
         db.collection(USERS_TAG)
             .document(SharedPreferenceUser.currentUser?.uid!!)
             .collection(MY_REQUEST_TAG)
             .add(mapOf(id to id))
         }
    }

    fun deleteRequest(uid: String) {
        db.collection(REQUEST_TAG).document(uid).delete()
    }

    fun showRequests(activity: Activity): Array<RequestData> {
        var a = db.collection(REQUEST_TAG).get().addOnCompleteListener(activity) {
            var result = it.result?.documents
            var resultRequestData : Array<RequestData> =
                Array(result?.size ?: 0) {i ->
                    result!![i].toObject(RequestData::class.java)!!
                }
            requests = resultRequestData.clone()
        }
        while (!a.isComplete);
        requests.forEach {
            Log.d("TESTTEXT", it.toString())
        }

        return requests
    }

}