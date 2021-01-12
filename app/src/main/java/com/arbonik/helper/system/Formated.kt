package com.arbonik.helper.system

import android.widget.EditText
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher

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

        fun makeMask(edittext: EditText)
        {
            val slots = UnderscoreDigitSlotsParser().parseSlots("+7 (___) ___-__-__")
            val formatWatcher: FormatWatcher = MaskFormatWatcher(MaskImpl.createTerminated(slots))
            formatWatcher.installOn(edittext) // устанавливаем форматер на любой EditText
        }
    }
}