package com.example.gh_coursework.ui.themes

import android.content.Context
import com.dolatkia.animatedThemeManager.AppTheme

interface MyAppTheme : AppTheme {
    fun colorPrimary(context: Context): Int
    fun colorPrimaryVariant(context: Context): Int
    fun colorOnPrimary(context: Context): Int
    fun colorSecondary(context: Context): Int
    fun colorSecondaryVariant(context: Context): Int
    fun colorOnSecondary(context: Context): Int
}