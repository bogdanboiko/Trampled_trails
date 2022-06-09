package com.example.trampled_trails.ui.route_details.image_details

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.trampled_trails.databinding.ItemImageDetailsBinding
import com.example.trampled_trails.ui.private_image_details.model.ImageModel

class RouteImageDetailsAdapter : ListAdapter<ImageModel, RouteImageDetailsAdapter.ImageViewHolder>(Diff) {

    inner class ImageViewHolder(private val binding: ItemImageDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imageModel: ImageModel) {

            val circularProgressDrawable = CircularProgressDrawable(itemView.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            if (imageModel.isUploaded) {
                itemView.context?.let { context ->
                    Glide.with(context)
                        .load(imageModel.image)
                        .placeholder(circularProgressDrawable)
                        .into(binding.pointImage)
                }
            } else {
                val imageUri = Uri.parse(imageModel.image)

                itemView.context?.contentResolver?.openInputStream(imageUri).use { inputStream ->
                    Drawable.createFromStream(inputStream, imageUri.toString())?.let { image ->
                        itemView.context?.let { context ->
                            Glide.with(context)
                                .load(image)
                                .placeholder(circularProgressDrawable)
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