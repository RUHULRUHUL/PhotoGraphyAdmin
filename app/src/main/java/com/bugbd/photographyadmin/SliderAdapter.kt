package com.bugbd.photographyadmin

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bugbd.photographyadmin.databinding.SliderRowItemBinding
import com.bumptech.glide.Glide

class SliderAdapter(private val list: MutableList<Slider>, private val context: Context) :
    RecyclerView.Adapter<SliderAdapter.ViewHolder>() {

    class ViewHolder(val binding: SliderRowItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SliderRowItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val slider = list[position]

        Glide.with(context).load(slider.imageUri?.toUri()).into(holder.binding.SliderImg)
        holder.binding.titleTxt.text = slider.title


    }

    override fun getItemCount(): Int {
        return list.size
    }


}