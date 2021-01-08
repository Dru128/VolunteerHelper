package com.arbonik.helper.auth

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.arbonik.helper.Map.MapsFragment
import com.arbonik.helper.R


open class ChooseLocationDialog : DialogFragment()
{
    open var dimAmount: Float = 0.9f

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        val builder = AlertDialog.Builder(requireActivity())
        builder.apply {
            setTitle("")

            setNegativeButton("Закрыть") { _, _ ->

            }
            setPositiveButton("Выбрать") { _, _ ->

            }

            var layout = LinearLayout(context)
            layout.id = View.generateViewId()
            var map = MapsFragment()
            childFragmentManager
                .beginTransaction()
                .add(layout.id, map)
                .commit()
            setView(layout)
/**
 * нужно com.arbonik.helper.Map.MapsFragment
 * разместить в диалоге на весь экран
 * */
        }


        return builder.create()
    }

    override fun onStart()
    {
        dialog?.window?.apply {
            setDimAmount(dimAmount) // яркость
        }
        super.onStart()
    }
}