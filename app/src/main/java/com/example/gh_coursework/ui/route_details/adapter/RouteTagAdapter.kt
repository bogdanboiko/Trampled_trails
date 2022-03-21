package com.example.gh_coursework.ui.route_details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gh_coursework.databinding.ItemRouteTagBinding
import com.example.gh_coursework.ui.route_details.model.RouteTagModel

class RouteTagAdapter : RecyclerView.Adapter<RouteTagAdapter.TagViewHolder>() {

    var tagsList = mutableListOf<RouteTagModel>()
    var checkedTagList = mutableListOf<RouteTagModel>()
    var uncheckedTagList = mutableListOf<RouteTagModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        return TagViewHolder(
            ItemRouteTagBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.bind(tagsList[position])
    }

    override fun getItemCount(): Int {
        return tagsList.size
    }

    inner class TagViewHolder(private val binding: ItemRouteTagBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tagModel: RouteTagModel) {
            binding.tagCheckbox.apply {
                text = tagModel.name

                if (checkedTagList.contains(tagModel)) {
                    isChecked = true
                } else if (uncheckedTagList.contains(tagModel)) {
                    isChecked = false
                }

                setOnClickListener {
                    if (isChecked) {
                        uncheckedTagList.remove(tagModel)
                        checkedTagList.add(tagModel)
                    } else {
                        checkedTagList.remove(tagModel)
                        uncheckedTagList.add(tagModel)
                    }
                }
            }
        }
    }
}