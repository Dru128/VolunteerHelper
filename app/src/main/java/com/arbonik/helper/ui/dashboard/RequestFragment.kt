package com.arbonik.helper.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.arbonik.helper.HelpRequest.DataHelpRequest
import com.arbonik.helper.R
import com.arbonik.helper.ui.home.AuthFragment

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

        return root
    }
}

class RequestRecycler : RecyclerView.Adapter<RequestRecycler.Companion.RequestHolder>(){
    companion object {
        class RequestHolder(view: View) : RecyclerView.ViewHolder(view){
            val textName: TextView = view.findViewById(R.id.request_from)
            val textAdress: TextView = view.findViewById(R.id.adress_request)
            val textDate: TextView = view.findViewById(R.id.date_request)
            val textTime: TextView = view.findViewById(R.id.time_request)

            fun bind(request : DataHelpRequest){
                textName.setText(request.name)
                textAdress.setText(request.adress)
                textDate.setText(request.date.toString())
                textTime.setText(request.category.toString())
            }
        }
    }

    override fun getItemCount(): Int {

    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun onBindViewHolder(holder: RequestHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}