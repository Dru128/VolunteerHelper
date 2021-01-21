package com.arbonik.helper.auth

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.arbonik.helper.MainActivity
import com.arbonik.helper.R
import com.arbonik.helper.helprequest.RequestManager
import com.arbonik.helper.system.Format
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.*
import java.util.concurrent.TimeUnit

enum class Aim{register, signIn} //намерение: регистрации или входа
abstract class AuthBase : AppCompatActivity()
{
    var mAuth = FirebaseAuth.getInstance()
    var db = FirebaseFirestore.getInstance()
    val sharedPreferenceUser = SharedPreferenceUser()
    val userDataFirebase = UserDataFirebase()
    var geoPoint: GeoPoint? = null

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

    public fun authUser(phone: String, aim: Aim) // вызывается в registration и signIn Activity
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
                    val codeText = EditText(this@AuthBase) // текст для ввода типа заявки
                        codeText.inputType = InputType.TYPE_CLASS_PHONE
                    setView(codeText) // установка текста в диалог
                    setCancelable(false) // не закрывать диалог при нажатии на фон
                    setTitle( if (aim == Aim.register) R.string.registration else R.string.signin ) // заголовок
                    setMessage(R.string.writecode) // сообщение
                    setNeutralButton("отмена") { _, _ -> }  // кнопка нейтрального ответа
                    setPositiveButton("ок")
                    { _, _ ->
                        val credential = PhoneAuthProvider.getCredential(verificationId, codeText.text.toString())
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

    private fun getDataUser(uid: String)
    {
        var ref = db.collection(RequestManager.USERS_TAG)
        var query = ref.document(uid)

        query.get()
            .addOnSuccessListener(this) {
                var result = it.data
                result?.let {
                    var returnUser = User(
                        name = result[User.NAME_TAG.toLowerCase(Locale.ROOT)].toString(),
                        phone = result[User.TAG_PHONE.toLowerCase(Locale.ROOT)].toString(),
                        comment = null/*result[User.TAG_ADDRESS.toLowerCase(Locale.ROOT)].toString()*/,
                        rating = 0f/*result[User.RATING_TAG.toLowerCase(Locale.ROOT)].toString().toFloat()*/,
                        category = USER_CATEGORY_CREATER(result[User.TAG_CATEGORY.toLowerCase(Locale.ROOT)].toString()),
                        uid = result[User.TAG_UID.toLowerCase(Locale.ROOT)].toString(),
                        notification = result[User.TAG_NOTFICATION.toLowerCase(Locale.ROOT)].toString().toBoolean()
                    )
                    if (getUserCategory() == USER_CATEGORY.VETERAN) returnUser.location = result[User.TAG_NOTFICATION.toLowerCase(Locale.ROOT)] as GeoPoint?
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

    private fun createUser(uid: String) = User(
        name = RegData.name,
        phone = RegData.phone.toString(),
        comment = null,
        rating = null,
        category = getUserCategory(),
        uid = uid,
        location = RegData.location,
        notification = true

    )

    private fun getUserCategory() = RegData.typeUser

    override fun onStart()
    {
        super.onStart()
        val currentUser = mAuth.currentUser
    }
}