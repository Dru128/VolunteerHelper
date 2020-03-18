package com.arbonik.helper.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arbonik.helper.R

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true // сохранение сострояния при перевороте
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val data: RecyclerView = root.findViewById(R.id.fragmentRecycler)
        val linear = LinearLayoutManager(context)
        data.layoutManager = linear

        root.findViewById<Button>(R.id.toAuth).setOnClickListener { v -> AuthFragment.createAuth() }

        homeViewModel.text.observe(this, Observer {
            var ca = CategoryAdapter()
            ca.categories = mutableListOf(
                CategoryWidget(R.drawable.ic_local_florist_black_24dp, getString(R.string.household), true),
                CategoryWidget(R.drawable.ic_launcher_background, "cas", false),
                CategoryWidget(R.drawable.ic_launcher_background, "qwer", false),
                CategoryWidget(R.drawable.ic_launcher_background, "1234", false),
                CategoryWidget(R.drawable.ic_launcher_background, "1234", false),
                CategoryWidget(R.drawable.ic_launcher_background, "1234", false),
                CategoryWidget(R.drawable.ic_launcher_background, "1234", false),
                CategoryWidget(R.drawable.ic_launcher_background, "1234", false),
                CategoryWidget(R.drawable.ic_launcher_background, "1234", false),
                CategoryWidget(R.drawable.ic_launcher_background, "1234", false),
                CategoryWidget(R.drawable.ic_launcher_background, "1234", false),
                CategoryWidget(R.drawable.ic_launcher_background, "1234", false),
                CategoryWidget(R.drawable.ic_launcher_background, "1234", false)
            )
            data.adapter = ca
        })
        return root
    }
}