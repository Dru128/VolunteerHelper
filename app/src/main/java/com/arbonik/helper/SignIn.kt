package com.arbonik.helper

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.arbonik.helper.auth.Authefication
import com.arbonik.helper.auth.RegistrationActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.android.synthetic.main.activity_auth.*
import kotlin.collections.HashMap

class SignIn : AppCompatActivity() {

//    private fun helloWorld(): Task<String> {
//        // Create the arguments to the callable function.
//        var data : HashMap<String, Any> = hashMapOf()
//        return firebaseFunction
//            .getHttpsCallable("hello")
//            .call(data)
//            .continueWith(object : Continuation<HttpsCallableResult, String>() {
//                override fun then(p0: Task<HttpsCallableResult>): String {
//                    return ""
//                }
//            })
//            {
//                task ->
//                // This continuation runs on either success or failure, but if the task
//                // has failed then result will throw an Exception which will be
//                // propagated down.
//                val result = task.result?.data as String
//                Log.d("TEXT", result)
//                result
//            }
//

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        singInBotton.setOnClickListener { v ->
            if (authentication(email.text.toString(), password.text.toString())) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
//                        RunHhttpRequest().execute()
                //button.text = task.result
                // If sign in fails, display a message to the user.
            }
        }

        registration.setOnClickListener { v ->
            startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }
    fun authentication(login: String, password: String): Boolean {
        var authefication = Authefication()
        return authefication.authUser("zaq@zaq.ru", "123456")
    }

    inner class RunHhttpRequest : AsyncTask<Unit, Unit, String>(){
        override fun doInBackground(vararg params: Unit?): String {
            var result = FirebaseFun().getFirst()
            Log.d("TEXT", result)
            return ""
        }
    }

}
