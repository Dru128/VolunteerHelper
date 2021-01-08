package com.arbonik.helper.Map

import android.Manifest
import android.app.AlertDialog
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
import com.google.android.gms.maps.model.CameraPosition
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
    private var myMacker: Marker? = null
    private lateinit var google_map: GoogleMap
    private lateinit var curLocation: LatLng
    private val db = FirebaseFirestore.getInstance().collection(RequestManager.USERS_TAG)
    private var permission_GPS: Boolean = false
    private val TYPE_USER: USER_CATEGORY? = null //SharedPreferenceUser.currentUser!!.category
    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Манипулирует картой, как только она становится доступной.
         * Этот обратный вызов срабатывает, когда карта готова к использованию.
         * Здесь мы можем добавлять маркеры или линии, добавлять слушателей или перемещать камеру.
         * В этом случае мы просто добавим маркер рядом с Сиднеем, Австралия.
         * Если Google Play services не установлен на устройстве, пользователю будет предложено
         * установить его внутри SupportMapFragment. Этот метод будет запущен только
         * после того, как пользователь установит сервисы Google Play и вернется в приложение.
         */



    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        var root = inflater.inflate(R.layout.fragment_maps, container, false)
        root.apply {
            var myswithc = findViewById<SwitchCompat>(R.id.switch12)
            myswithc.setOnClickListener {
                if (myswithc.isChecked) initMap(GoogleMap.MAP_TYPE_HYBRID)
                else initMap(GoogleMap.MAP_TYPE_NORMAL)
            }
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        initMap(GoogleMap.MAP_TYPE_NORMAL)
    }

    private fun initMap(map_type: Int)
    {
        permission_GPS = ContextCompat.checkSelfPermission(requireContext() ,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        var mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync { googleMap ->
            google_map = googleMap
            googleMap.apply {
                mapType = map_type
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
                USER_CATEGORY.VETERAN ->
                {
                    googleMap.setOnMapClickListener {
                        if (myMacker == null)
                        {
                            myMacker = googleMap.addMarker(MarkerOptions().position(it))
                        }
                        else
                        {
                            googleMap.animateCamera(CameraUpdateFactory.newLatLng(it))
                            myMacker!!.position = it
                        }
                        curLocation = it
                        Toast.makeText(context, getString(R.string.modify_position), Toast.LENGTH_SHORT).show()
                    }
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
                        //                        .whereEqualTo(User.TAG_CATEGORY, USER_CATEGORY.VETERAN)
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
                                            "   адрес: ${document[User.TAG_ADDRESS.toLowerCase(Locale.ROOT)].toString()}"
                                        )
                                )
                            }
                        }
                }
                else ->
                {

                }
            }
        }
    }

    fun move_camera(lat: Double, lon: Double)
    {
        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(lat, lon))
            .build()
        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
        google_map.animateCamera(cameraUpdate)
    }
//--------------------------------------------------------| интерфейсы |--------------------------------------------------------
    override fun onMyLocationButtonClick(): Boolean = false

    override fun onMyLocationClick(point: Location)
    {
        val geoPoint = LatLng(point.latitude, point.longitude)
        curLocation = geoPoint
        google_map.animateCamera(CameraUpdateFactory.newLatLng(geoPoint))
        myMacker!!.position = geoPoint
        Toast.makeText(context, getString(R.string.modify_position), Toast.LENGTH_SHORT).show()
    }

    override fun onMyLocationChange(point: Location?)
    {
        if (point != null)
            curLocation = LatLng(point.latitude, point.longitude)

    }


    override fun onMapReady(macker: GoogleMap?)
    {

    }

    override fun onMarkerClick(macker: Marker?): Boolean
    {
        move_camera(macker!!.position.latitude, macker!!.position.longitude)
//        Toast.makeText(context, macker!!.position.toString(), Toast.LENGTH_SHORT).show()
        return true
    }

    override fun onMarkerDragStart(macker: Marker?)
    {

    }

    override fun onMarkerDrag(macker: Marker?)
    {

    }

    override fun onMarkerDragEnd(macker: Marker?)
    {

    }
}
