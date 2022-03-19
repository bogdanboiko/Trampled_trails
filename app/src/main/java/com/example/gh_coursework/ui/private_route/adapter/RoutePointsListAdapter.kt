package com.example.gh_coursework.ui.private_route.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gh_coursework.databinding.ItemPointBinding
import com.example.gh_coursework.ui.private_route.model.PrivateRoutePointDetailsPreviewModel

interface RoutePointsListCallback {
    fun onPointItemClick(point: PrivateRoutePointDetailsPreviewModel)
}

class RoutePointsListAdapter(val callback: RoutePointsListCallback) :
    RecyclerView.Adapter<RoutePointsListAdapter.PointViewHolder>() {

    var currentList: List<PrivateRoutePointDetailsPreviewModel> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointViewHolder {
        val binding = ItemPointBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PointViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PointViewHolder, position: Int) {
        currentList[position].let { holder.bind(it) }
    }

    override fun getItemCount(): Int = currentList.size

    inner class PointViewHolder(private val binding: ItemPointBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PrivateRoutePointDetailsPreviewModel) {
            with(binding) {
                txtName.text = item.caption
                txtDescription.text = item.description

//                Glide.with(itemView)
//                    .load(item.imgResources)
//                    .placeholder(imgMapImage.drawable)
//                    .error(R.drawable.ic_launcher_background)
//                    .transform(RoundedCorners(10))
//                    .into(imgMapImage)
            }

            binding.root.setOnClickListener {
                callback.onPointItemClick(item)
            }
        }
    }
}