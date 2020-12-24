package com.arbonik.helper.auth

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.arbonik.helper.MainActivity
import com.arbonik.helper.R
import com.arbonik.helper.helprequest.RequestManager
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.concurrent.TimeUnit

enum class Aim{register, signIn}
open class AuthBase : AppCompatActivity()
{
    var mAuth = FirebaseAuth.getInstance()
    var db = FirebaseFirestore.getInstance()
    val sharedPreferenceUser = SharedPreferenceUser()
    val userDataFirebase = UserDataFirebase()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        mAuth.setLanguageCode("ru")
        if(sharedPreferenceUser.checkAuth())
        {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    fun authUser(phone: String, aim: Aim) // вызывается в registration и signIn Activity
    {

        var callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks()
        {
            override fun onVerificationCompleted(credential: PhoneAuthCredential)
            {

            }

            override fun onVerificationFailed(e: FirebaseException)
            {
                if (e is FirebaseAuthInvalidCredentialsException)
                {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException)
                {
                    // The SMS quota for the project has been exceeded
                }
                // Show a message and update the UI
                Toast.makeText(this@AuthBase, "Ошибка!", Toast.LENGTH_LONG).show()
            }

            override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken)
            {
                AlertDialog.Builder(this@AuthBase/*, R.drawable.dialog_style*/).apply{
                    val editText = EditText(this@AuthBase) // текст для ввода типа заявки
                    setView(editText)

                    setTitle( if (aim == Aim.register) R.string.signup else R.string.signin ) // заголовок
                    setMessage(R.string.writecode) // сообщение
                    setNeutralButton("отмена") { _, _ -> }  // кнопка нейтрального ответа
                    setPositiveButton("ок")
                    { _, _ ->
                        val credential = PhoneAuthProvider.getCredential(verificationId, editText.text.toString())
                        mAuth.signInWithCredential(credential)
                            .addOnCompleteListener(this@AuthBase)
                            { task ->
                                when (aim)
                                {
                                    Aim.register ->
                                    {
                                        userDataFirebase.addUser(createUser(mAuth.currentUser?.uid!!))
                                        getDataUser(mAuth.currentUser!!.uid)
                                        Toast.makeText(this@AuthBase, "регистрация завершена", Toast.LENGTH_SHORT).show()
                                    }
                                    Aim.signIn ->
                                    {
                                        getDataUser(mAuth.currentUser!!.uid)
                                    }
                                    else -> { }
                                }

                                if (task.exception is FirebaseAuthInvalidCredentialsException)
                                {
                                    Toast.makeText(this@AuthBase, "Ошибка!", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } // кнопка положительного ответа
                    show() // создаем диалог
                }

            }
            override fun onCodeAutoRetrievalTimeOut(p0: String)
            {
                super.onCodeAutoRetrievalTimeOut(p0)
            }
        }

        val options = PhoneAuthOptions.newBuilder()
            .setPhoneNumber(phone)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    fun getDataUser(uid: String)
    {
        var ref = db.collection(RequestManager.USERS_TAG)
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
                    // Notification.subscribeTopic(Notification.TOPIC_FOR_VOLONTER)
                    startActivity(Intent(this@AuthBase, MainActivity::class.java))
                    finish()
                }
            }
            .addOnFailureListener {
                Log.d("TESTTEXT", "WOW")
            }
    }

    fun createUser(uid: String) = User(
        name.text.toString(),
        phone.text.toString(),
        adress.text.toString(),
        getUserCategory(),
        uid,
        true
    )

    fun getUserCategory() : USER_CATEGORY =
        when (role_radio_group.checkedRadioButtonId)
        {
            R.id.radioButtonVeteran -> USER_CATEGORY.VETERAN
            R.id.radioButtonVolonteer -> USER_CATEGORY.VOLONTEER
            else -> USER_CATEGORY.ADMIN
        }

    override fun onStart()
    {
        super.onStart()
        val currentUser = mAuth.currentUser
    }
}