package com.arbonik.helper.admin.users

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arbonik.helper.R
import com.arbonik.helper.auth.STATUS_ACCOUNT
import com.arbonik.helper.auth.User
import com.arbonik.helper.auth.UserDataFirebase
import com.arbonik.helper.helprequest.RequestManager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.util.*


class UsersFragment : Fragment()
{
    var adapter = RecyclerAdapterUsersAdmin()
    var tabLayout: TabLayout? = null
    var recyclerView: RecyclerView? = null
    var db = FirebaseFirestore.getInstance()
    val userDataFirebase = UserDataFirebase()
    var isGetUser = false
    var snapshotListener: ListenerRegistration? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val root = inflater.inflate(R.layout.fragment_users, container, false)
        root.apply {
            tabLayout = findViewById(R.id.tablayout_fragment_users)
            recyclerView = findViewById(R.id.recycler_view_fragment_users)
        }

        return root
    }

    private fun readyData()
    {
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab)
            {
                when (tab.text)
                {
                    getString(R.string.reg_checked) -> adapter.sortUsers(STATUS_ACCOUNT.REG_CHECKED)
                    getString(R.string.active) -> adapter.sortUsers(STATUS_ACCOUNT.ACTIVE)
                    getString(R.string.blocked) -> adapter.sortUsers(STATUS_ACCOUNT.LOCKED)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }


    private fun initSnapshotListener() = db.collection(RequestManager.USERS_TAG).addSnapshotListener { snapshots, e ->
        if (e != null)
        {
            Toast.makeText(context, "Ошибка ${e.code} \n ${e.message}", Toast.LENGTH_LONG).show()
            Log.d("GET_DATA_ADMIN_ERROR", "Ошибка ${e.code} \n" + " ${e.message}")
            return@addSnapshotListener
        }
        val result = mutableListOf<User>()
        for (dc in snapshots!!.documentChanges) result.add(
            userDataFirebase.convertDocumentToUser(
                dc.document.data
            )
        )

//        Log.d("GET_DATA_ADMIN", "новые данные: \n $result")
        if (!isGetUser)
        {
            Log.d("GET_DATA_ADMIN", "Данные получены")
            adapter.allUsers = result
            adapter.sortUsers(STATUS_ACCOUNT.REG_CHECKED)
            isGetUser = true
            readyData()
        }
        else
        {
            adapter.onDataUpdate(result)
        }
    }


    override fun onStart()
    {
        snapshotListener = initSnapshotListener()
        super.onStart()
    }

    override fun onStop()
    {
        snapshotListener?.remove()
        isGetUser = false
        super.onStop()
    }
}