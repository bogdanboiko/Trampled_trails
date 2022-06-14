package com.example.trampled_trails.ui.private_point.helper

import android.R
import android.content.Context
import android.content.res.ColorStateList
import androidx.core.graphics.drawable.DrawableCompat
import com.example.trampled_trails.databinding.FragmentPrivatePointsBinding
import com.example.trampled_trails.ui.themes.MyAppTheme

fun syncPrivatePointsFragmentTheme(theme: MyAppTheme, binding: FragmentPrivatePointsBinding, context: Context) {
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
            mapRoutePointModSwitcher.setImageResource(com.example.trampled_trails.R.drawable.ic_points_light)
            homepageButton.setImageResource(com.example.trampled_trails.R.drawable.ic_home_light)
            centralPointer.setImageResource(com.example.trampled_trails.R.drawable.ic_pin_point_light)
        } else {
            mapRoutePointModSwitcher.setImageResource(com.example.trampled_trails.R.drawable.ic_points_dark)
            homepageButton.setImageResource(com.example.trampled_trails.R.drawable.ic_home_dark)
            centralPointer.setImageResource(com.example.trampled_trails.R.drawable.ic_pin_point_dark)
        }

        cancelButton.backgroundTintList =
            ColorStateList.valueOf(theme.colorSecondary(context))
        createButton.backgroundTintList =
            ColorStateList.valueOf(theme.colorSecondary(context))

        DrawableCompat.wrap(getPointsList.background)
            .setTint(theme.colorOnPrimary(context))
        getPointsList.iconTint = ColorStateList.valueOf(theme.colorSecondary(context))
        getPointsList.setTextColor(theme.colorPrimaryVariant(context))

        DrawableCompat.wrap(bottomAppBar.background)
            .setTint(theme.colorPrimary(context))
        bottomNavigationView.itemIconTintList = colorStates
        bottomNavigationView.itemTextColor = colorStates

        pointDetailsBottomSheetDialogLayout.root.backgroundTintList =
            ColorStateList.valueOf(theme.colorPrimary(context))
        pointDetailsBottomSheetDialogLayout.pointDetailsEditButton.imageTintList =
            ColorStateList.valueOf(theme.colorSecondary(context))
        pointDetailsBottomSheetDialogLayout.pointDetailsDeleteButton.imageTintList =
            ColorStateList.valueOf(theme.colorSecondary(context))
        pointDetailsBottomSheetDialogLayout.pointCaptionText.setTextColor(
            theme.colorSecondaryVariant(context)
        )
        pointDetailsBottomSheetDialogLayout.pointDescriptionText.setTextColor(
            theme.colorSecondaryVariant(context)
        )
        pointDetailsBottomSheetDialogLayout.emptyDataPlaceholder.setTextColor(
            theme.colorSecondaryVariant(context)
        )

        bottomSheetDialogPoints.root.backgroundTintList =
            ColorStateList.valueOf(theme.colorPrimary(context))
        bottomSheetDialogPoints.pointFilterByTagButton.imageTintList =
            ColorStateList.valueOf(theme.colorSecondary(context))
        bottomSheetDialogPoints.emptyDataPlaceholder.setTextColor(
            theme.colorSecondaryVariant(context)
        )
    }
}