package com.arbonik.helper.system

import android.app.Dialog
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.arbonik.helper.R

open class MarkDialog : DialogFragment()
{
    lateinit var  ok: Button
    lateinit var  cansel: Button
    lateinit var  not_ak: CheckBox
    lateinit var  ratingBar: RatingBar
    open var dimAmount: Float = 0.75f

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {

        val builder = AlertDialog.Builder(requireActivity())
        builder.apply {
            setTitle("Пожалуйста, оцените работу волонтёра")
            //                setMessage(PreferenceManager.getDefaultSharedPreferences(context).getBoolean("ask_rating", true).toString())
            val dialogLayout: View = requireActivity().layoutInflater.inflate(R.layout.raiting_dialog, null)
            setView(dialogLayout)
            initViews(dialogLayout)

        }
        return builder.create()
    }

    private fun initViews(parent: View)
    {
        parent.apply {
            ok = findViewById(R.id.OK)
            cansel = findViewById(R.id.cansel)
            not_ak = findViewById(R.id.not_ask)
            ratingBar = findViewById(R.id.ratingBar)
        }
        cansel.setOnClickListener{ onDestroyView() }
        ok.setOnClickListener{
            val mark = ratingBar.rating
            if (mark != 0f)
            {
//                                Toast.makeText(context)
                ok.text = mark.toString()
                onDestroyView()
            }
            else
            {
                Toast.makeText(context, "Выберите оценку", Toast.LENGTH_SHORT)
                    .apply {
                        setGravity(Gravity.CENTER, 0, 0)
                        show()
                    }
            }
        }
    }

    override fun onDestroyView()
    {
        if (not_ak.isChecked)
            PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putBoolean("ask_rating", false)
                .apply()
        super.onDestroyView()
    }

    override fun onStart()
    {
        dialog?.window?.apply {
            setDimAmount(dimAmount)
            setBackgroundDrawableResource(R.drawable.around)
            setGravity(Gravity.BOTTOM)
        }
        super.onStart()
    }
}
