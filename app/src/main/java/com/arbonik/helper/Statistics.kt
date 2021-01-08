package com.arbonik.helper

import com.google.firebase.firestore.FirebaseFirestore


object StatisticsFireBase
{
    const val COLLECTION_TAG = "STATISTICS" // collection
    const val REQUEST_TAG = "REQUESTS"      // document
    const val USERS_TAG = "USERS"           // document
    const val PERFOMED_TAG = "perfomed"     // field
    const val NULLIFIED_TAG = "nullified"   // field
    const val VETERANS_TAG = "VETERANS"     // field
    const val VOLONTEERS_TAG = "VOLONTEERS" // field
    const val ADMINS_TAG = "ADMINS"         // field

    var db = FirebaseFirestore.getInstance().collection(COLLECTION_TAG)

    interface getStatistics
    {
        fun getData(document: String, field: String){}
    }

    fun reverseData(document: String, value: Any)
    {
        db.document(document).set(value)
    }

    private data class Users(var veterans: Int = 0, var volonteers: Int = 0, var admins: Int = 0)
    private data class Requests(var nullified: Int = 0, var perfomed: Int = 0)
}
