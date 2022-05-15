package com.example.gh_coursework.ui.private_point.adapter

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.gh_coursework.R
import com.example.gh_coursework.databinding.ItemPointBinding
import com.example.gh_coursework.ui.private_point.model.PrivatePointDetailsModel

interface PointsListCallback {
    fun onPointItemClick(pointDetails: PrivatePointDetailsModel)
}

class PointsListAdapter(val callback: PointsListCallback) :
    ListAdapter<PrivatePointDetailsModel, PointsListAdapter.PointViewHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointViewHolder {
        return PointViewHolder(
            ItemPointBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PointViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemCount(): Int = currentList.size

    inner class PointViewHolder(private val binding: ItemPointBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PrivatePointDetailsModel) {
            with(binding) {
                txtName.text = item.caption
                txtDescription.text = item.description

                if (item.caption.isEmpty() && item.description.isEmpty()) {
                    emptyDataPlaceholder.visibility = View.VISIBLE
                } else {
                    emptyDataPlaceholder.visibility = View.GONE
                }

                if (item.imageList.isEmpty()) {
                    Glide.with(itemView)
                        .load(R.drawable.ic_image_placeholder)
                        .placeholder(imgMapImage.drawable)
                        .transform(RoundedCorners(10))
                        .into(imgMapImage)
                } else {
                    val imageLink = item.imageList[0]

                    if (imageLink.isUploaded) {
                        Glide.with(itemView)
                            .load(imageLink.image)
                            .placeholder(imgMapImage.drawable)
                            .error(R.drawable.ic_image_placeholder)
                            .transform(RoundedCorners(10))
                            .into(imgMapImage)
                    } else {
                        Glide.with(itemView)
                            .load(Drawable.createFromPath(Uri.parse(item.imageList[0].image).path))
                            .placeholder(imgMapImage.drawable)
                            .error(R.drawable.ic_image_placeholder)
                            .transform(RoundedCorners(10))
                            .into(imgMapImage)
                    }
                }

                root.setOnClickListener {
                    callback.onPointItemClick(item)
                }
            }

        }
    }

    object Diff : DiffUtil.ItemCallback<PrivatePointDetailsModel>() {
        override fun areItemsTheSame(
            oldItem: PrivatePointDetailsModel,
            newItem: PrivatePointDetailsModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: PrivatePointDetailsModel,
            newItem: PrivatePointDetailsModel
        ): Boolean {
            return oldItem.pointId == newItem.pointId
                    && oldItem.x == newItem.x
                    && oldItem.y == newItem.y
                    && oldItem.caption == newItem.caption
                    && oldItem.description == newItem.description
                    && oldItem.imageList.containsAll(newItem.imageList)
                    && oldItem.tagList.containsAll(newItem.tagList)
        }
    }
}