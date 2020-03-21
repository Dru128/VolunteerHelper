package com.arbonik.helper.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceFragmentCompat
import com.arbonik.helper.R

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var notificationsViewModel: SettingsViewModel
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.fragment_settings, rootKey)
    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        notificationsViewModel =
//            ViewModelProviders.of(this).get(SettingsViewModel::class.java)
//
//        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
//        val textView: TextView = root.findViewById(R.id.text_notifications)
//
//        notificationsViewModel.text.observe(this, Observer {
//            textView.text = it
//        })
//        return root
//    }
}