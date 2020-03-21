package com.arbonik.helper.ui.home

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.arbonik.helper.R

open class SettengsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.xml.fragment_settings)
//        val name = findViewById<EditText>(R.id.nameEdit)
//        val phone = findViewById<EditText>(R.id.phoneEdit)
    }

    companion object{
        fun createAuth(): SettengsActivity{
            var temp = SettengsActivity()
            return temp
        }
    }

}