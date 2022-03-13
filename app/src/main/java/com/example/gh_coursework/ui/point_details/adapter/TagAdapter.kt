package com.example.gh_coursework.ui.point_details.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gh_coursework.databinding.ItemTagBinding
import com.example.gh_coursework.ui.point_details.model.PointTagModel

class TagAdapter : RecyclerView.Adapter<TagAdapter.TagViewHolder>() {
    private var list = listOf<PointTagModel>()

    fun setList(tagList: List<PointTagModel>) {
        list = tagList
        notifyDataSetChanged()
    }

    inner class TagViewHolder(private val binding: ItemTagBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tagModel: PointTagModel) {
            binding.tagCheckbox.text = tagModel.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        Log.e("e", "e")
        return TagViewHolder(
            ItemTagBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}