package com.example.gh_coursework.ui.themes

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.gh_coursework.R

class DarkTheme : MyAppTheme {
    override fun id(): Int {
        return 1
    }

    override fun colorPrimary(context: Context): Int {
        return ContextCompat.getColor(context, R.color.gray)
    }

    override fun colorPrimaryVariant(context: Context): Int {
        return ContextCompat.getColor(context, R.color.gray_dark)
    }

    override fun colorOnPrimary(context: Context): Int {
        return ContextCompat.getColor(context, R.color.gray_light)
    }

    override fun colorSecondary(context: Context): Int {
        return ContextCompat.getColor(context, R.color.yellow)
    }

    override fun colorSecondaryVariant(context: Context): Int {
        return ContextCompat.getColor(context, R.color.yellow_dark)
    }

    override fun colorOnSecondary(context: Context): Int {
        return ContextCompat.getColor(context, R.color.yellow_light)
    }
}