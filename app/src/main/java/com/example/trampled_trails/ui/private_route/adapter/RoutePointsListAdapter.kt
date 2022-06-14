package com.example.trampled_trails.ui.private_route.adapter

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.trampled_trails.R
import com.example.trampled_trails.databinding.ItemPointBinding
import com.example.trampled_trails.ui.private_route.model.RoutePointModel

interface RoutePointsListCallback {
    fun onPointItemClick(pointId: String)
}

class RoutePointsListAdapter(val callback: RoutePointsListCallback) :
    ListAdapter<RoutePointModel, RoutePointsListAdapter.PointViewHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointViewHolder {
        val binding = ItemPointBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PointViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PointViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int = currentList.size

    inner class PointViewHolder(private val binding: ItemPointBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RoutePointModel) {

            val circularProgressDrawable =  CircularProgressDrawable(itemView.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            with(binding) {
                if (item.caption.isEmpty() && item.description.isEmpty()) {
                    emptyDataPlaceholder.visibility = View.VISIBLE
                } else {
                    emptyDataPlaceholder.visibility = View.GONE
                }

                txtName.text = item.caption
                txtDescription.text = item.description

                if (item.imageList.isEmpty()) {
                    Glide.with(itemView)
                        .load(R.drawable.ic_image_placeholder)
                        .placeholder(circularProgressDrawable)
                        .transform(RoundedCorners(10))
                        .into(imgMapImage)
                } else {
                    val imageLink = item.imageList[0]

                    if (imageLink.isUploaded) {
                        Glide.with(itemView)
                            .load(imageLink.image)
                            .placeholder(circularProgressDrawable)
                            .error(R.drawable.ic_image_placeholder)
                            .transform(RoundedCorners(10))
                            .into(imgMapImage)
                    } else {
                        Glide.with(itemView)
                            .load(Drawable.createFromPath(Uri.parse(item.imageList[0].image).path))
                            .placeholder(circularProgressDrawable)
                            .error(R.drawable.ic_image_placeholder)
                            .transform(RoundedCorners(10))
                            .into(imgMapImage)
                    }
                }

                root.setOnClickListener {
                    callback.onPointItemClick(item.pointId)
                }
            }

        }
    }

    object Diff : DiffUtil.ItemCallback<RoutePointModel>() {
        override fun areItemsTheSame(
            oldItem: RoutePointModel,
            newItem: RoutePointModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: RoutePointModel,
            newItem: RoutePointModel
        ): Boolean {
            return oldItem.pointId == newItem.pointId
                    && oldItem.caption == newItem.caption
                    && oldItem.description == newItem.description
                    && oldItem.imageList == newItem.imageList
                    && oldItem.tagList == newItem.tagList
        }
    }
}