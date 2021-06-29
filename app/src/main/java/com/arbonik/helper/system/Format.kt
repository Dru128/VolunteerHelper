package com.arbonik.helper.system

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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

        fun latLngTogeoPoint(latLng: LatLng) = GeoPoint(latLng.latitude, latLng.longitude)
        fun geoPointTolatLng(geoPoint: GeoPoint) = LatLng(geoPoint.latitude, geoPoint.longitude)

        fun makeMaskEditTest(text: EditText)
        {
            val slots = UnderscoreDigitSlotsParser().parseSlots("+7 (___) ___-__-__")
            val formatWatcher: FormatWatcher = MaskFormatWatcher(MaskImpl.createTerminated(slots))
            formatWatcher.installOn(text) // устанавливаем форматер на любой EditText
        }

        fun makeMaskPhone(phone: String): String
        {
            var p = phone
            if (phone.length == 12)
            {
                p = StringBuilder(p).insert(p.length - 10, " (").toString()
                p = StringBuilder(p).insert(p.length - 7, ") ").toString()
                p = StringBuilder(p).insert(p.length - 4, "-").toString()
                p = StringBuilder(p).insert(p.length - 2, "-").toString()
            }
                return p
        }
    }
}
