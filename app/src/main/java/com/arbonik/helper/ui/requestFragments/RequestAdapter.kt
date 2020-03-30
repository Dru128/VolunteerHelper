package com.arbonik.helper.ui.requestFragments

import android.graphics.Color
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.arbonik.helper.FireDatabase
import com.arbonik.helper.HelpRequest.DataHelpRequest
import com.arbonik.helper.HelperApplication
import com.arbonik.helper.R
import com.arbonik.helper.ui.settings.SettingsFragment

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

                    if (PreferenceManager.getDefaultSharedPreferences(HelperApplication.globalContext)
                        .getBoolean(SettingsFragment.key_role, false))
                    {
                        button.setOnClickListener { v ->
                            FireDatabase.requestReference.child(request.unic).child("answered")
                                .setValue(true)
                            button.setBackgroundColor(Color.GREEN)
                            button.setText("Заявка принята!")
                        }
                    }
                else {
                        button.setOnClickListener { v ->
                            FireDatabase.requestReference.child(request.unic).removeValue()
                            button.setBackgroundColor(Color.RED)
                            button.setText("Заявка Удалена!")
                        }
                    }
                    textName.setText(request.name)
                    textAdress.setText(request.address)
                    textPhone.setText(request.phone)
                    categiryImage.setImageResource(request.category.img)

                Log.d("WHAT", "${PreferenceManager.getDefaultSharedPreferences(HelperApplication.globalContext).getString(
                    SettingsFragment.key_phone, "")}" )
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