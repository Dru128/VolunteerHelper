package com.arbonik.helper.system

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint


class Format()
{
    companion object
    {
        fun latLng_to_geoPoint(latLng: LatLng) = GeoPoint(latLng.latitude, latLng.longitude)
        fun geoPoint_to_latLng(geoPoint: GeoPoint) = LatLng(geoPoint.latitude, geoPoint.longitude)

        fun makeMaskTextView(phone: String): String
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
