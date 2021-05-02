package com.arbonik.helper.auth

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.arbonik.helper.MainActivity
import com.arbonik.helper.R
import com.arbonik.helper.helprequest.RequestManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.safetynet.SafetyNet
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.main.activity_registration.*
import org.json.JSONObject
import java.util.*
import java.util.EnumSet.of
import java.util.concurrent.TimeUnit
import javax.crypto.Cipher.SECRET_KEY


open class AuthBase : AppCompatActivity()
{
    var mAuth = FirebaseAuth.getInstance()
    var db = FirebaseFirestore.getInstance()
    val sharedPreferenceUser = SharedPreferenceUser()
    val userDataFirebase = UserDataFirebase()
    var queue: RequestQueue? = null
    var verify: Boolean = false
    val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        mAuth.setLanguageCode("ru")
        queue = Volley.newRequestQueue(applicationContext)
    }

    private fun verifyGoogleReCAPTCHA()
    {

        // строка ниже используется для обеспечения нашей безопасности
        // чистый клиент и проверьте с помощью reCAPTCHA
        SafetyNet.getClient(this).verifyWithRecaptcha(getString(R.string.site_key))
            .addOnSuccessListener { response ->
                // в строке ниже мы проверяем токен ответа.
                if (response.tokenResult.isNotEmpty())
                {
                    // если токен ответа не пустой, то мы
                    // вызываем наш метод проверки.
                     handleVerification(response.tokenResult)
                }
            }
            .addOnFailureListener { e ->
                // this method is called when we get any error.
                if (e is ApiException)
                {
                    val apiException = e
                    // below line is use to display an error message which we get.
                    Log.d("TAG", "Error message: " + CommonStatusCodes.getStatusCodeString(apiException.statusCode))
                }
                else
                {
                    // строка ниже используется для отображения всплывающего сообщения при любой ошибке.
                    Toast.makeText(this, "Error found is : $e", Toast.LENGTH_SHORT).show()
                }
            }
    }

    protected open fun handleVerification(responseToken: String)
    {
        // метод проверки внутреннего дескриптора мы
        // проверяем вашего пользователя с помощью токена ответа.
        // URL-адрес для отправки ключа нашего сайта и секретного ключа
        // на указанный ниже URL-адрес с помощью метода POST.
        val url = "https://www.google.com/recaptcha/api/siteverify"
        // здесь мы делаем строковый запрос и
        // использование метода post для передачи данных.
        val request: StringRequest =
            object : StringRequest(Request.Method.POST, url, Response.Listener<String?>
            { response ->
                    // внутри метода ответа мы проверяем,
                    // ответ успешен или нет.
                    try
                    {
                        val jsonObject = JSONObject(response)
                        if (jsonObject.getBoolean("success"))
                        {
                            // если ответ успешен, то мы
                            // показываем ниже всплывающее сообщение.
                            Toast.makeText(this, "User verified with reCAPTCHA", Toast.LENGTH_SHORT).show()
                            verify = true
                            authUser(authViewModel.phone!!, authViewModel.aim!!)
                        }
                        else
                        {
                            // если ответ в случае сбоя мы отображаем
                            // всплывающее сообщение ниже.
                            Toast.makeText(applicationContext, jsonObject.getString("error-codes").toString(), Toast.LENGTH_LONG).show()
                        }
                    }
                    catch (ex: Exception)
                    {
                        // если мы получим какое-либо исключение, то мы
                        // отображение сообщения об ошибке в logcat.
                        Log.d("TAG", "JSON exception: " + ex.message)
                    }
                },
                Response.ErrorListener { error ->
                    // внутри сообщения об ошибке мы отображаем
                    // сообщение журнала в нашем logcat.
                Log.d("TAG", "Error message: " + error.message)
            })
            {
                // ниже приведен метод getParamns, в котором мы будем
                // передаем токен ответа и секретный ключ по указанному выше URL.
                override fun getParams(): Map<String, String>?
                {
                    // мы передаем данные с помощью hashmap
                    // пара ключ и значение.
                    val params: MutableMap<String, String> = HashMap()
                    params["secret"] = getString(R.string.secret_key)
                    params["response"] = responseToken
                    return params
                }
            }
        // строка кода ниже используется для установки повтора
        // политика, если api не сработает с одной попытки.
        request.retryPolicy = DefaultRetryPolicy(
            // мы устанавливаем время для повтора 5 секунд.
            50000,
            // строка ниже предназначена для выполнения максимального числа попыток.
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        // наконец-то мы добавляем наш запрос в очередь.
        queue!!.add(request)
    }

    fun authUser(phone: String, aim: Aim) // вызывается в registration и signIn Activity
    {

        authViewModel.aim = aim
        authViewModel.phone = phone
        if (!verify)
        {
            verifyGoogleReCAPTCHA()
            return
        }


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
                    setTitle(if (aim == Aim.register) R.string.registration else R.string.signin) // заголовок
                    setMessage(R.string.writecode) // сообщение
                    setNeutralButton("отмена") { _, _ -> }  // кнопка нейтрального ответа
                    setPositiveButton("ок")
                    { _, _ ->
                        val credential = PhoneAuthProvider.getCredential(
                            verificationId, codeText.text.toString()
                        )
                        mAuth.signInWithCredential(credential)
                            .addOnCompleteListener(this@AuthBase)
                            { task ->
                                when (aim)
                                {
                                    Aim.register ->
                                    {
                                        userDataFirebase.addUser(createUser(mAuth.currentUser?.uid!!))
                                        getDataUser(mAuth.currentUser!!.uid)
                                        Toast.makeText(
                                            this@AuthBase,
                                            "регистрация завершена",
                                            Toast.LENGTH_SHORT
                                        ).show()
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
                        inf = result[User.TAG_INF.toLowerCase(Locale.ROOT)].toString(),
                        rating = 0f/*result[User.RATING_TAG.toLowerCase(Locale.ROOT)].toString().toFloat()*/,
                        category = USER_CATEGORY_CREATER(result[User.TAG_CATEGORY.toLowerCase(Locale.ROOT)].toString()),
                        uid = result[User.TAG_UID.toLowerCase(Locale.ROOT)].toString(),
                        notification = result[User.TAG_NOTFICATION.toLowerCase(Locale.ROOT)].toString()
                            .toBoolean(),
                        location = result[User.TAG_LOCATION.toLowerCase(Locale.ROOT)] as GeoPoint?
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

    private fun createUser(uid: String) = User(
        name = RegData.name,
        phone = RegData.phone.toString(),
        inf = RegData.inf,
        rating = null,
        category = getUserCategory(),
        uid = uid,
        location = if (RegData.typeUser == USER_CATEGORY.VETERAN) RegData.location else /*GeoPoint(0.0, 0.0)*/ null,
        notification = true
    )

    private fun getUserCategory() = RegData.typeUser

    override fun onStart()
    {
        super.onStart()
        val currentUser = mAuth.currentUser
    }
}

enum class Aim{register, signIn} // намерение: регистрации или входа

class AuthViewModel : ViewModel()
{
    var phone = ""
    var aim: Aim? = null
}