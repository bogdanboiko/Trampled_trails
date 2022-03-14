package com.example.gh_coursework.ui.point_details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gh_coursework.databinding.ItemTagBinding
import com.example.gh_coursework.ui.point_details.model.PointTagModel
import java.util.*

class TagAdapter : ListAdapter<PointTagModel, TagAdapter.TagViewHolder>(Diff) {
    private var checkedTagList = emptyList<PointTagModel>()
    private var _addTagList = LinkedList<PointTagModel>()

    val addTagList: LinkedList<PointTagModel>
        get() = _addTagList

    private var _removeTagList = LinkedList<PointTagModel>()

    val removeTagList: LinkedList<PointTagModel>
        get() = _removeTagList

    fun insertCheckedTagList(list: List<PointTagModel>) {
        checkedTagList = list
    }

    fun clearTagsLists() {
        _addTagList = LinkedList<PointTagModel>()
        _removeTagList = LinkedList<PointTagModel>()
    }

    inner class TagViewHolder(private val binding: ItemTagBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tagModel: PointTagModel) {
            binding.tagCheckbox.apply {
                text = tagModel.name

                isChecked = checkedTagList.contains(tagModel)

                setOnClickListener {
                    if (isChecked) {
                        if (!checkedTagList.contains(tagModel)) {
                            _addTagList.add(tagModel)
                        } else {
                            _removeTagList.remove(tagModel)
                        }
                    } else {
                        if (checkedTagList.contains(tagModel)) {
                            _removeTagList.add(tagModel)
                        } else {
                            _addTagList.remove(tagModel)
                        }
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