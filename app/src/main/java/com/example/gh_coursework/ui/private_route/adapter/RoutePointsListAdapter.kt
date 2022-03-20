package com.example.gh_coursework.ui.private_route.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gh_coursework.databinding.ItemPointBinding
import com.example.gh_coursework.ui.private_route.model.PrivateRoutePointDetailsPreviewModel

interface RoutePointsListCallback {
    fun onPointItemClick(point: PrivateRoutePointDetailsPreviewModel)
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
                txtName.text = item.caption
                txtDescription.text = item.description
            }

            binding.root.setOnClickListener {
                callback.onPointItemClick(item)
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
            return oldItem.x == newItem.x && oldItem.y == newItem.y
        }
    }
}