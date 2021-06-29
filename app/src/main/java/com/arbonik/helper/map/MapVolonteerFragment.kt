package com.arbonik.helper.map

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.arbonik.helper.R
import com.arbonik.helper.auth.SharedPreferenceUser
import com.arbonik.helper.helprequest.RequestData
import com.arbonik.helper.system.BundleHelper
import com.arbonik.helper.system.Format
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore


class MapVolonteerFragment : MapsFragment()
{

    var ref = FirebaseFirestore.getInstance().collection("requests")
    override fun creatAll()
    {
        val request = BundleHelper.getRequestData(requireArguments().getBundle("request")!!)
        val request_id = requireArguments().getString("request_id").toString()

            val acceptRequestButton = Button(context)
            acceptRequestButton.apply {
                setBackgroundResource(R.drawable.button_blue_radius)
                setTextColor(Color.WHITE)
                text = if (request.status) getString(R.string.decline_request) else getString(R.string.accept_request)
                setOnClickListener {
                    if (request.status)
                    {
                        ref.document(request_id).update(
                            mapOf(
                                "status" to false,
                                "accepter" to ""
                            )
                        )
                    }
                    else
                    {
                        ref.document(request_id).update(
                            mapOf(
                                "status" to true,
                                "accepter" to SharedPreferenceUser.currentUser?.uid
                            )
                        )
                    }
                    navController.popBackStack()
                    onDestroy()
                }
            }

        map_layout_child?.addView(acceptRequestButton)

            val marker = google_map!!.addMarker(
                MarkerOptions().position(
                    Format.geoPointTolatLng(
                        request.master.location!!
                    )
                )
            )

            google_map!!.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter
            {
                override fun getInfoContents(marker: Marker) = create_marker_layout(request)
                override fun getInfoWindow(p0: Marker?) = null
            })
            moveCamera(marker!!.position)
            marker.showInfoWindow()


    }

    private fun create_marker_layout(request: RequestData): LinearLayout
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

            text_snippet.text = "${master.name} \n ${Format.makeMaskPhone(master.phone.toString())} \n ${master.inf} \n $date \n $comment"
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