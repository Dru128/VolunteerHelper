package com.arbonik.helper.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.children
import com.arbonik.helper.MainActivity
import com.arbonik.helper.R
import com.arbonik.helper.helprequest.RequestManager.Companion.USERS_TAG
import com.arbonik.helper.othertools.CheckValidate.Companion.checkDataInput
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_auth.*

class SignInActivity : AuthBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        forgott_pass.setOnClickListener {
        }
        singInBotton.setOnClickListener { v ->
            val allView = auth_activity_container.children
            if (checkDataInput(allView)) {
                signIn(email.text.toString(), password.text.toString())
            } else
                Toast.makeText(this, getString(R.string.inputAllView), Toast.LENGTH_LONG).show()
        }

        registration.setOnClickListener { v ->
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }


    fun signIn(login : String, password : String) {
        mAuth.signInWithEmailAndPassword(login+"@mail.ru", password)
            .addOnCompleteListener(this) {
            if (it.isSuccessful) {
                val currentUser = mAuth.currentUser!!
                getDataUser(currentUser.uid)
            }
        }
            .addOnFailureListener { p0 ->
                Toast.makeText(this, "Ошибка: ${p0.message}", Toast.LENGTH_LONG).show()
            }
    }

    var db = FirebaseFirestore.getInstance()

    fun getDataUser(uid: String){
        var ref = db.collection(USERS_TAG)
        var query = ref.document(uid)

        query.get()
            .addOnSuccessListener(this) {
                var result = it.data
                result?.let {
                    var returnUser = User(
                        result[User.NAME_TAG.toLowerCase()].toString(),
                        result[User.TAG_PHONE.toLowerCase()].toString(),
                        result[User.TAG_ADDRESS.toLowerCase()].toString(),
                        USER_CATEGORY_CREATER(result[User.TAG_CATEGORY.toLowerCase()].toString()),
                        result[User.TAG_UID.toLowerCase()].toString(),
                        result[User.TAG_NOTFICATION.toLowerCase()].toString().toBoolean()
                    )
                    sharedPreferenceUser.authInDevice(returnUser)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
            .addOnFailureListener {
                Log.d("TESTTEXT", "WOW")
            }
    }
}
