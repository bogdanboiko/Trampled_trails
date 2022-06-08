package com.example.gh_coursework.ui.private_route.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gh_coursework.databinding.ItemTagBinding
import com.example.gh_coursework.ui.route_details.model.RouteTagModel

class RouteFilterTagAdapter : ListAdapter<RouteTagModel, RouteFilterTagAdapter.TagViewHolder>(Diff) {
    private var _filterByTagList = mutableListOf<RouteTagModel>()
    val filterByTagList: List<RouteTagModel>
        get() = _filterByTagList

    fun setFilteredTags(tags: MutableList<RouteTagModel>) {
        _filterByTagList = tags
    }

    inner class TagViewHolder(private val binding: ItemTagBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tagModel: RouteTagModel) {
            binding.tagCheckbox.apply {
                text = tagModel.name
                this.isChecked = _filterByTagList.contains(tagModel)

                this.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        _filterByTagList.add(tagModel)
                    } else {
                        _filterByTagList.remove(tagModel)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        return TagViewHolder(
            ItemTagBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    object Diff : DiffUtil.ItemCallback<RouteTagModel>() {
        override fun areItemsTheSame(oldItem: RouteTagModel, newItem: RouteTagModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: RouteTagModel, newItem: RouteTagModel): Boolean {
            return oldItem.tagId == newItem.tagId && oldItem.name == newItem.name
        }
    }
}