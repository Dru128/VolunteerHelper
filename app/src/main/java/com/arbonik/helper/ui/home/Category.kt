package com.arbonik.helper.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arbonik.helper.R

enum class Category(val nam: String){
    HELP("Помощь по хозяйству"),
    HELP1("Прогулка с питомцем"),
    HELP2("Поход за прогулками"),
    HELP3("Душевное общение"),
    HELP5("Помощь по хозяйству")
}

class CategoryWidget (val iconCategory : Int, val text : String, val choise : Boolean) {
}

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
            val iconCategory = view.findViewById<ImageView>(R.id.img)
            val label = view.findViewById<TextView>(R.id.label)
            val checkBox = view.findViewById<CheckBox>(R.id.check)

            fun bind(category: CategoryWidget){
                iconCategory.setImageResource(category.iconCategory)
                label.setText(category.text)
                checkBox.isChecked = category.choise
            }
        }
    }

}