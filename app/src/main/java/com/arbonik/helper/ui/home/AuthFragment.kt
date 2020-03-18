package com.arbonik.helper.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.arbonik.helper.FireDatabase
import com.arbonik.helper.R

open class AuthFragment : Fragment() {

    companion object{
        fun createAuth(): AuthFragment{
            var temp = AuthFragment()
            return temp
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(
            R.layout.fragment_auth,
            container,
            false)
        val name = view.findViewById<EditText>(R.id.nameEdit)
        val phone = view.findViewById<EditText>(R.id.phoneEdit)

        view.findViewById<Button>(R.id.goAuth).setOnClickListener { v ->
            val ref = FireDatabase.database.getReference("account")
            ref.setValue(AuthData(name.text.toString(), phone.text.toString()
            ))
        }

        return view
    }
}

class AuthData(val login : String, val number : String){

}