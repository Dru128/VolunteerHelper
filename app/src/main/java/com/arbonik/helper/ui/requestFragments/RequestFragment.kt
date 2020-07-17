package com.arbonik.helper.ui.requestFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arbonik.helper.*
import com.arbonik.helper.databinding.DataRequestBinding
import com.arbonik.helper.databinding.FragmentReqestBinding
import com.arbonik.helper.helprequest.RequestData
import com.arbonik.helper.helprequest.RequestManager
import com.arbonik.helper.helprequest.RequestViewModel
import com.google.firebase.firestore.FirebaseFirestore

class RequestFragment: Fragment() {
    val REQUEST_TAG = "requests"

    val db = FirebaseFirestore.getInstance()

    var requests : Array<RequestData> = arrayOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentReqestBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_reqest, container, false)

        binding.requestRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.requestRecyclerView.adapter = RequestAdapter(requests)

        db.collection(REQUEST_TAG).get().addOnCompleteListener {
            var result = it.result?.documents
            requests =
                Array(result?.size ?: 0) {i ->
                    result!![i].toObject(RequestData::class.java)!!
                }
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


