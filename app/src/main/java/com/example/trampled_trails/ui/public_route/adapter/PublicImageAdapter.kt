package com.example.trampled_trails.ui.public_route.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.trampled_trails.databinding.ItemImagesPreviewBinding

class PublicImageAdapter(private val onItemCLick: View.OnClickListener) :
    ListAdapter<String, PublicImageAdapter.ImageViewHolder>(Diff) {

    inner class ImageViewHolder(private val binding: ItemImagesPreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(image: String) {

            val circularProgressDrawable = CircularProgressDrawable(itemView.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            itemView.setOnClickListener(onItemCLick)
            itemView.context?.let { it1 ->
                Glide.with(it1)
                    .load(image)
                    .transform(MultiTransformation(CenterCrop(), RoundedCorners(10)))
                    .placeholder(circularProgressDrawable)
                    .into(binding.pointImage)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ItemImagesPreviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}

    object Diff : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean {
            return oldItem == newItem
        }
    }
