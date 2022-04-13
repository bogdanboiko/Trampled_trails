package com.example.gh_coursework.ui.homepage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gh_coursework.R
import com.example.gh_coursework.databinding.ItemHomepageBinding

data class Data(val viewType: Int, val image: Int, val text: String)

class HomepageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_SETTINGS = 1
        const val VIEW_TYPE_EDIT = 2
        const val VIEW_TYPE_LOG_OUT = 3
    }

    var list = listOf(
        Data(VIEW_TYPE_SETTINGS, R.drawable.ic_setting, "Settings"),
        Data(VIEW_TYPE_EDIT, R.drawable.ic_edit, "Edit profile"),
        Data(VIEW_TYPE_LOG_OUT, R.drawable.ic_log_out, "Log out")
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_SETTINGS) {
            return SettingsViewHolder(
                ItemHomepageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else if (viewType == VIEW_TYPE_EDIT) {
            return EditViewHolder(
                ItemHomepageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        return LogOutViewHolder(
            ItemHomepageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (list[position].viewType) {
            VIEW_TYPE_SETTINGS -> {
                list[position].let { (holder as SettingsViewHolder).bind(it) }
            }
            VIEW_TYPE_EDIT -> {
                list[position].let { (holder as EditViewHolder).bind(it) }
            }
            VIEW_TYPE_LOG_OUT -> {
                list[position].let { (holder as LogOutViewHolder).bind(it) }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].viewType
    }

    private inner class SettingsViewHolder(private val binding: ItemHomepageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Data) {
            binding.imgItemHomepage.setImageResource(item.image)
            binding.txtItemHomepage.text = item.text
        }
    }

    private inner class EditViewHolder(private val binding: ItemHomepageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Data) {
            binding.imgItemHomepage.setImageResource(item.image)
            binding.txtItemHomepage.text = item.text
        }
    }

    private inner class LogOutViewHolder(private val binding: ItemHomepageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Data) {
            binding.imgItemHomepage.setImageResource(item.image)
            binding.txtItemHomepage.text = item.text
        }
    }
}