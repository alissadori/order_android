package com.example.cafe.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cafe.R
import com.example.cafe.retrofit.response.category.Category
import kotlinx.android.synthetic.main.adapter_category.view.*

class CategoryAdapter(var categories: ArrayList<Category>, var listener: OnAdapterListener) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.adapter_category,
                parent, false
            )
        )

    override fun getItemCount() = categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.view.tv_name_category.text = category.nama_kategori
        holder.view.tv_name_category.setOnClickListener {
            listener.onClick(category)
        }

    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    }

    fun setData(data: List<Category>) {
        categories.clear()
        categories.addAll(data)
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onClick(category: Category)
    }

}