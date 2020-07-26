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

class RequestFragment : Fragment(),
RequestAdapter.OnRequestSelectedListener{

    private var firestore: FirebaseFirestore? = null
    private var query: Query? = null

    private var requestRecycler: RecyclerView? = null
    private var adapter: RequestAdapter? = null

    init {
        initFirestore()
    }

    private fun initFirestore(){
        firestore = FirebaseFirestore.getInstance()
        val fieldPath = FieldPath.of("master", "phone")
        val phone = SharedPreferenceUser.currentUser?.phone
        query = when (SharedPreferenceUser.currentUser?.category) {
            USER_CATEGORY.VETERAN -> {
                firestore!!.collection(RequestManager.REQUEST_TAG)
                    .whereEqualTo(fieldPath, phone!!)
            }
            USER_CATEGORY.VOLONTEER -> {
                firestore!!.collection(RequestManager.REQUEST_TAG)

            }
            USER_CATEGORY.ADMIN -> TODO()
            null -> TODO()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_reqest, container, false)

        requestRecycler =  root.findViewById(R.id.request_recycler_view)
        initRecyclerView()

        return root
    }

    private fun initRecyclerView(){
        adapter = RequestAdapter(query!!, this)
        val linear = LinearLayoutManager(context)
        requestRecycler?.layoutManager = linear
        requestRecycler?.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }

    override fun onRequestSelectedListener(requestData: DocumentSnapshot) {

    }
}