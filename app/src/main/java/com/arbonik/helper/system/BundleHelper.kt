package com.arbonik.helper.system

import android.os.Bundle
import com.arbonik.helper.auth.STATUS_ACCOUNT_CREATER
import com.arbonik.helper.auth.USER_CATEGORY
import com.arbonik.helper.auth.USER_CATEGORY_CREATER
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
            putString(User.NAME_TAG, data.master.name)
            putString(User.TAG_PHONE, data.master.phone)
            putString(User.TAG_INF, data.master.inf)
            putDouble(User.TAG_LOCATION_LON, data.master.location!!.longitude)
            putDouble(User.TAG_LOCATION_LAT, data.master.location!!.latitude)
            putBoolean("status", data.status)
            putString(User.TAG_UID, data.master.uid)
            putString(User.TAG_CATEGORY, data.master.category.toString())
            putString(User.TAG_STATUS_ACCOUNT, data.master.status_account.toString())
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
                    name = getString(User.NAME_TAG),
                    phone = getString(User.TAG_PHONE),
                    inf = getString(User.TAG_INF),
                    uid = getString(User.TAG_UID),
                    location = GeoPoint(
                        getDouble(User.TAG_LOCATION_LAT),
                        getDouble(User.TAG_LOCATION_LON)
                    ),
                    category = USER_CATEGORY_CREATER(getString(User.TAG_CATEGORY).toString()),
                    notification = null,
                    rating = null,
                    status_account = STATUS_ACCOUNT_CREATER(getString(User.TAG_STATUS_ACCOUNT).toString())
                )
            )
            return data
        }
    }
}