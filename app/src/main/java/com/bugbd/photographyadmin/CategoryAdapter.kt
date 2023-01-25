package com.bugbd.photographyadmin

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bugbd.photographyadmin.databinding.CategoryRowItemBinding
import com.bugbd.photographyadmin.databinding.SliderRowItemBinding
import com.bumptech.glide.Glide

class CategoryAdapter(
    private val categoryList: MutableList<Category>?,
    private val context: Context
) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    class ViewHolder(val binding: CategoryRowItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CategoryRowItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categoryList?.get(position)
        holder.binding.titleTxt.text = category?.categoryTitle
    }

    override fun getItemCount(): Int {
        return categoryList?.size ?: 0
    }


}