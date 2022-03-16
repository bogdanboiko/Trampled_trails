package com.example.gh_coursework.ui.private_route

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.gh_coursework.R
import com.example.gh_coursework.databinding.ItemRouteBinding
import com.example.gh_coursework.ui.private_route.model.PrivateRouteModel

interface RoutesListAdapterCallback {
    fun onRouteItemLongPressed(route: PrivateRouteModel)
    fun onRouteItemClick(route: PrivateRouteModel)
}

class RoutesListAdapter(val callback: RoutesListAdapterCallback) : RecyclerView.Adapter<RoutesListAdapter.RouteViewHolder>() {

    var currentList: List<PrivateRouteModel> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        val binding = ItemRouteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return RouteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RoutesListAdapter.RouteViewHolder, position: Int) {
        currentList[position].let { holder.bind(it) }
    }

    override fun getItemCount(): Int = currentList.size

    inner class RouteViewHolder(private val binding: ItemRouteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PrivateRouteModel) {
            with(binding) {
                txtName.text = item.name
                txtDescription.text = item.description
                txtRating.text = item.rating.toString()

                Glide.with(itemView)
                    .load(item.imgResources)
                    .placeholder(imgMapImage.drawable)
                    .error(R.drawable.ic_launcher_background)
                    .transform(RoundedCorners(10))
                    .into(imgMapImage)
            }

            binding.root.setOnLongClickListener {
                item.routeId?.let { id -> callback.onRouteItemLongPressed(item) }

                return@setOnLongClickListener true
            }

            binding.root.setOnClickListener {
                callback.onRouteItemClick(item)
            }
        }
    }
}