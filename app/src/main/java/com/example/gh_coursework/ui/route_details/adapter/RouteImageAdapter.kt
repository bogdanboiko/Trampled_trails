package com.example.gh_coursework.ui.route_details.adapter

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gh_coursework.databinding.ItemImagePointBinding
import com.example.gh_coursework.ui.route_details.model.RouteImageModel

class RouteImageAdapter(private val onItemCLick: View.OnClickListener) :
    ListAdapter<RouteImageModel, RouteImageAdapter.ImageViewHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ItemImagePointBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ImageViewHolder(private val binding: ItemImagePointBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageModel: RouteImageModel) {
            itemView.setOnClickListener(onItemCLick)

            val imageUri = Uri.parse(imageModel.image)
            itemView.context?.contentResolver?.openInputStream(imageUri).use {
                val image = Drawable.createFromStream(it, imageUri.toString())
                if (image != null) {
                    itemView.context?.let { it1 ->
                        Glide.with(it1)
                            .load(image)
                            .into(binding.pointImage)
                    }
                }
            }
        }
    }

    object Diff : DiffUtil.ItemCallback<RouteImageModel>() {
        override fun areItemsTheSame(
            oldItem: RouteImageModel,
            newItem: RouteImageModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: RouteImageModel,
            newItem: RouteImageModel
        ): Boolean {
            return oldItem.routeId == newItem.routeId && oldItem.image == newItem.image
        }
    }

}