package com.arbonik.helper.system

class Format()
{
    companion object
    {
        fun format_number(number: String) = number.replace(Regex("[-_() ]"))
        {
            when (it.value)
            {
                "-" -> ""
                "_" -> ""
                "(" -> ""
                ")" -> ""
                " " -> ""
                else -> it.value
            }
        }
    }
}