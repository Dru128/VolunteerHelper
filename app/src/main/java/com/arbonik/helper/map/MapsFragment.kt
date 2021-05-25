package com.arbonik.helper.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.arbonik.helper.R
import com.arbonik.helper.auth.SharedPreferenceUser
import com.arbonik.helper.auth.USER_CATEGORY
import com.arbonik.helper.auth.User
import com.arbonik.helper.helprequest.RequestManager
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.data_request.*
import java.util.*


open class MapsFragment() : Fragment(),
    OnMyLocationButtonClickListener,
    GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMarkerDragListener,
    GoogleMap.OnMyLocationChangeListener
{
    val navController by lazy { this.requireView().findNavController() }

    var map_layout_child: LinearLayout? = null
    var map_layout_main: ConstraintLayout? = null

    open var google_map: GoogleMap? = null
    set(value)
    {
        field = value
        creatAll()
    }
    private val db = FirebaseFirestore.getInstance().collection(RequestManager.USERS_TAG)
    private var permission_GPS: Boolean = false
    val TYPE_USER by lazy { SharedPreferenceUser.currentUser?.category }
    open fun creatAll() { }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View?
    {
        setHasOptionsMenu(true)
        var root = inflater.inflate(R.layout.fragment_maps, container, false)
        root.apply {
            map_layout_main = findViewById(R.id.map_layout)
            map_layout_child = findViewById(R.id.map_layout_child)
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

        var mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync { googleMap ->
            google_map = googleMap
            locationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            googleMap.apply {
                uiSettings.isZoomControlsEnabled = true
                google_map?.uiSettings?.isMyLocationButtonEnabled = true

//                setOnMarkerClickListener(this@MapsFragment)
                setOnMyLocationButtonClickListener(this@MapsFragment)
                setOnMyLocationChangeListener(this@MapsFragment)
            }



            when (TYPE_USER)
            {
                USER_CATEGORY.VETERAN, null ->
                {
                    //                    turnGPS()
                    /*
                    if (permission_GPS)
                    {
                        //                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curLocation, 5f))
                        AlertDialog.Builder(context).apply {
                            setTitle("выбрать текущее местоположение?")
                            setPositiveButton("да, выбрать") { _, _ ->
                                onMyLocationButtonClick()

                                val geoPoint = LatLng(
                                    googleMap.myLocation.latitude, googleMap.myLocation.longitude
                                )
                                setMyMackerPosition(geoPoint)
                                moveCamera(geoPoint)
                            }
                            setNegativeButton("отмена") { _, _ -> }
                            show()
                        }
                    }*/
                }

                USER_CATEGORY.ADMIN ->
                {
                    db.whereEqualTo(User.TAG_CATEGORY, USER_CATEGORY.VETERAN).get()
                        .addOnSuccessListener { documents ->
                            for (document in documents)
                            {
                                val location =
                                    document[User.TAG_LOCATION.toLowerCase(Locale.ROOT)] as GeoPoint
                                googleMap.addMarker(
                                    MarkerOptions().position(
                                        LatLng(
                                            location.latitude, location.longitude
                                        )
                                    )
                                        .title(document[User.NAME_TAG.toLowerCase(Locale.ROOT)].toString())
                                        .snippet(
                                            "телефон: ${document[User.TAG_PHONE.toLowerCase(Locale.ROOT)].toString()}" + "   адрес: ${
                                                document[User.TAG_INF.toLowerCase(Locale.ROOT)].toString()
                                            }"
                                        )
                                )
                            }
                        }
                }
                else -> { }
            }
        }
    }
    @SuppressLint("MissingPermission")
    val locationPermission = registerForActivityResult(ActivityResultContracts.RequestPermission())
    { isGranted ->
        if (isGranted)
        {
            google_map?.isMyLocationEnabled = true
        }
        else {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 0
            )
        }
    }

    open fun isGeoDisabled(): Boolean
    {
        val mLocationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val mIsGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val mIsNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return !mIsGPSEnabled && !mIsNetworkEnabled
    }

    fun setMapType(type: Int) { google_map?.mapType = type }

    fun moveCamera(position: LatLng) { google_map?.animateCamera(
        CameraUpdateFactory.newLatLngZoom(
            position, 15f
        ), 1500, null
    ) }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onDestroy()
    {
        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar
        actionBar?.setHomeButtonEnabled(false)
        actionBar?.setDisplayHomeAsUpEnabled(false)
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if (item.itemId == android.R.id.home)
        {
            onDestroyView()
            onDestroy()
        }
        return super.onOptionsItemSelected(item)
    }

    //--------------------------------------------------------| интерфейсы & проверки |--------------------------------------------------------
    override fun onMyLocationButtonClick(): Boolean
    {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) // проверка включен ли GPS
        {
            AlertDialog.Builder(context).apply {
                setMessage(getString(R.string.proposal_include_gps))
                setPositiveButton(getString(R.string.ok)) { _, _ ->
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                setNegativeButton(getString(R.string.not)) { _, _ ->
                    onDestroyView()
                }
                show()
            }
        }
        return false
    }

    override fun onMyLocationChange(point: Location?) { }

    override fun onMarkerClick(macker: Marker): Boolean
    {
        moveCamera(macker.position)
        return true
    }

    override fun onMarkerDragStart(macker: Marker?) { }
    override fun onMarkerDrag(macker: Marker?) { }
    override fun onMarkerDragEnd(macker: Marker?) { }
}
