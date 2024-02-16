package com.amefure.linkmark.View.Category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amefure.linkmark.Model.AppThemaColor
import com.amefure.linkmark.Model.Category
import com.amefure.linkmark.R

class CategoryAdapter(categoryList: List<Category>) :RecyclerView.Adapter<CategoryAdapter.MainViewHolder>() {

        private val _categoryList: MutableList<Category> = categoryList.toMutableList()

        override fun getItemCount(): Int = _categoryList.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
            return MainViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.fragment_category_item, parent, false)
            )
        }

        override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
            val category = _categoryList[position]

            val drawable = when(category.color) {
                AppThemaColor.RED.name -> R.drawable.circle_red
                AppThemaColor.YELLOW.name -> R.drawable.circle_yellow
                AppThemaColor.BLUE.name -> R.drawable.circle_blue
                AppThemaColor.GREEN.name -> R.drawable.circle_green
                AppThemaColor.PURPLE.name -> R.drawable.circle_purple
                else -> R.drawable.circle_red
            }
            holder.color.setBackgroundResource(drawable)
            holder.name.text = category.name
            holder.count.text = "3"
        }

        class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val color: View = itemView.findViewById(R.id.category_color)
            val name: TextView = itemView.findViewById(R.id.category_name)
            val count: TextView = itemView.findViewById(R.id.link_count_label)
        }

}