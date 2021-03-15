package com.arbonik.helper.helprequest

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.arbonik.helper.R
import com.arbonik.helper.auth.SharedPreferenceUser
import com.arbonik.helper.auth.USER_CATEGORY
import com.arbonik.helper.auth.data_request_map
import com.arbonik.helper.system.Format
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query


open class RequestAdapter(_query: Query, _listener: OnRequestSelectedListener)
    : FirestoreAdapter<RequestAdapter.ViewHolder>(_query)
{

    interface OnRequestSelectedListener
    {
        fun onRequestSelectedListener(requestData: DocumentSnapshot)
    }

    var listener = _listener
    var userCategory = SharedPreferenceUser.currentUser?.category!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val inflater = LayoutInflater.from(parent.context)
        val vh : ViewHolder = ViewHolder(inflater.inflate(R.layout.data_request, parent, false))
        return vh
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.bind(snapshots[position], listener)
    }

        inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item)
        {
             var dateView: TextView = item.findViewById(R.id.date_data)
             var titleView: TextView = item.findViewById(R.id.title_data)
             var nameView: TextView = item.findViewById(R.id.name_data)
             var numberView: TextView = item.findViewById(R.id.number_data)
             var commentView: TextView = item.findViewById(R.id.comment_data)
             var infView: TextView = item.findViewById(R.id.inf_data)
             var statusView: TextView = item.findViewById(R.id.status)
             val button : Button = item.findViewById(R.id.button_data)
             val button_location : Button = item.findViewById(R.id.button_location)
             val copy_phone : Button = item.findViewById(R.id.copy_phone)
             val context = item.context

            fun bind(snapshot: DocumentSnapshot, listener: OnRequestSelectedListener)
            {
                val requestData = snapshot.toObject(RequestData::class.java)
                    requestData?.let {
                        dateView.setText(it.date)
                        titleView.setText(it.title)
                        nameView.setText(it.master.name)
                        numberView.setText(Format.makeMaskTextView(it.master.phone.toString()))
                        commentView.setText(it.comment)
                        infView.setText(it.master.inf)
                        Linkify.addLinks(numberView, Linkify.PHONE_NUMBERS)

                        button_location.setOnClickListener {
                            var navController = it.findNavController()
                            val bundle = Bundle()
                            bundle.putSerializable("request", requestData)
                            navController.navigate(R.id.action_navigation_request_vol_to_map_volonteer_fragment, bundle)
                            navController.addOnDestinationChangedListener{ navController: NavController, navDestination: NavDestination, bundle: Bundle? ->
                                if (navController.currentDestination?.id == R.id.action_map_volonteer_fragment_to_navigation_request_vol ||
                                    navController.currentDestination?.id == R.id.action_navigation_request_vol_to_map_volonteer_fragment   )
                                {
                                    Toast.makeText(context, "testtesttest", Toast.LENGTH_LONG)
                                        .show()
                                }
                            }

                        }
                        copy_phone.setOnClickListener {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText(
                                "TAG", requestData.master.phone.toString()
                            )
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, R.string.coped_phone, Toast.LENGTH_SHORT).show()
                        }
                        when (userCategory)
                        {
                            USER_CATEGORY.VETERAN ->
                            {
                                statusView.text =
                                    if (it.status) "Заявка принята" else "Заявка обрабатывается"
                                button.text =
                                    if (it.status) "Заявка исполнена" else "Отозвать заявку"
                                button.setOnClickListener { v ->
                                    snapshot.reference.delete()
                                }
                            }
                            USER_CATEGORY.VOLONTEER ->
                            {
                                statusView.text =
                                    if (it.status) "Заявка принята" else "Заявка обрабатывается"
                                button.text =
                                    if (it.status) "Отказаться от заявки" else "Принять заявку"
                                button.setOnClickListener { v ->
                                    if (!it.status)
                                    {
                                        snapshot.reference.update(
                                            mapOf(
                                                "status" to true,
                                                "accepter" to SharedPreferenceUser.currentUser?.uid
                                            )
                                        )
                                    }
                                    else
                                    {
                                        snapshot.reference.update("status", false)
                                        snapshot.reference.update("accepter", "")
                                    }
                                }
                            }
                            USER_CATEGORY.ADMIN ->
                            {

                            }
                        }
                    }
                itemView.setOnClickListener {
                    listener.onRequestSelectedListener(snapshot)
                }
                fun accept_request()
                {

                }
            }

        }
}