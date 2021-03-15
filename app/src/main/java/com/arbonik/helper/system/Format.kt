package com.arbonik.helper.system

import android.widget.EditText
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher

class Format()
{
    companion object
    {
        fun format_number(number: String) = number.replace(Regex("[-_() ]")) {
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

        fun makeMaskEditTest(text: EditText)
        {
            val slots = UnderscoreDigitSlotsParser().parseSlots("+7 (___) ___-__-__")
            val formatWatcher: FormatWatcher = MaskFormatWatcher(MaskImpl.createTerminated(slots))
            formatWatcher.installOn(text) // устанавливаем форматер на любой EditText
        }

        fun latLng_to_geoPoint(latLng: LatLng) = GeoPoint(latLng.latitude, latLng.longitude)
        fun geoPoint_to_latLng(geoPoint: GeoPoint) = LatLng(geoPoint.latitude, geoPoint.longitude)

        fun makeMaskTextView(phone: String): String
        {
            var p = phone
            p = StringBuilder(p).insert(p.length - 10, " (").toString()
            p = StringBuilder(p).insert(p.length - 7, ") ").toString()
            p = StringBuilder(p).insert(p.length - 4, "-").toString()
            p = StringBuilder(p).insert(p.length - 2, "-").toString()
            return p
        }
    }
}
