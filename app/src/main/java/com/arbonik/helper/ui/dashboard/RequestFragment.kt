package com.arbonik.helper.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arbonik.helper.FireDatabase
import com.arbonik.helper.HelpRequest.DataHelpRequest
import com.arbonik.helper.R
import com.arbonik.helper.ui.home.AuthFragment
import com.arbonik.helper.ui.home.Category
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import org.w3c.dom.Text

class RequestFragment: Fragment() {
    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_reqest, container, false)

        val recycler : RecyclerView =  root.findViewById(R.id.request_recycler_view)

        val linear = LinearLayoutManager(context)
        recycler.layoutManager = linear

        val requestAdapter = RequestAdapter()
        FireDatabase.requestReference.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                recycler.invalidate()
            }
            override fun onChildRemoved(p0: DataSnapshot) {
            }
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                requestAdapter.requestioons.add(DataHelpRequest.fromBD(p0))
                requestAdapter.notifyDataSetChanged()
            }
        })

        recycler.adapter = requestAdapter
        return root
    }
}

class RequestAdapter : RecyclerView.Adapter<RequestAdapter.Companion.RequestHolder>(){

    var requestioons : MutableList<DataHelpRequest> = mutableListOf()

    companion object {
        class RequestHolder(view: View) : RecyclerView.ViewHolder(view){
            val textName: TextView = view.findViewById(R.id.name_data)
            val textAdress: TextView = view.findViewById(R.id.address_data)
            val textPhone: TextView = view.findViewById(R.id.number_data)
            val categiryImage: ImageView = view.findViewById(R.id.img_data)
            val button : Button = view.findViewById(R.id.button_data)
            fun bind(request : DataHelpRequest){
                if (request.answered) {
                    button.setBackgroundColor(Color.GREEN)
                    button.setText("Заявка принята!")
                }
                button.setOnClickListener { v ->
                    FireDatabase.requestReference.child(request.unic).child("answered").setValue(true)
                    button.setBackgroundColor(Color.GREEN)
                    button.setText("Заявка принята!")
                }
                textName.setText(request.name)
                textAdress.setText(request.address)
                textPhone.setText(request.phone)
                categiryImage.setImageResource(request.category.img)
            }
        }
    }
    override fun onBindViewHolder(holder: RequestHolder, position: Int) {
        holder.bind(requestioons[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.data_request, parent, false)
        return RequestHolder(view)
    }

    override fun getItemCount(): Int = requestioons.size
}
