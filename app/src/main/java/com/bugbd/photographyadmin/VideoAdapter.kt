package com.bugbd.photographyadmin

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bugbd.photographyadmin.databinding.VideoRowItemBinding
import com.bumptech.glide.Glide

class VideoAdapter(private val list: MutableList<Video>, private val context: Context) :
    RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    class ViewHolder(val binding: VideoRowItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            VideoRowItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val video = list[position]

        holder.binding.titleTxt.text = video.videoTitle
        holder.binding.descriptionTxt.text = video.Videodescription


    }

    override fun getItemCount(): Int {
        return list.size
    }
}


