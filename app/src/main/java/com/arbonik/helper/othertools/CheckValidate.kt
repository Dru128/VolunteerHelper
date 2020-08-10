package com.arbonik.helper.othertools

import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.arbonik.helper.R

class CheckValidate
{
    companion object
    {
        fun checkDataInput(allView: Sequence<View>): Boolean
        {
            for (i in allView)
            {
                if (i is EditText)
                {
                    if (i.text.isEmpty())
                    {
                        return false
                    }
                }
            }
            return true
        }
    }
}