package com.example.gh_coursework.ui.private_point.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gh_coursework.databinding.ItemTagBinding
import com.example.gh_coursework.ui.point_details.model.PointTagModel

class PointFilterTagAdapter :
    ListAdapter<PointTagModel, PointFilterTagAdapter.TagViewHolder>(Diff) {
    private var _filterByTagList = mutableListOf<PointTagModel>()
    val filterByTagList: List<PointTagModel>
        get() = _filterByTagList

    fun setFilteredTags(tags: MutableList<PointTagModel>) {
        _filterByTagList = tags
    }

    inner class TagViewHolder(private val binding: ItemTagBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tagModel: PointTagModel) {
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

    object Diff : DiffUtil.ItemCallback<PointTagModel>() {
        override fun areItemsTheSame(oldItem: PointTagModel, newItem: PointTagModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PointTagModel, newItem: PointTagModel): Boolean {
            return oldItem.tagId == newItem.tagId && oldItem.name == newItem.name
        }
    }
}