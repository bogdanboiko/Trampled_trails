package com.example.gh_coursework.ui.point_details.adapter

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.gh_coursework.databinding.ItemImagePointBinding
import com.example.gh_coursework.ui.model.ImageModel

class ImageAdapter(private val onItemCLick: View.OnClickListener) :
    ListAdapter<ImageModel, ImageAdapter.ImageViewHolder>(Diff) {

    inner class ImageViewHolder(private val binding: ItemImagePointBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageModel: ImageModel) {
                itemView.setOnClickListener(onItemCLick)

                val imageUri = Uri.parse(imageModel.image)
                itemView.context?.contentResolver?.openInputStream(imageUri).use {
                    val image = Drawable.createFromStream(it, imageUri.toString())
                    if (image != null) {
                        itemView.context?.let { it1 ->
                            Glide.with(it1)
                                .load(image)
                                .transform(MultiTransformation(CenterCrop(), RoundedCorners(10)))
                                .into(binding.pointImage)
                        }
                    }
                }
        }
    }

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

    object Diff : DiffUtil.ItemCallback<ImageModel>() {
        override fun areItemsTheSame(
            oldItem: ImageModel,
            newItem: ImageModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ImageModel,
            newItem: ImageModel
        ): Boolean {
            return when (oldItem) {
                is ImageModel.PointImageModel ->
                    oldItem.pointId == (newItem as ImageModel.PointImageModel).pointId
                            && oldItem.image == newItem.image
                is ImageModel.RouteImageModel ->
                    oldItem.routeId == (newItem as ImageModel.RouteImageModel).routeId
                            && oldItem.image == newItem.image
            }
        }
    }
}