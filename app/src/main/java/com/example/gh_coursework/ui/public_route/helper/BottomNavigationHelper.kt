package com.example.gh_coursework.ui.public_route.helper

import android.view.View
import androidx.navigation.Navigation.findNavController
import com.example.gh_coursework.databinding.FragmentPublicRouteBinding
import com.example.gh_coursework.ui.public_route.PublicRoutesFragmentArgs
import com.example.gh_coursework.ui.public_route.PublicRoutesFragmentDirections

fun configPublicFragmentBottomNavBar(
    arguments: PublicRoutesFragmentArgs,
    binding: FragmentPublicRouteBinding,
    view: View
) {

    binding.bottomNavigationView.menu.getItem(0).isChecked = true
    binding.bottomNavigationView.menu.getItem(2).setOnMenuItemClickListener {
        if (arguments.popUpTo == "point") {
            findNavController(view).navigate(
                PublicRoutesFragmentDirections.actionPublicRouteFragmentToPrivatePointFragment()
            )
        } else if (arguments.popUpTo == "route") {
            findNavController(view).navigate(
                PublicRoutesFragmentDirections.actionPublicRouteFragmentToPrivateRouteFragment()
            )
        }

        return@setOnMenuItemClickListener true
    }
}