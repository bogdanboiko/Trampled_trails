package com.example.trampled_trails.ui.homepage.helper

import android.content.Context
import android.content.res.ColorStateList
import com.example.trampled_trails.databinding.FragmentHomepageBinding
import com.example.trampled_trails.ui.themes.MyAppTheme

fun syncHomepageFragmentTheme(
    theme: MyAppTheme,
    binding: FragmentHomepageBinding,
    context: Context
) {

    with(binding) {
        root.setBackgroundColor(theme.colorPrimary(context))
        txtUsername.setTextColor(theme.colorSecondaryVariant(context))
        progressBar.dataSyncProgressBar.indeterminateTintList =
            ColorStateList.valueOf(theme.colorSecondary(context))
        progressBar.txtProgressBar.setTextColor(theme.colorSecondaryVariant(context))
        btnSingIn.backgroundTintList = ColorStateList.valueOf(theme.colorSecondary(context))
        btnSave.backgroundTintList = ColorStateList.valueOf(theme.colorSecondary(context))
    }
}