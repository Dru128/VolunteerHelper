package com.arbonik.helper.admin

import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore


class TypeRequest(var type: String, val key: String)

class FireBaseTypesRequest()
{
    var reference = FirebaseFirestore.getInstance().collection("TYPE_HELP")

    fun addTypeHelp(t: String, context: Context)
    {
        // добавить тип помощи  в БД (он будет использоваться для выгрузки вариантов помощи на устройстве клиента)
        reference.add(hashMapOf("type" to t))
            .addOnSuccessListener {
                Toast.makeText(context, "Тип помощи добавлен", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(context, "Ошибка: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }


    fun deleteTypeHelp(key: String)
    {
        reference.document(key).delete()
    }
}

