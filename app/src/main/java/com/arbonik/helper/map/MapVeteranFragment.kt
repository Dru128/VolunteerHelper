package com.arbonik.helper.map

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.arbonik.helper.R
import com.arbonik.helper.auth.RegData
import com.arbonik.helper.auth.USER_CATEGORY
import com.arbonik.helper.auth.User
import com.arbonik.helper.system.Format
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapVeteranFragment: MapsFragment()
{
    var curLocation: LatLng? = null
    var myMacker: Marker? = null

    override fun creatAll()
    {
        curLocation = requireArguments().getParcelable(User.TAG_LOCATION)
        if (curLocation != null) setMyMackerPosition(curLocation!!)

        if (TYPE_USER == null)
        {

            google_map?.setOnMapClickListener { setMyMackerPosition(it) }
            google_map?.setOnMyLocationClickListener { setMyMackerPosition(LatLng(it.latitude, it.longitude)) }

            val acceptPositionButton = Button(context)
            acceptPositionButton.apply {
                setBackgroundResource(R.drawable.button_blue_radius)
                setTextColor(Color.WHITE)
                text = getString(R.string.choose)
                setOnClickListener {
                    if (curLocation != null)
                    {
                        RegData.location = Format.latLngTogeoPoint(curLocation!!)
                        navController.popBackStack()
                        onDestroy()
                    }
                    else
                        Toast.makeText(context, getString(R.string.not_shoose), Toast.LENGTH_SHORT).show()
                }
            }

            map_layout_child?.addView(acceptPositionButton)
        }

    }

    private fun setMyMackerPosition(position: LatLng)
    {
        curLocation = position
        if (myMacker == null) myMacker = google_map?.addMarker(MarkerOptions().position(position))
        else myMacker!!.position = position
        moveCamera(position)
    }
}