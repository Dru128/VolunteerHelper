package com.arbonik.helper.auth

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.arbonik.helper.MainActivity
import com.arbonik.helper.helprequest.RequestManager
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import java.util.*


open class AuthBase : AppCompatActivity()
{

    var auth = FirebaseAuth.getInstance()
    var db = FirebaseFirestore.getInstance()
    val sharedPreferenceUser = SharedPreferenceUser()

    fun createUser(uid: String, phone: String) = User(
        name = RegData.name,
        phone = phone,
        inf = RegData.inf,
        rating = null,
        category = RegData.typeUser,
        uid = uid,
        location = if (RegData.typeUser == USER_CATEGORY.VETERAN) RegData.location else /*GeoPoint(0.0, 0.0)*/ null,
        notification = true
    )

    fun getDataUser(uid: String)
    {
        var ref = db.collection(RequestManager.USERS_TAG)
        var query = ref.document(uid)

        query.get()
            .addOnSuccessListener(this) {
                val result = it.data
                if (result != null)
                {
                    result?.let {
                        var returnUser = User(
                            name = result[User.NAME_TAG.toLowerCase(Locale.ROOT)].toString(),
                            phone = result[User.TAG_PHONE.toLowerCase(Locale.ROOT)].toString(),
                            inf = result[User.TAG_INF.toLowerCase(Locale.ROOT)].toString(),
                            rating = 0f/*result[User.RATING_TAG.toLowerCase(Locale.ROOT)].toString().toFloat()*/,
                            category = USER_CATEGORY_CREATER(result[User.TAG_CATEGORY.toLowerCase(Locale.ROOT)].toString()),
                            uid = result[User.TAG_UID.toLowerCase(Locale.ROOT)].toString(),
                            notification = result[User.TAG_NOTFICATION.toLowerCase(Locale.ROOT)].toString().toBoolean(),
                            location = result[User.TAG_LOCATION.toLowerCase(Locale.ROOT)] as GeoPoint?
                        )
                        sharedPreferenceUser.authInDevice(returnUser)
                        //                    Toast.makeText(this, "Регистрация завершена", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                } else
                {
                    startActivity(Intent(this, RegistrationActivity::class.java))
                    finish()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Ошибка: ${it.message}", Toast.LENGTH_LONG).show()
            }
    }
}