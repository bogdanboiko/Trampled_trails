package com.example.gh_coursework.ui.private_route.helper

import android.R
import android.content.Context
import android.content.res.ColorStateList
import androidx.core.graphics.drawable.DrawableCompat
import com.example.gh_coursework.databinding.FragmentPrivateRouteBinding
import com.example.gh_coursework.ui.themes.MyAppTheme

fun syncPrivateRoutesFragmentTheme(theme: MyAppTheme, binding: FragmentPrivateRouteBinding, context: Context) {
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
            mapRoutePointModSwitcher.setImageResource(com.example.gh_coursework.R.drawable.ic_routes_light)
            homepageButton.setImageResource(com.example.gh_coursework.R.drawable.ic_home_light)

            pointTypeSwitchButton.setSwitchToggleCheckedDrawableRes(com.example.gh_coursework.R.drawable.ic_pin_route_rotated_light)
            pointTypeSwitchButton.setSwitchToggleNotCheckedDrawableRes(com.example.gh_coursework.R.drawable.ic_pin_point_rotated_light)
        } else {
            mapRoutePointModSwitcher.setImageResource(com.example.gh_coursework.R.drawable.ic_routes_dark)
            homepageButton.setImageResource(com.example.gh_coursework.R.drawable.ic_home_dark)

            pointTypeSwitchButton.setSwitchToggleCheckedDrawableRes(com.example.gh_coursework.R.drawable.ic_pin_route_rotated_dark)
            pointTypeSwitchButton.setSwitchToggleNotCheckedDrawableRes(com.example.gh_coursework.R.drawable.ic_pin_point_rotated_dark)
        }

        saveRouteButton.backgroundTintList =
            ColorStateList.valueOf(theme.colorSecondary(context))
        createButton.backgroundTintList =
            ColorStateList.valueOf(theme.colorSecondary(context))

        resetRouteButton.backgroundTintList =
            ColorStateList.valueOf(theme.colorSecondary(context))
        undoPointCreatingButton.backgroundTintList =
            ColorStateList.valueOf(theme.colorSecondary(context))

        pointTypeSwitchButton.switchBkgCheckedColor =
            theme.colorSecondaryVariant(context)
        pointTypeSwitchButton.switchBkgNotCheckedColor =
            theme.colorSecondaryVariant(context)
        pointTypeSwitchButton.switchToggleCheckedColor = theme.colorSecondary(context)
        pointTypeSwitchButton.switchToggleNotCheckedColor =
            theme.colorSecondary(context)

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
        bottomSheetDialogRouteDetails.routeDetailsDeleteButton.imageTintList =
            ColorStateList.valueOf(theme.colorSecondaryVariant(context))
        bottomSheetDialogRouteDetails.routeDetailsEditButton.imageTintList =
            ColorStateList.valueOf(theme.colorSecondaryVariant(context))
        bottomSheetDialogRouteDetails.btnChangeRouteAccess.imageTintList =
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
        bottomSheetDialogPointDetails.pointDetailsDeleteButton.imageTintList =
            ColorStateList.valueOf(theme.colorSecondaryVariant(context))
        bottomSheetDialogPointDetails.pointDetailsEditButton.imageTintList =
            ColorStateList.valueOf(theme.colorSecondaryVariant(context))
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