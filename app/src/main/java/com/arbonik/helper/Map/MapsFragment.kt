package com.arbonik.helper.Map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

import com.arbonik.helper.R
import com.arbonik.helper.auth.SharedPreferenceUser
import com.arbonik.helper.auth.USER_CATEGORY
import com.arbonik.helper.auth.User
import com.arbonik.helper.helprequest.RequestManager
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

import java.util.*


class MapsFragment() : Fragment(),
    OnMyLocationButtonClickListener,
    OnMyLocationClickListener,
    OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMarkerDragListener,
    GoogleMap.OnMyLocationChangeListener
{
    var curLocation: LatLng? = null
    var myMacker: Marker? = null
    private lateinit var google_map: GoogleMap
    private val db = FirebaseFirestore.getInstance().collection(RequestManager.USERS_TAG)
    private var permission_GPS: Boolean = false
    private val TYPE_USER: USER_CATEGORY? = SharedPreferenceUser.currentUser?.category

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        var root = inflater.inflate(R.layout.fragment_maps, container, false)
        root.apply {
            val type_map_switch = findViewById<SwitchCompat>(R.id.tipy_map)
            type_map_switch.setOnClickListener {
                if (type_map_switch.isChecked) setMapType(GoogleMap.MAP_TYPE_HYBRID)
                else setMapType(GoogleMap.MAP_TYPE_NORMAL)
            }
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        initMap()
    }

    private fun initMap()
    {
        permission_GPS = ContextCompat.checkSelfPermission(requireContext(),  Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        var mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync { googleMap ->
            google_map = googleMap
            googleMap.apply {
                if (curLocation != null) setMyMackerPosition(curLocation!!)
                uiSettings.isZoomControlsEnabled = true
                uiSettings.isMyLocationButtonEnabled = true

//                setOnMarkerClickListener(this@MapsFragment)
                setOnMyLocationButtonClickListener(this@MapsFragment)
                setOnMyLocationClickListener(this@MapsFragment)
                setOnMyLocationChangeListener(this@MapsFragment)
                if (permission_GPS)
                {
                    isMyLocationEnabled = true
                }
                else ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            }

            when (TYPE_USER)
            {
                USER_CATEGORY.VOLONTEER ->
                {

                }
                USER_CATEGORY.VETERAN, null ->
                {
                    googleMap.setOnMapClickListener { setMyMackerPosition(it) }
/*                    if (permission_GPS)
                    {
                        //                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curLocation, 5f))
                        AlertDialog.Builder(context).apply {
                            setTitle("выбрать текущее местоположение?")
                            setPositiveButton("да, выбрать") { _, _ ->
                                onMyLocationButtonClick()
                                //                                val geoPoint = LatLng(googleMap.myLocation.latitude, googleMap.myLocation.longitude)

                            }
                            setNegativeButton("отмена") { _, _ -> }
                            show()
                        }
                    }*/
                }
                USER_CATEGORY.ADMIN ->
                {
                    db
                        .whereEqualTo(User.TAG_CATEGORY, USER_CATEGORY.VETERAN)
                        .get().addOnSuccessListener { documents ->
                            for (document in documents)
                            {
                                val location =
                                    document[User.TAG_LOCATION.toLowerCase(Locale.ROOT)] as GeoPoint
                                googleMap.addMarker(
                                    MarkerOptions()
                                        .position(LatLng(location.latitude, location.longitude))
                                        .title(document[User.NAME_TAG.toLowerCase(Locale.ROOT)].toString())
                                        .snippet(
                                            "телефон: ${document[User.TAG_PHONE.toLowerCase(Locale.ROOT)].toString()}" +
                                            "   адрес: ${document[User.INF_TAG.toLowerCase(Locale.ROOT)].toString()}"
                                        )
                                )
                            }
                        }
                }
                else -> { }
            }
        }
    }

    fun setMapType(type: Int) { google_map.mapType = type }

    fun setMyMackerPosition(position: LatLng)
    {
        curLocation = position
        if (myMacker == null) myMacker = google_map.addMarker(MarkerOptions().position(position))
        else myMacker!!.position = position
                google_map.animateCamera(CameraUpdateFactory.newLatLng(position))
        Toast.makeText(context, getString(R.string.modify_position), Toast.LENGTH_SHORT).show()
    }

//--------------------------------------------------------| интерфейсы |--------------------------------------------------------
    override fun onMyLocationButtonClick(): Boolean = false

    override fun onMyLocationClick(position: Location)
    {
        setMyMackerPosition(LatLng(position.latitude, position.longitude))
    }

    override fun onMyLocationChange(point: Location?) { }

    override fun onMapReady(macker: GoogleMap?)
    {

    }

    override fun onMarkerClick(macker: Marker?): Boolean
    {
        google_map.animateCamera(CameraUpdateFactory.newLatLng(macker?.position))
        return true
    }

    override fun onMarkerDragStart(macker: Marker?) { }
    override fun onMarkerDrag(macker: Marker?) { }
    override fun onMarkerDragEnd(macker: Marker?) { }
}
