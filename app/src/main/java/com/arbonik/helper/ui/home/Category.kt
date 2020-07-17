package com.arbonik.helper.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arbonik.helper.R

data class CategoryWidget (val category: String, var choise : Boolean = false)

class CategoryAdapter: RecyclerView.Adapter<CategoryAdapter.Companion.CategoryViewHolder>(){
    var categories : MutableList<CategoryWidget> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) =
        holder.bind(categories[position])

    companion object {
        class CategoryViewHolder(view : View) : RecyclerView.ViewHolder(view){
            val label = view.findViewById<TextView>(R.id.label)
            val checkBox = view.findViewById<CheckBox>(R.id.check)

            fun bind(category: CategoryWidget){
                label.setText(category.category)
                checkBox.isChecked = category.choise

                checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                    category.choise = isChecked
                }

            }
        }
    }

}