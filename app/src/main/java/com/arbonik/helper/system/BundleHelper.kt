package com.arbonik.helper.system

import android.os.Bundle
import androidx.core.os.bundleOf
import com.arbonik.helper.auth.USER_CATEGORY
import com.arbonik.helper.auth.User
import com.arbonik.helper.helprequest.RequestData
import com.google.firebase.firestore.GeoPoint

object BundleHelper
{
    fun putRequestData(data: RequestData): Bundle
    {
        val bundle = Bundle()
        bundle.apply {
            putString("title", data.title)
            putString("comment", data.comment)
            putString("date", data.date)
            putString("name", data.master.name)
            putString("phone", data.master.phone)
            putString("inf", data.master.inf)
            putDouble("longitude", data.master.location!!.longitude)
            putDouble("latitude", data.master.location!!.latitude)
            putBoolean("status", data.status)
            putString("uid", data.master.uid)
            putString("user_category", data.master.category.toString())
        }
        return bundle
    }

    fun getRequestData(bundle: Bundle): RequestData
    {

        bundle.apply {
            var data = RequestData(
                status = getBoolean("status"),
                title = getString("title").toString(),
                comment = getString("comment").toString(),
                date = getString("date").toString(),
                master = User(
                    name = getString("name"),
                    phone = getString("phone"),
                    inf = getString("inf"),
                    uid = getString("uid"),
                    location = GeoPoint(
                        getDouble("latitude"),
                        getDouble("longitude")
                    ),
                    category = USER_CATEGORY.valueOf(getString("user_category")!!),
                    notification = null,
                    rating = null
                )
            )
            return data
        }
    }
}