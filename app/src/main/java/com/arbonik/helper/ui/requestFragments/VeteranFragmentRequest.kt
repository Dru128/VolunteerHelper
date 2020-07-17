package com.arbonik.helper.ui.requestFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arbonik.helper.R

class VeteranFragmentRequest : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_reqest, container, false)

        val recycler : RecyclerView =  root.findViewById(R.id.request_recycler_view)

        val linear = LinearLayoutManager(context)
        recycler.layoutManager = linear

//        val requestAdapter = RequestAdapter(requestManager.requests)

//        recycler.adapter = requestAdapter
        return root

    }
}