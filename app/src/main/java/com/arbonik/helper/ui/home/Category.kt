package com.arbonik.helper.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arbonik.helper.R

enum class Category(val nam: String, val img : Int){
    HELP("Помощь по хозяйству", R.drawable.ic_local_florist_black_24dp),
    PETS("Прогулка с питомцем", R.drawable.ic_pets_black_24dp),
    PRODUCT("Поход за продуктами", R.drawable.ic_shopping_cart_black_24dp),
    COMMUNITY("Душевное общение, детские утренники", R.drawable.ic_cake_black_24dp),

}

class CategoryWidget (val category: Category, var choise : Boolean) {

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
                iconCategory.setImageResource(category.category.img)
                label.setText(category.category.nam)
                checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
                    category.choise = isChecked
                }
            }
        }
    }

}