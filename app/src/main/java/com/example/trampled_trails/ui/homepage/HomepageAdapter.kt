package com.example.trampled_trails.ui.homepage

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dolatkia.animatedThemeManager.ThemeManager
import com.example.trampled_trails.R
import com.example.trampled_trails.databinding.ItemHomepageBinding
import com.example.trampled_trails.databinding.ItemHomepageSwitchBinding
import com.example.trampled_trails.ui.themes.DarkTheme
import com.example.trampled_trails.ui.themes.LightTheme

interface HomepageCallback {
    fun onEditClick()
    fun onLogOutClick()
}

data class Data(val viewType: Int)

class HomepageAdapter(
    private val callback: HomepageCallback,
    private val sharedPreferences: SharedPreferences
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_THEME = 1
        const val VIEW_TYPE_EDIT = 2
        const val VIEW_TYPE_LOG_OUT = 3
    }

    var viewTypeList = listOf(
        Data(VIEW_TYPE_THEME),
        Data(VIEW_TYPE_EDIT),
        Data(VIEW_TYPE_LOG_OUT)
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

    override fun getItemCount(): Int = viewTypeList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (viewTypeList[position].viewType) {
            VIEW_TYPE_THEME -> {
                viewTypeList[position].let { (holder as ThemeViewHolder).bind() }
            }
            VIEW_TYPE_EDIT -> {
                viewTypeList[position].let { (holder as EditViewHolder).bind() }
            }
            VIEW_TYPE_LOG_OUT -> {
                viewTypeList[position].let { (holder as LogOutViewHolder).bind() }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return viewTypeList[position].viewType
    }

    private inner class ThemeViewHolder(private val binding: ItemHomepageSwitchBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind() {
                with(binding) {
                    imgItemHomepage.setImageResource(R.drawable.ic_setting)
                    txtItemHomepage.text = itemView.context.resources.getText(R.string.homepage_theme)
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

        fun bind() {
            with(binding) {
                imgItemHomepage.setImageResource(R.drawable.ic_edit)
                txtItemHomepage.text = itemView.resources.getText(R.string.homepage_edit_profile)

                root.setOnClickListener {
                    callback.onEditClick()
                }
            }
        }
    }

    private inner class LogOutViewHolder(private val binding: ItemHomepageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            with(binding) {
                imgItemHomepage.setImageResource(R.drawable.ic_log_out)
                txtItemHomepage.text = itemView.resources.getText(R.string.homepage_log_out)

                root.setOnClickListener {
                    callback.onLogOutClick()
                }
            }
        }
    }
}