package com.example.trampled_trails.ui.public_route.helper

import android.content.Context
import android.content.res.ColorStateList
import androidx.core.graphics.drawable.DrawableCompat
import android.R
import com.example.trampled_trails.databinding.FragmentPublicRouteBinding
import com.example.trampled_trails.ui.themes.MyAppTheme

fun syncPublicFragmentTheme(theme: MyAppTheme, binding: FragmentPublicRouteBinding, context: Context) {
    val colorStates = ColorStateList(
        arrayOf(
            intArrayOf(-R.attr.state_checked),
            intArrayOf(R.attr.state_checked)
        ), intArrayOf(
            theme.colorSecondaryVariant(context),
            theme.colorOnSecondary(context)
        )
    )

    with(binding) {
        if (theme.id() == 0) {
            homepageButton.setImageResource(com.example.trampled_trails.R.drawable.ic_home_light)
        } else {
            homepageButton.setImageResource(com.example.trampled_trails.R.drawable.ic_home_dark)
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
        bottomSheetDialogRouteDetails.routeDistance.setTextColor(
            theme.colorSecondaryVariant(context)
        )
        bottomSheetDialogRouteDetails.routeDetailsAddToFavouriteButton.imageTintList =
            ColorStateList.valueOf(theme.colorSecondaryVariant(context))
        bottomSheetDialogRouteDetails.routeCaptionText.setTextColor(
            theme.colorSecondaryVariant(context)
        )
        bottomSheetDialogRouteDetails.routeDescriptionText.setTextColor(
            theme.colorSecondaryVariant(context)
        )
        bottomSheetDialogRouteDetails.emptyDataPlaceholder.setTextColor(
            theme.colorSecondaryVariant(context)
        )

        bottomSheetDialogPointDetails.root.backgroundTintList =
            ColorStateList.valueOf(theme.colorPrimary(context))
        bottomSheetDialogPointDetails.pointCaptionText.setTextColor(
            theme.colorSecondaryVariant(context)
        )
        bottomSheetDialogPointDetails.pointDescriptionText.setTextColor(
            theme.colorSecondaryVariant(context)
        )
        bottomSheetDialogPointDetails.emptyDataPlaceholder.setTextColor(
            theme.colorSecondaryVariant(context)
        )
    }
}