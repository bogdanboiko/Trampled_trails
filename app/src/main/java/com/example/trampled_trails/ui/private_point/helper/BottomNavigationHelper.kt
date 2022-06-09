package com.example.trampled_trails.ui.private_point.helper

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import com.example.trampled_trails.R
import com.example.trampled_trails.databinding.FragmentPrivatePointsBinding
import com.example.trampled_trails.ui.private_point.PrivatePointsFragmentDirections

fun configPrivatePointsFragmentBottomNavBar(
    isInternetAvailable: Boolean,
    binding: FragmentPrivatePointsBinding,
    context: Context,
    view: View
) {

    binding.bottomNavigationView.menu.getItem(2).isChecked = true
    binding.bottomNavigationView.menu.getItem(0).setOnMenuItemClickListener {
        if (isInternetAvailable) {
            findNavController(view).navigate(
                PrivatePointsFragmentDirections.actionPrivatePointsFragmentToPublicRoutesFragment(
                    "point"
                )
            )
        } else {
            Toast.makeText(
                context,
                R.string.no_internet_connection,
                Toast.LENGTH_SHORT
            )
                .show()
        }

        return@setOnMenuItemClickListener true
    }
}