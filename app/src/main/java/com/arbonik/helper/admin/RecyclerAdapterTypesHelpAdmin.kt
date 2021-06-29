package com.arbonik.helper.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arbonik.helper.R


class RecyclerAdapterTypesHelpAdmin: RecyclerView.Adapter<RecyclerAdapterTypesHelpAdmin.ViewHolder>()
{
    //project-529198191554
    var db = FireBaseTypesRequest()
    var dataset: MutableList<TypeRequest> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val View = LayoutInflater.from(parent.context).inflate(R.layout.type_help_item_admin, parent, false)
        return ViewHolder(View)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.onBind(dataset[position])
    }

    override fun getItemCount(): Int = dataset.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var context = itemView.context
        var type_text = itemView.findViewById<TextView>(R.id.type_text)
        var deletetype_button = itemView.findViewById<Button>(R.id.delete_type_buton)

        fun onBind(type: TypeRequest)
        {
            type_text.text = type.type
            deletetype_button.setOnClickListener {
                for (i in 0 until dataset.size)
                {
                    if (type.type == dataset[i].type)
                    {
                        db.deleteTypeHelp(type.key)
                        dataset.removeAt(i)
                        notifyDataSetChanged()
                        return@setOnClickListener
                    }
                }
            }
        }
    }
}


