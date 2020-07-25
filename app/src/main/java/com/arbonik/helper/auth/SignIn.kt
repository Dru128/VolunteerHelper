package com.arbonik.helper.auth

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import com.arbonik.helper.FirebaseFun
import com.arbonik.helper.MainActivity
import com.arbonik.helper.R
import com.arbonik.helper.helprequest.RequestManager.Companion.USERS_TAG
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_auth.*

class SignIn : AuthActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        forgott_pass.setOnClickListener {
        }
        singInBotton.setOnClickListener { v ->
            signIn(email.text.toString(), password.text.toString())
        }

        registration.setOnClickListener { v ->
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }


    fun signIn(login : String, password : String):Boolean {
        mAuth.signInWithEmailAndPassword(login, password).addOnCompleteListener(this) {
            if (it.isSuccessful) {
                val currentUser = mAuth.currentUser!!
                getDataUser(currentUser.uid)
                true
            } else {
                false
            }
        }
        return false
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
                        result[User.TAG_UID.toLowerCase()].toString()
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

    class RunHhttpRequest : AsyncTask<Unit, Unit, String>(){
        override fun doInBackground(vararg params: Unit?): String {
            var result = FirebaseFun().getFirst()
            Log.d("TEXT", result)
            return ""
        }
    }
}
