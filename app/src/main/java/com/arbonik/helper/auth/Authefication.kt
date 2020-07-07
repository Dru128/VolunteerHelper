package com.arbonik.helper.auth

import com.google.firebase.auth.FirebaseAuth

class Authefication {
    private val mAuth = FirebaseAuth.getInstance()
    private val sharedPreferenceUser = SharedPreferenceUser()
    private val userDataFirebase = UserDataFirebase()

    fun signUp(user : User){
        userDataFirebase.addUser(user)
        sharedPreferenceUser.authInDevice(user)
    }

    fun authUser(login : String, password : String):Boolean {
        mAuth.signInWithEmailAndPassword(login, password).addOnCompleteListener() {
            if (it.isSuccessful) {
                val currentUser = mAuth.currentUser!!
                val user = userDataFirebase.getDataUser(currentUser.uid)
                sharedPreferenceUser.authInDevice(user)
                true
            } else {
                //RunHhttpRequest().execute()
                //button.text = task.result
                // If sign in fails, display a message to the user.
                false
            }
        }
//        preferenwceManager.checkAuth()
        return true
    }

}