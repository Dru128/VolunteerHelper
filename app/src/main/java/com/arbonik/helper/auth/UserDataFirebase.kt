package com.arbonik.helper.auth

import android.widget.Toast
import com.arbonik.helper.HelperApplication
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import java.util.*
import kotlin.collections.HashMap

class UserDataFirebase
{
    val COLLECTION_TAG = "USERS"

    var db = FirebaseFirestore.getInstance()

    fun addUser(user: User)
    {
        db.collection(COLLECTION_TAG).document(user.uid.toString()).set(user)
    }

    fun updateUserData(document: String, data: Map<String, Any>)
    {
        db.collection(COLLECTION_TAG).document("5mJ9AmswqITeUFA77Ksbs17Mzh52").update(User.TAG_STATUS_ACCOUNT, "TEST_177711")
            .addOnSuccessListener {
                Toast.makeText(HelperApplication.globalContext, "addOnSuccessListener", Toast.LENGTH_SHORT).show()

            }
            .addOnFailureListener{
                Toast.makeText(HelperApplication.globalContext, "addOnFailureListener", Toast.LENGTH_SHORT).show()

            }
    }

    fun convertDocumentToUser(data: MutableMap<String, Any>) = User(
        name = data[User.NAME_TAG.toLowerCase(Locale.ROOT)].toString(),
        phone = data[User.TAG_PHONE.toLowerCase(Locale.ROOT)].toString(),
        inf = data[User.TAG_INF.toLowerCase(Locale.ROOT)].toString(),
        rating = 0f/*result[User.RATING_TAG.toLowerCase(Locale.ROOT)].toString().toFloat()*/,
        category = USER_CATEGORY_CREATER(data[User.TAG_CATEGORY.toLowerCase(Locale.ROOT)].toString()),
        uid = data[User.TAG_UID.toLowerCase(Locale.ROOT)].toString(),
        notification = data[User.TAG_NOTFICATION.toLowerCase(Locale.ROOT)].toString().toBoolean(),
        location = data[User.TAG_LOCATION.toLowerCase(Locale.ROOT)] as GeoPoint?,
        status_account = STATUS_ACCOUNT_CREATER(data[User.TAG_STATUS_ACCOUNT.toLowerCase(Locale.ROOT)].toString())
    )
}