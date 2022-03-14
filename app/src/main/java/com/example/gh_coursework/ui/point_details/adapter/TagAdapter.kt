package com.example.gh_coursework.ui.point_details.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gh_coursework.databinding.ItemTagBinding
import com.example.gh_coursework.ui.point_details.model.PointTagModel

class TagAdapter : ListAdapter<PointTagModel, TagAdapter.TagViewHolder>(Diff) {

    inner class TagViewHolder(private val binding: ItemTagBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tagModel: PointTagModel) {
            binding.tagCheckbox.text = tagModel.name
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