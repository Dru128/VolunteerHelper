package com.arbonik.helper.ui.requestFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arbonik.helper.R
import com.arbonik.helper.auth.SharedPreferenceUser
import com.arbonik.helper.auth.USER_CATEGORY
import com.arbonik.helper.helprequest.RequestAdapter
import com.arbonik.helper.helprequest.RequestManager
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

open class RequestFragment : Fragment(), RequestAdapter.OnRequestSelectedListener
{

    protected var firestore: FirebaseFirestore? = null
    protected var query: Query? = null

    private var requestRecycler: RecyclerView? = null
    private var adapter: RequestAdapter? = null

    init
    {
        firestore = FirebaseFirestore.getInstance()
        initFirestore()
    }

    protected open fun initFirestore()
    {
        val fieldPath = FieldPath.of("master", "phone")
        val phone = SharedPreferenceUser.currentUser?.phone
        query = when (SharedPreferenceUser.currentUser?.category) {
            USER_CATEGORY.VETERAN -> {
                firestore!!.collection(RequestManager.REQUEST_TAG)
                    .whereEqualTo(fieldPath, phone!!)
            }
            USER_CATEGORY.VOLONTEER -> {
                firestore!!.collection(RequestManager.REQUEST_TAG)
                    .whereEqualTo("status", false)

            }
            USER_CATEGORY.ADMIN -> TODO()
            null -> TODO()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        val root = inflater.inflate(R.layout.fragment_reqests, container, false)

        requestRecycler =  root.findViewById(R.id.request_recycler_view)
        initRecyclerView()

        return root
    }

    private fun initRecyclerView()
    {
        adapter = RequestAdapter(query!!, this)
        val linear = LinearLayoutManager(context)
        requestRecycler?.layoutManager = linear
        requestRecycler?.adapter = adapter
    }

    override fun onStart()
    {
        super.onStart()
        adapter?.startListening()
    }

    override fun onStop()
    {
        super.onStop()
        adapter?.stopListening()
    }

    override fun onRequestSelectedListener(requestData: DocumentSnapshot) {

    }
}

class RequestVolonteerFragment : RequestFragment() {
    override fun initFirestore() {
        val fieldPath = FieldPath.of("accepter")
        val phone = SharedPreferenceUser.currentUser?.phone
        query = firestore!!.collection(RequestManager.REQUEST_TAG)
                    .whereEqualTo(fieldPath, SharedPreferenceUser.currentUser?.uid)

    }
}