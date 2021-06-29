package com.arbonik.helper.admin.users

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.arbonik.helper.HelperApplication
import com.arbonik.helper.R
import com.arbonik.helper.auth.STATUS_ACCOUNT
import com.arbonik.helper.auth.USER_CATEGORY
import com.arbonik.helper.auth.User
import com.arbonik.helper.helprequest.RequestManager
import com.arbonik.helper.system.Format
import com.google.firebase.firestore.FirebaseFirestore


class RecyclerAdapterUsersAdmin(): RecyclerView.Adapter<RecyclerAdapterUsersAdmin.ViewHolder>()
{
    var db = FirebaseFirestore.getInstance().collection(RequestManager.USERS_TAG)
    var allUsers = mutableListOf<User>() // все юзеры
    private var dataSet = mutableListOf<User>() // юзеры которые будут отображаться
    private lateinit var statusAccount: STATUS_ACCOUNT

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item_admin, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.onBind(dataSet[position])
    }

    override fun getItemCount(): Int = dataSet.size

    fun sortUsers(SA: STATUS_ACCOUNT?)
    {
        if (SA != null) statusAccount = SA
        val result = mutableListOf<User>()
        allUsers.forEach { if (it.status_account == statusAccount) result.add(it) }
        dataSet = result
        notifyDataSetChanged()
    }

    fun onDataUpdate(updateData: MutableList<User>)
    {
        updateData.forEach { updateItem ->
            for (i in 0 until allUsers.size)
            {
                if (allUsers[i].uid.toString() == updateItem.uid.toString()) allUsers[i] = updateItem
            }
        }
        sortUsers(null)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var context = itemView.context
        var user_type_text = itemView.findViewById<TextView>(R.id.text_user_type_users_admin)
        var name_text = itemView.findViewById<TextView>(R.id.text_name_users_admin)
        var phone_text = itemView.findViewById<TextView>(R.id.text_phone_users_admin)
        var inf_text = itemView.findViewById<TextView>(R.id.text_inf_users_admin)
//        var uid_text = itemView.findViewById<TextView>(R.id.text_uid_users_admin)
        var button_location = itemView.findViewById<ImageButton>(R.id.button_location_user_admin)

        var activate_button = itemView.findViewById<Button>(R.id.button_activate_admin_item)
        var lock_button = itemView.findViewById<Button>(R.id.button_lock_admin_item)
        var unlock_button = itemView.findViewById<Button>(R.id.button_unlock_admin_item)
        var address_container = itemView.findViewById<ConstraintLayout>(R.id.inf_layout_users_admin)

        fun onBind(user: User)
        {
            if (user.category.toString() == USER_CATEGORY.VETERAN.toString())
            {
                address_container.visibility = View.VISIBLE
                inf_text.text = user.inf
                button_location.setOnClickListener {
                    if (user.location != null)
                    {
                        val bundle = Bundle()
                        bundle.putParcelable(User.TAG_LOCATION, Format.geoPointTolatLng(user.location!!))
                        it.findNavController().navigate(R.id.action_navigation_users_fragment_to_mapsFragment, bundle)
                    }
                }
            }
            else
            {
                address_container.visibility = View.GONE
            }
            when (statusAccount)
            {
                STATUS_ACCOUNT.REG_CHECKED ->
                {
                    activate_button.visibility = View.VISIBLE
                    lock_button.visibility = View.VISIBLE
                    unlock_button.visibility = View.GONE
                }
                STATUS_ACCOUNT.ACTIVE ->
                {
                    activate_button.visibility = View.GONE
                    lock_button.visibility = View.VISIBLE
                    unlock_button.visibility = View.GONE
                }
                STATUS_ACCOUNT.LOCKED ->
                {
                    activate_button.visibility = View.GONE
                    lock_button.visibility = View.GONE
                    unlock_button.visibility = View.VISIBLE
                }
            }

            activate_button.setOnClickListener {
                db.document(user.uid.toString()).update("status_account", STATUS_ACCOUNT.ACTIVE)
            }
            lock_button.setOnClickListener {
                db.document(user.uid.toString()).update("status_account", STATUS_ACCOUNT.LOCKED)
            }
            unlock_button.setOnClickListener {
                db.document(user.uid.toString()).update("status_account", STATUS_ACCOUNT.ACTIVE)
            }
            user_type_text.text = user.category.toString()
            name_text.text = user.name.toString()
            phone_text.text = Format.makeMaskPhone(user.phone.toString())
//            uid_text.text = user.uid
            Linkify.addLinks(phone_text, Linkify.PHONE_NUMBERS)

            itemView.findViewById<ImageButton>(R.id.button_copy_phone_user_admin)
                .setOnClickListener {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("phone", user.phone.toString())
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(context, R.string.coped_phone, Toast.LENGTH_SHORT).show()
                }
        }
    }
}

