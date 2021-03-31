package com.arbonik.helper.system

import android.view.Gravity
import androidx.fragment.app.DialogFragment
import com.arbonik.helper.R

open class CustomDialog(val dimAmount: Float) : DialogFragment()
{
    override fun onStart()
    {
        dialog?.apply {
            setCanceledOnTouchOutside(false)
        }
        dialog?.window?.apply {
            setDimAmount(dimAmount)
            setBackgroundDrawableResource(R.drawable.around)
        }
        super.onStart()
    }
}
