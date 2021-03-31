package com.arbonik.helper.auth

import android.app.Dialog
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.arbonik.helper.R
import com.arbonik.helper.system.CustomDialog
import kotlin.system.exitProcess

class DialogPrivacyPolicy(dimAmount: Float) : CustomDialog(dimAmount)
{
    lateinit var message: TextView
    lateinit var ok: Button
    var accept_policy = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {

        val builder = AlertDialog.Builder(requireActivity())
        builder.apply {
            setTitle("${getString(R.string.welcome)}\n«${getString(R.string.app_name)}»")
            val dialogLayout: View = requireActivity().layoutInflater.inflate(R.layout.privacy_policy_layout, null)
            setView(dialogLayout)
            initViews(dialogLayout)

        }
        return builder.create()
    }

    override fun onDestroyView()
    {
        if (!accept_policy) exitProcess(0) // если пользователь не принял политику конфиденциальности закрыть приложение
        super.onDestroyView()
    }


    private fun initViews(parent: View)
    {
        parent.apply {
            ok = findViewById(R.id.accept_button)
            message = findViewById(R.id.inf_text_privacy)
        }
        ok.text = getString(R.string.accept)
        ok.setOnClickListener {
            accept_policy = true
            onDestroyView()
        }
        message.movementMethod = LinkMovementMethod.getInstance()
    }
}