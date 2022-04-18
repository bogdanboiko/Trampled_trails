package com.example.gh_coursework.ui.private_point.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gh_coursework.databinding.ItemRouteTagBinding
import com.example.gh_coursework.ui.point_details.model.PointTagModel
import com.example.gh_coursework.ui.route_details.model.RouteTagModel
import java.util.*

class PointFilterTagAdapter :
    ListAdapter<PointTagModel, PointFilterTagAdapter.TagViewHolder>(Diff) {
    private var _filterByTagList = mutableListOf<PointTagModel>()
    val filterByTagList: List<PointTagModel>
        get() = _filterByTagList

    fun setFilteredTags(tags: MutableList<PointTagModel>) {
        _filterByTagList = tags
    }

    inner class TagViewHolder(private val binding: ItemRouteTagBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tagModel: PointTagModel) {
            binding.tagCheckbox.apply {
                text = tagModel.name
                if (_filterByTagList.contains(tagModel)) {
                    this.isChecked = true
                }

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
            ItemRouteTagBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    object Diff : DiffUtil.ItemCallback<PointTagModel>() {
        override fun areItemsTheSame(oldItem: PointTagModel, newItem: PointTagModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PointTagModel, newItem: PointTagModel): Boolean {
            return oldItem.tagId == newItem.tagId && oldItem.name == newItem.name
        }
    }
}