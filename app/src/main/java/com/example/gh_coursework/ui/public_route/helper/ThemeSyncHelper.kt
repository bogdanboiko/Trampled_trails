package com.example.gh_coursework.ui.public_route.helper

import android.content.Context
import android.content.res.ColorStateList
import androidx.core.graphics.drawable.DrawableCompat
import com.example.gh_coursework.R
import com.example.gh_coursework.databinding.FragmentPublicRouteBinding
import com.example.gh_coursework.ui.themes.MyAppTheme

fun syncPublicFragmentTheme(theme: MyAppTheme, binding: FragmentPublicRouteBinding, context: Context) {
    val colorStates = ColorStateList(
        arrayOf(
            intArrayOf(-R.attr.checked),
            intArrayOf(R.attr.checked)
        ), intArrayOf(
            theme.colorSecondaryVariant(context),
            theme.colorOnSecondary(context)
        )
    )

    with(binding) {
        if (theme.id() == 0) {
            homepageButton.setImageResource(com.example.gh_coursework.R.drawable.ic_home_light)
        } else {
            homepageButton.setImageResource(com.example.gh_coursework.R.drawable.ic_home_dark)
        }

        DrawableCompat.wrap(getRoutesList.background)
            .setTint(theme.colorOnPrimary(context))
        DrawableCompat.wrap(getRoutePointsList.background)
            .setTint(theme.colorOnPrimary(context))
        getRoutesList.iconTint = ColorStateList.valueOf(theme.colorSecondary(context))
        getRoutesList.setTextColor(theme.colorPrimaryVariant(context))
        getRoutePointsList.iconTint =
            ColorStateList.valueOf(theme.colorSecondary(context))
        getRoutePointsList.setTextColor(theme.colorPrimaryVariant(context))

        DrawableCompat.wrap(bottomAppBar.background)
            .setTint(theme.colorPrimary(context))
        bottomNavigationView.itemIconTintList = colorStates
        bottomNavigationView.itemTextColor = colorStates

        bottomSheetDialogRoutes.root.backgroundTintList =
            ColorStateList.valueOf(theme.colorPrimary(context))
        bottomSheetDialogRoutes.routeFilterByTagButton.imageTintList =
            ColorStateList.valueOf(theme.colorSecondaryVariant(context))
        bottomSheetDialogRoutes.emptyDataPlaceholder.setTextColor(
            theme.colorSecondaryVariant(context)
        )

        bottomSheetDialogRoutePoints.root.backgroundTintList =
            ColorStateList.valueOf(theme.colorPrimary(context))
        bottomSheetDialogRoutePoints.emptyDataPlaceholder.setTextColor(
            theme.colorSecondaryVariant(context)
        )

        bottomSheetDialogRouteDetails.root.backgroundTintList =
            ColorStateList.valueOf(theme.colorPrimary(context))
        bottomSheetDialogRouteDetails.routeDetailsAddToFavouriteButton.imageTintList =
            ColorStateList.valueOf(theme.colorSecondaryVariant(context))
        bottomSheetDialogRouteDetails.emptyDataPlaceholder.setTextColor(
            theme.colorSecondaryVariant(context)
        )

        bottomSheetDialogPointDetails.root.backgroundTintList =
            ColorStateList.valueOf(theme.colorPrimary(context))
        bottomSheetDialogPointDetails.emptyDataPlaceholder.setTextColor(
            theme.colorSecondaryVariant(context)
        )
    }
}