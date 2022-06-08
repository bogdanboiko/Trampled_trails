package com.example.gh_coursework.ui.themes

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.gh_coursework.R

class LightTheme : MyAppTheme {
    override fun id(): Int {
        return 0
    }

    override fun colorPrimary(context: Context): Int {
        return ContextCompat.getColor(context, R.color.beige)
    }

    override fun colorPrimaryVariant(context: Context): Int {
        return ContextCompat.getColor(context, R.color.beige_dark)
    }

    override fun colorOnPrimary(context: Context): Int {
        return ContextCompat.getColor(context, R.color.white)
    }

    override fun colorSecondary(context: Context): Int {
        return ContextCompat.getColor(context, R.color.red)
    }

    override fun colorSecondaryVariant(context: Context): Int {
        return ContextCompat.getColor(context, R.color.red_dark)
    }

    override fun colorOnSecondary(context: Context): Int {
        return ContextCompat.getColor(context, R.color.red_light)
    }
}