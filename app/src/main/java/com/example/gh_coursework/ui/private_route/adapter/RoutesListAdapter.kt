package com.example.gh_coursework.ui.private_route.adapter

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
import com.example.gh_coursework.databinding.ItemRouteBinding
import com.example.gh_coursework.ui.private_route.model.RouteModel

interface RoutesListAdapterCallback {
    fun onRouteItemClick(route: RouteModel)
}

class RoutesListAdapter(val callback: RoutesListAdapterCallback) :
    ListAdapter<RouteModel, RoutesListAdapter.RouteViewHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        val binding = ItemRouteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return RouteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int = currentList.size

    inner class RouteViewHolder(private val binding: ItemRouteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RouteModel) {
            with(binding) {
                if (item.name?.isEmpty() == true && item.description?.isEmpty() == true && item.rating == null) {
                    txtName.visibility = View.INVISIBLE
                    txtDescription.visibility = View.INVISIBLE
                    txtRating.visibility = View.INVISIBLE

                    emptyDataPlaceholder.visibility = View.VISIBLE

                } else {
                    txtName.text = item.name
                    txtDescription.text = item.description
                    txtRating.text = item.rating.toString()
                    emptyDataPlaceholder.visibility = View.INVISIBLE
                }

                if (item.imageList.isEmpty()) {
                    Glide.with(itemView)
                        .load(R.drawable.ic_image_placeholder)
                        .placeholder(imgMapImage.drawable)
                        .transform(RoundedCorners(10))
                        .into(imgMapImage)
                } else if (item.imageList.isNotEmpty()) {
                    Glide.with(itemView)
                        .load(Drawable.createFromPath(Uri.parse(item.imageList[0].image).path))
                        .placeholder(imgMapImage.drawable)
                        .error(R.drawable.ic_image_placeholder)
                        .transform(RoundedCorners(10))
                        .into(imgMapImage)
                }

                root.setOnClickListener {
                    callback.onRouteItemClick(item)
                }
            }
        }
    }

    object Diff : DiffUtil.ItemCallback<RouteModel>() {
        override fun areItemsTheSame(
            oldItem: RouteModel,
            newItem: RouteModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: RouteModel,
            newItem: RouteModel
        ): Boolean {
            return oldItem.routeId == newItem.routeId && oldItem.description == newItem.description
        }
    }
}