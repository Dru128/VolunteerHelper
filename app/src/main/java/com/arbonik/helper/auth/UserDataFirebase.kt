package com.arbonik.helper.auth

import com.google.firebase.firestore.FirebaseFirestore

class UserDataFirebase {

    val COLLECTION_TAG = "USERS"

    var db = FirebaseFirestore.getInstance()

    fun getDataUser(uid: String): User? {
        var ref = db.collection(COLLECTION_TAG)
        var query = ref.document(uid)

        var returnUser : User? = null

        query.get()
            .addOnSuccessListener {
            val result = it.data
                result?.let {
                    returnUser = User(
                        result[User.NAME_TAG].toString(),
                        result[User.TAG_PHONE].toString(),
                        result[User.TAG_ADDRESS].toString(),
                        USER_CATEGORY_CREATER(result[User.TAG_CATEGORY].toString()),
                        result[User.TAG_UID].toString()
                    )
                }
            }
            .addOnFailureListener {

            }
        return returnUser
    }
    fun addUser(user : User){
        db.collection(COLLECTION_TAG).document(user.UID.toString()).set(user)
    }

    fun User.USER_TO_HASH() = HashMap<String, Any>().apply{
        put(User.NAME_TAG, name!!)
        put(User.TAG_PHONE, phone!!)
        put(User.TAG_ADDRESS, address!!)
        put(User.TAG_CATEGORY, CATEGORY!!)
        put(User.TAG_UID, UID!!)
    }
}