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
import com.example.trampled_trails.databinding.ItemRouteBinding
import com.example.trampled_trails.ui.private_route.model.RouteModel

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

            val circularProgressDrawable =  CircularProgressDrawable(itemView.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            with(binding) {
                if (item.name?.isEmpty() == true && item.description?.isEmpty() == true) {
                    emptyDataPlaceholder.visibility = View.VISIBLE
                } else {
                    emptyDataPlaceholder.visibility = View.GONE
                }

                txtName.text = item.name
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