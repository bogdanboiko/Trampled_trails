package com.example.gh_coursework.ui.private_route.adapter

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
import com.example.gh_coursework.ui.private_route.model.PrivateRoutePointDetailsPreviewModel

interface RoutePointsListCallback {
    fun onPointItemClick(pointId: Long)
}

class RoutePointsListAdapter(val callback: RoutePointsListCallback) :
    ListAdapter<PrivateRoutePointDetailsPreviewModel, RoutePointsListAdapter.PointViewHolder>(Diff) {

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

        fun bind(item: PrivateRoutePointDetailsPreviewModel) {
            with(binding) {
                if (item.caption.isEmpty() && item.description.isEmpty()) {
                    txtName.visibility = View.INVISIBLE
                    txtDescription.visibility = View.INVISIBLE

                    emptyDataPlaceholder.visibility = View.VISIBLE

                } else {
                    txtName.text = item.caption
                    txtDescription.text = item.description
                }

                if (item.imageList.isEmpty()) {
                    Glide.with(itemView)
                        .load(R.drawable.ic_image_placeholder)
                        .placeholder(imgMapImage.drawable)
                        .transform(RoundedCorners(10))
                        .into(imgMapImage)
                }

                if (item.imageList.isNotEmpty()) {
                    Glide.with(itemView)
                        .load(item.imageList[0])
                        .placeholder(imgMapImage.drawable)
                        .error(R.drawable.ic_image_placeholder)
                        .transform(RoundedCorners(10))
                        .into(imgMapImage)
                }

                root.setOnClickListener {
                    item.pointId.let { it1 -> callback.onPointItemClick(it1) }
                }
            }

        }
    }

    object Diff : DiffUtil.ItemCallback<PrivateRoutePointDetailsPreviewModel>() {
        override fun areItemsTheSame(
            oldItem: PrivateRoutePointDetailsPreviewModel,
            newItem: PrivateRoutePointDetailsPreviewModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: PrivateRoutePointDetailsPreviewModel,
            newItem: PrivateRoutePointDetailsPreviewModel
        ): Boolean {
            return oldItem.pointId == newItem.pointId
                    && oldItem.caption == newItem.caption
                    && oldItem.description == newItem.description
                    && oldItem.imageList == newItem.imageList
                    && oldItem.tagList == newItem.tagList
        }
    }
}