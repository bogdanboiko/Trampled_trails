package com.example.gh_coursework.ui.private_image_details

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gh_coursework.databinding.ItemImageDetailsBinding
import com.example.gh_coursework.ui.private_image_details.model.ImageModel.PointImageModel

class ImageDetailsAdapter :
    ListAdapter<PointImageModel, ImageDetailsAdapter.ImageViewHolder>(Diff) {

    inner class ImageViewHolder(private val binding: ItemImageDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageModel: PointImageModel) {
            if (imageModel.isUploaded) {
                itemView.context?.let { it1 ->
                    Glide.with(it1)
                        .load(imageModel.image)
                        .into(binding.pointImage)
                }
            } else {
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ItemImageDetailsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    object Diff : DiffUtil.ItemCallback<PointImageModel>() {
        override fun areItemsTheSame(
            oldItem: PointImageModel,
            newItem: PointImageModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: PointImageModel,
            newItem: PointImageModel
        ): Boolean {
            return oldItem.pointId == newItem.pointId && oldItem.image == newItem.image
        }
    }
}