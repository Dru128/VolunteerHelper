package com.arbonik.helper.auth

import android.app.Dialog
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.arbonik.helper.R
import com.arbonik.helper.system.CustomDialog

class DialogPrivacyPolicy(dimAmount: Float) : CustomDialog(dimAmount)
{
    lateinit var message: TextView
    lateinit var ok: Button

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

    private fun initViews(parent: View)
    {
        parent.apply {
            ok = findViewById(R.id.accept_button)
            message = findViewById(R.id.inf_text_privacy)
        }
        ok.text = getString(R.string.accept)
        ok.setOnClickListener{ onDestroyView() }
        message.movementMethod = LinkMovementMethod.getInstance()
    }
}