package com.arbonik.helper.helprequest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arbonik.helper.R
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query

open class RequestAdapter(_query : Query, _listener : OnRequestSelectedListener)
    : FirestoreAdapter<RequestAdapter.Companion.ViewHolder>(_query) {

    interface OnRequestSelectedListener{
        fun onRequestSelectedListener(requestData: DocumentSnapshot)
    }

    var listener = _listener

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val vh : ViewHolder =
            ViewHolder(inflater.inflate(R.layout.data_request, parent, false))
        return vh
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(snapshots[position], listener)
    }

    companion object {
        class ViewHolder(item: View) : RecyclerView.ViewHolder(item){

            var dateView: TextView = item.findViewById(R.id.date_data)
            var titleView: TextView = item.findViewById(R.id.title_data)
            var nameView: TextView = item.findViewById(R.id.name_data)
            var addressView: TextView = item.findViewById(R.id.address_data)
            var numberView: TextView = item.findViewById(R.id.number_data)
            var commentView: TextView = item.findViewById(R.id.comment_data)

            fun bind(snapshot : DocumentSnapshot, listener : OnRequestSelectedListener){
                val requestData = snapshot.toObject(RequestData::class.java)
                    requestData?.let {
                        dateView.setText(it.date)
                        titleView.setText(it.title)
                        nameView.setText(it.master.name)
                        addressView.setText(it.master.address)
                        numberView.setText(it.master.phone)
                        commentView.setText(it.comment)
                    }
                itemView.setOnClickListener {
                    listener.onRequestSelectedListener(snapshot)
                }
            }

        }

    }

}