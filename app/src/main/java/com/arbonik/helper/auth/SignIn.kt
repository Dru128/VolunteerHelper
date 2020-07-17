package com.arbonik.helper.auth

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import com.arbonik.helper.FirebaseFun
import com.arbonik.helper.MainActivity
import com.arbonik.helper.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_auth.*

class SignIn : AuthActivity() {

    companion object{
        var appUser : User? =  null
            set(value) {
                Log.d("TESTTEXT", value.toString())
                field = value
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        forgott_pass.setOnClickListener {
            v -> signIn("asd@mail.ru", "123456")

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
                Log.d("TESTTEXT", "THAT WAS A MISSTAKE")

//                startActivity(Intent(this, MainActivity::class.java))
                true
            } else {
                Log.d("TESTTEXT", "THAT WAS A MISSTAKE")
                //RunHhttpRequest().execute()
                //button.text = task.result
                // If sign in fails, display a message to the user.
                false
            }
        }
        return false
    }


    val COLLECTION_TAG = "USERS"

    var db = FirebaseFirestore.getInstance()

    fun getDataUser(uid: String){
        var ref = db.collection(COLLECTION_TAG)
        var query = ref.document(uid)

        query.get()
            .addOnSuccessListener(this) {
                Log.d("TESTTEXT", "THAT WAS A MISSTAKE")
                var result = it.data
                Log.d("TESTTEXT", "THAT WAS A MISSTAKE")
                result?.let {
                Log.d("TESTTEXT", "THAT WAS A MISSTAKE")
                    var returnUser = User(
                        result[User.NAME_TAG.toLowerCase()].toString(),
                        result[User.TAG_PHONE.toLowerCase()].toString(),
                        result[User.TAG_ADDRESS.toLowerCase()].toString(),
                        USER_CATEGORY_CREATER(result[User.TAG_CATEGORY.toLowerCase()].toString()),
                        result[User.TAG_UID.toLowerCase()].toString()
                    )
                Log.d("TESTTEXT", "THAT WAS A MISSTAKE")
                    sharedPreferenceUser.authInDevice(returnUser)
                    startActivity(Intent(this, MainActivity::class.java))
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
