package com.arbonik.helper.ui.requestFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arbonik.helper.R
import com.arbonik.helper.databinding.DataRequestBinding
import com.arbonik.helper.databinding.FragmentReqestBinding
import com.arbonik.helper.helprequest.RequestData
import com.arbonik.helper.helprequest.RequestManager
import com.arbonik.helper.helprequest.RequestViewModel
import com.arbonik.helper.helprequest.RequestAdapter
import com.google.firebase.firestore.*

class LegacyRequestFragment: Fragment(),
    RequestAdapter.OnRequestSelectedListener {

    val db = FirebaseFirestore.getInstance()

    var requests : Array<RequestData> = arrayOf()
    var requestsId : Array<String> = arrayOf()

    // ViewBlock

    private var firestore: FirebaseFirestore? = null
    private var query: Query? = null
    override fun onRequestSelectedListener(requestData: DocumentSnapshot) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        var userRequestQuery =
//            db.collection(RequestManager.USERS_TAG)
//                .document(SharedPreferenceUser.currentUser?.uid!!)
//                .collection(MY_REQUEST_TAG)
//                .get().addOnCompleteListener {task ->
//                    var res = task.result?.documents
//                    requestsId = Array(res?.size!!){
//                        Log.d("TESTTEXT", res[it].toString())
//                        res[it].toString()
//                    }
//                }
        initFirestore()

    }

    private fun initFirestore() {
        firestore = FirebaseFirestore.getInstance()

        query = firestore?.collection("requests")
            ?.orderBy("date", Query.Direction.DESCENDING)
            ?.limit(5)
    }

    lateinit var binding : FragmentReqestBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reqest, container, false)

        initRecyclerView()

//        var query = when (SharedPreferenceUser.currentUser?.category){
//            USER_CATEGORY.VETERAN -> {
//                db.collection(REQUEST_TAG)
//                    .document(SharedPreferenceUser.currentUser?.uid!!)
//                    .collection(MY_REQUEST_TAG)
//            }
//
//            USER_CATEGORY.VOLONTEER -> db.collection(REQUEST_TAG)
//
//            USER_CATEGORY.ADMIN -> db.collection(REQUEST_TAG)
//                .endAt(SharedPreferenceUser.currentUser?.uid)
//            else -> throw Exception()
//                //.endAt(SharedPreferenceUser.currentUser?.uid)
//        }
//
//        query.get().addOnCompleteListener { it : Task<QuerySnapshot> ->
//
//            var result = it.result?.documents
//            requests =
//                Array(result?.size ?: 0) {i ->
//                    result!![i].toObject(RequestData::class.java)!!
//                }
//            requests.forEach { Log.d("TESTTEXT", it.toString()) }
//            binding.requestRecyclerView.adapter = RequestAdapter(requests)
//        }

        return binding.root
    }

    private fun initRecyclerView() {
        query?.let{
            Log.w("HUY", "No query, not initializing RecyclerView");
        }

        binding.requestRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.requestRecyclerView.adapter = RequestAdapter(requests)
    }

    override fun onStart() {
        super.onStart()
    }

    class RequestHolder(var binding: DataRequestBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.request = RequestViewModel(RequestManager())
        }
        fun bind(request : RequestData){
            binding.request?.request = request
            binding.executePendingBindings()
        }
    }
    inner class RequestAdapter(var requests: Array<RequestData>) : RecyclerView.Adapter<RequestHolder>(){

        override fun onBindViewHolder(holder: RequestHolder, position: Int) {
            holder.bind(requests[position])
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestHolder {
            val inflater = LayoutInflater.from(activity)
            val binding : DataRequestBinding = DataBindingUtil.inflate(inflater, R.layout.data_request, parent, false)
            return RequestHolder(binding)
        }

        override fun getItemCount(): Int = requests.size
    }
}


