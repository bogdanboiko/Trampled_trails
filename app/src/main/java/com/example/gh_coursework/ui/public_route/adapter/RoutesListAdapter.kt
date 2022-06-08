package com.example.gh_coursework.ui.public_route.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.gh_coursework.R
import com.example.gh_coursework.databinding.ItemPublicRouteBinding
import com.example.gh_coursework.ui.public_route.model.PublicRouteModel

interface RoutesListAdapterCallback {
    fun onRouteItemClick(publicRoute: PublicRouteModel)
}

class RoutesListAdapter(private val callback: RoutesListAdapterCallback) :
    PagingDataAdapter<PublicRouteModel, RoutesListAdapter.RouteViewHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        val binding = ItemPublicRouteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return RouteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class RouteViewHolder(private val binding: ItemPublicRouteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PublicRouteModel) {

            val circularProgressDrawable = CircularProgressDrawable(itemView.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            with(binding) {
                txtName.text = item.name
                txtDescription.text = item.description

                if (item.name.isEmpty() && item.description.isEmpty()) {
                    emptyDataPlaceholder.visibility = View.VISIBLE
                } else {
                    emptyDataPlaceholder.visibility = View.INVISIBLE
                }

                if (item.imageList.isEmpty()) {
                    Glide.with(itemView)
                        .load(R.drawable.ic_image_placeholder)
                        .placeholder(circularProgressDrawable)
                        .transform(RoundedCorners(10))
                        .into(imgMapImage)
                } else {
                    Glide.with(itemView)
                        .load(item.imageList.last())
                        .placeholder(circularProgressDrawable)
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

    object Diff : DiffUtil.ItemCallback<PublicRouteModel>() {
        override fun areItemsTheSame(
            oldItem: PublicRouteModel,
            newItem: PublicRouteModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: PublicRouteModel,
            newItem: PublicRouteModel
        ): Boolean {
            return oldItem.routeId == newItem.routeId && oldItem.description == newItem.description
        }
    }
}