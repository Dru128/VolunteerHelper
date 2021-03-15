package com.arbonik.helper.ui

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.navigation.findNavController
import com.arbonik.helper.Map.MapsFragment
import com.arbonik.helper.R
import com.arbonik.helper.helprequest.RequestData
import com.arbonik.helper.system.Format
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapVolonteerFragment : MapsFragment()
{
    override fun creatAll()
    {


        val acceptRequestButton = Button(context)
        acceptRequestButton.apply {
            setBackgroundResource(R.drawable.button_blue_radius)
            text = getString(R.string.accept_request)
            setTextColor(Color.WHITE)
            setOnClickListener {
                it.findNavController().navigate(R.id.action_map_volonteer_fragment_to_navigation_request_vol)
            }
        }

        map_layout_child?.addView(acceptRequestButton)




            val request = requireArguments().getSerializable("request") as RequestData

            val marker = google_map!!.addMarker(
                MarkerOptions().position(Format.geoPoint_to_latLng(request.master.location!!))
            )

            google_map!!.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter
            {
                override fun getInfoContents(marker: Marker) = create_marker_layout(request)

                override fun getInfoWindow(p0: Marker?) = null
            })
            moveCamera(marker!!.position)
            marker.showInfoWindow()
            //                    super.creatAll()

    }

    fun create_marker_layout(request: RequestData): LinearLayout
    {
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        layout.setBackgroundColor(Color.WHITE)
        val text_title = TextView(context)
        val text_snippet = TextView(context)

        request.apply {
            text_title.setTextColor(Color.BLACK)
            text_title.setTypeface(null, Typeface.BOLD)
            text_title.text = title
            text_title.gravity = Gravity.CENTER

            text_snippet.text = "${master.name} \n ${Format.makeMaskTextView(master.phone.toString())} \n ${master.inf} \n $date \n $comment"
            text_snippet.gravity = Gravity.CENTER
            text_snippet.setTextColor(Color.GRAY)
            text_snippet.maxWidth = 600
        }

        // сделать заявку принятой

        layout.addView(text_title)
        layout.addView(text_snippet)

        return layout
    }
}