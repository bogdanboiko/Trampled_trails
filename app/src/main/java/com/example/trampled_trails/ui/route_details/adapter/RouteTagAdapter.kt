package com.example.trampled_trails.ui.route_details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trampled_trails.databinding.ItemTagBinding
import com.example.trampled_trails.ui.route_details.model.RouteTagModel
import java.util.*

class RouteTagAdapter : ListAdapter<RouteTagModel, RouteTagAdapter.TagViewHolder>(Diff) {

    private var checkedTagList = emptyList<RouteTagModel>()

    private var _addTagList = LinkedList<RouteTagModel>()
    val addTagList: LinkedList<RouteTagModel>
        get() = _addTagList

    private var _removeTagList = LinkedList<RouteTagModel>()
    val removeTagList: LinkedList<RouteTagModel>
        get() = _removeTagList

    fun insertCheckedTagList(list: List<RouteTagModel>) {
        checkedTagList = list
    }

    fun clearTagsLists() {
        _addTagList = LinkedList<RouteTagModel>()
        _removeTagList = LinkedList<RouteTagModel>()
    }

    inner class TagViewHolder(private val binding: ItemTagBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tagModel: RouteTagModel) {
            binding.tagCheckbox.apply {
                text = tagModel.name

                isChecked = (checkedTagList.contains(tagModel) && !removeTagList.contains(tagModel)) || addTagList.contains(tagModel)

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

    object Diff : DiffUtil.ItemCallback<RouteTagModel>() {
        override fun areItemsTheSame(oldItem: RouteTagModel, newItem: RouteTagModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: RouteTagModel, newItem: RouteTagModel): Boolean {
            return oldItem.tagId == newItem.tagId && oldItem.name == newItem.name
        }
    }
}