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
import com.arbonik.helper.*
import com.arbonik.helper.auth.SharedPreferenceUser
import com.arbonik.helper.auth.SignIn
import com.arbonik.helper.auth.USER_CATEGORY
import com.arbonik.helper.auth.UserDataFirebase
import com.arbonik.helper.databinding.DataRequestBinding
import com.arbonik.helper.databinding.FragmentReqestBinding
import com.arbonik.helper.helprequest.RequestData
import com.arbonik.helper.helprequest.RequestManager
import com.arbonik.helper.helprequest.RequestManager.Companion.MY_REQUEST_TAG
import com.arbonik.helper.helprequest.RequestManager.Companion.REQUEST_TAG
import com.arbonik.helper.helprequest.RequestViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class RequestFragment: Fragment() {

    val db = FirebaseFirestore.getInstance()

    var requests : Array<RequestData> = arrayOf()
    var requestsId : Array<String> = arrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var userRequestQuery =
            db.collection(RequestManager.USERS_TAG)
                .document(SharedPreferenceUser.currentUser?.uid!!)
                .collection(MY_REQUEST_TAG)
                .get().addOnCompleteListener {task ->
                    var res = task.result?.documents
                    requestsId = Array(res?.size!!){
                        Log.d("TESTTEXT", res[it].toString())
                        res[it].toString()
                    }
                }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentReqestBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_reqest, container, false)

        binding.requestRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.requestRecyclerView.adapter = RequestAdapter(requests)


        var query = when (SharedPreferenceUser.currentUser?.category){
            USER_CATEGORY.VETERAN -> {
                db.collection(REQUEST_TAG)
                    .document(SharedPreferenceUser.currentUser?.uid!!)
                    .collection(MY_REQUEST_TAG)
            }

            USER_CATEGORY.VOLONTEER -> db.collection(REQUEST_TAG)

            USER_CATEGORY.ADMIN -> db.collection(REQUEST_TAG)
                .endAt(SharedPreferenceUser.currentUser?.uid)
            else -> throw Exception()
                //.endAt(SharedPreferenceUser.currentUser?.uid)
        }

        query.get().addOnCompleteListener { it : Task<QuerySnapshot> ->

            var result = it.result?.documents
            requests =
                Array(result?.size ?: 0) {i ->
                    result!![i].toObject(RequestData::class.java)!!
                }
            requests.forEach { Log.d("TESTTEXT", it.toString()) }
            binding.requestRecyclerView.adapter = RequestAdapter(requests)
        }

        return binding.root
    }

    inner class RequestHolder(var binding: DataRequestBinding) : RecyclerView.ViewHolder(binding.root){
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


