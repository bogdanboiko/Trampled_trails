package com.example.gh_coursework.ui.homepage

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dolatkia.animatedThemeManager.ThemeManager
import com.example.gh_coursework.R
import com.example.gh_coursework.databinding.ItemHomepageBinding
import com.example.gh_coursework.databinding.ItemHomepageSwitchBinding
import com.example.gh_coursework.ui.themes.DarkTheme
import com.example.gh_coursework.ui.themes.LightTheme

interface HomepageCallback {
    fun onEditClick()
    fun onLogOutClick()
}

data class Data(val viewType: Int, val image: Int, val text: String)

class HomepageAdapter(
    private val callback: HomepageCallback,
    private val sharedPreferences: SharedPreferences
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_THEME = 1
        const val VIEW_TYPE_EDIT = 2
        const val VIEW_TYPE_LOG_OUT = 3
    }

    var list = listOf(
        Data(VIEW_TYPE_THEME, R.drawable.ic_setting, "Theme"),
        Data(VIEW_TYPE_EDIT, R.drawable.ic_edit, "Edit profile"),
        Data(VIEW_TYPE_LOG_OUT, R.drawable.ic_log_out, "Log out")
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_THEME) {
            return ThemeViewHolder(
                ItemHomepageSwitchBinding.inflate(
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
            VIEW_TYPE_THEME -> {
                list[position].let { (holder as ThemeViewHolder).bind(it) }
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

    private inner class ThemeViewHolder(private val binding: ItemHomepageSwitchBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(item: Data) {
                with(binding) {
                    imgItemHomepage.setImageResource(item.image)
                    txtItemHomepage.text = item.text
                    switchTheme.isChecked = sharedPreferences.getBoolean("theme_switch", false)

                    switchTheme.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            ThemeManager.instance.changeTheme(LightTheme(), switchTheme)
                            sharedPreferences.edit().putString("theme", "light").apply()
                        } else {
                            ThemeManager.instance.changeTheme(DarkTheme(), switchTheme)
                            sharedPreferences.edit().putString("theme", "dark").apply()
                        }

                        sharedPreferences.edit().putBoolean("theme_switch", isChecked).apply()
                    }
            }
        }
    }

    private inner class EditViewHolder(private val binding: ItemHomepageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Data) {
            with(binding) {
                imgItemHomepage.setImageResource(item.image)
                txtItemHomepage.text = item.text

                root.setOnClickListener {
                    callback.onEditClick()
                }
            }
        }
    }

    private inner class LogOutViewHolder(private val binding: ItemHomepageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Data) {
            with(binding) {
                imgItemHomepage.setImageResource(item.image)
                txtItemHomepage.text = item.text

                root.setOnClickListener {
                    callback.onLogOutClick()
                }
            }
        }
    }
}