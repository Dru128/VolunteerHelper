package com.arbonik.helper.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.arbonik.helper.R


class RecyclerView_admin: RecyclerView.Adapter<RecyclerView_admin.ViewHolder>()
{
    var db = FireStore()

    companion object
    {
        var Dataset: MutableList<TypeRequest> = mutableListOf()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val View =
            LayoutInflater.from(parent.context).inflate(R.layout.admin_item, parent, false)
        return ViewHolder(View)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.onBind(Dataset[position])
    }

    override fun getItemCount(): Int = Dataset.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        var context = itemView.context
        var type_text = itemView.findViewById<TextView>(R.id.type_text)
        var deletetype_button = itemView.findViewById<Button>(R.id.deletetype_buton)

        fun onBind(type: TypeRequest)
        {
            type_text.text = type.type
            deletetype_button.setOnClickListener {
                for (i in 0 until Dataset.size)
                {
                    if (type.type == Dataset[i].type)
                    {
                        db.deleteTypeHelp(type.key)
                        Dataset.removeAt(i)
                        notifyDataSetChanged()
                        return@setOnClickListener
                    }
                }
                Toast.makeText(context, "This type is deleted", Toast.LENGTH_LONG).show()
            }
        }
    }
}


