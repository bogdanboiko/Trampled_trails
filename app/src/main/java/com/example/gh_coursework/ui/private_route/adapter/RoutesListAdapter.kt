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
import com.example.gh_coursework.databinding.ItemRouteBinding
import com.example.gh_coursework.ui.private_route.model.PrivateRouteModel

interface RoutesListAdapterCallback {
    fun onRouteItemClick(route: PrivateRouteModel)
    fun onRouteItemImageClick(route: PrivateRouteModel)
}

class RoutesListAdapter(val callback: RoutesListAdapterCallback) :
    ListAdapter<PrivateRouteModel, RoutesListAdapter.RouteViewHolder>(Diff) {

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

        fun bind(item: PrivateRouteModel) {
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
                }

                if (item.imgResources?.isEmpty() == true) {
                    imgMapImage.setOnClickListener {
                        callback.onRouteItemImageClick(item)
                    }
                }

                Glide.with(itemView)
                    .load(item.imgResources)
                    .placeholder(imgMapImage.drawable)
                    .error(R.drawable.ic_image_placeholder)
                    .transform(RoundedCorners(10))
                    .into(imgMapImage)

                root.setOnClickListener {
                    callback.onRouteItemClick(item)
                }
            }
        }
    }

    object Diff : DiffUtil.ItemCallback<PrivateRouteModel>() {
        override fun areItemsTheSame(
            oldItem: PrivateRouteModel,
            newItem: PrivateRouteModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: PrivateRouteModel,
            newItem: PrivateRouteModel
        ): Boolean {
            return oldItem.routeId == newItem.routeId
                    && oldItem.coordinatesList == newItem.coordinatesList
        }
    }
}