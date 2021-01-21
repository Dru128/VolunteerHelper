package com.arbonik.helper.auth

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.arbonik.helper.Map.MapsFragment
import com.arbonik.helper.R
import com.arbonik.helper.system.Format
import com.google.android.gms.maps.model.LatLng


class RegistrationActivity : AuthBase()
{
    val containerButtons by lazy { findViewById<LinearLayout>(R.id.reg_cansel_choose) }
    var mapsFragment: MapsFragment? = MapsFragment()
    var regFragment: RegistrationFragment = RegistrationFragment()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        findViewById<Button>(R.id.reg_close)
            .setOnClickListener {
                setRegistrationFragment()
            }

        findViewById<Button>(R.id.reg_choose)
            .setOnClickListener {
                if (mapsFragment != null && mapsFragment!!.myMacker != null)
                {
                    RegData.location = Format.latLng_to_geoPoint(mapsFragment!!.myMacker!!.position)
                    mapsFragment!!.myMacker = null
                    setRegistrationFragment()
                }
            }
        setRegistrationFragment()
    }

    fun setRegistrationFragment()
    {
        containerButtons?.visibility = View.GONE
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.activity_layout_reg, regFragment)
            .commit()
    }

    fun setMapFragment()
    {
        mapsFragment = MapsFragment()
        if (RegData.location != null) mapsFragment!!.curLocation = Format.geoPoint_to_geoPointlatLng(RegData.location!!)
        containerButtons?.visibility = View.VISIBLE
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.activity_layout_reg, mapsFragment!!)
            .commit()
    }
}


