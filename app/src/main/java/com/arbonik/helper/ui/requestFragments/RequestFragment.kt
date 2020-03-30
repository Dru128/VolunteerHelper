package com.arbonik.helper.ui.requestFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arbonik.helper.*
import com.arbonik.helper.HelpRequest.DataHelpRequest
import com.arbonik.helper.ui.settings.SettingsFragment
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class RequestFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_reqest, container, false)

        val recycler : RecyclerView =  root.findViewById(R.id.request_recycler_view)

        val linear = LinearLayoutManager(context)
        recycler.layoutManager = linear

        val requestAdapter = RequestAdapter()

        FireDatabase.requestReference.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                requestAdapter.notifyDataSetChanged()
            }
            override fun onChildRemoved(p0: DataSnapshot) {
            }
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val request = DataHelpRequest.fromBD(p0)
                if (!PreferenceManager.getDefaultSharedPreferences(HelperApplication.globalContext).getBoolean(
                        SettingsFragment.key_role, false)) // if mode volonteer = of, view filter = on
                {
                    if (PreferenceManager.getDefaultSharedPreferences(HelperApplication.globalContext).getString(
                            SettingsFragment.key_phone, ""
                        ) == request.phone
                    ) // if number user != number reques, done it
                    requestAdapter.requestioons.add(request)
                }
                else{
                    requestAdapter.requestioons.add(request)
                }
                    requestAdapter.notifyDataSetChanged()
            }
        })
            recycler.adapter = requestAdapter
                return root
    }
}


