package com.example.gh_coursework.ui.public_route.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gh_coursework.databinding.ItemTagBinding

class PublicRouteFilterByTagAdapter : ListAdapter<String, PublicRouteFilterByTagAdapter.TagViewHolder>(Diff) {
    private var _filterByTagList = mutableListOf<String>()
    val filterByTagList: List<String>
        get() = _filterByTagList

    fun setFilteredTags(tags: MutableList<String>) {
        _filterByTagList = tags
    }

    inner class TagViewHolder(private val binding: ItemTagBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tag: String) {
            binding.tagCheckbox.apply {
                text = tag
                if (_filterByTagList.contains(tag)) {
                    this.isChecked = true
                }

                this.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        _filterByTagList.add(tag)
                    } else {
                        _filterByTagList.remove(tag)
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

    object Diff : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}