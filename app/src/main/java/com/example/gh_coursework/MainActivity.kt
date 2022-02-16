package com.example.gh_coursework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import com.example.gh_coursework.databinding.ActivityMainBinding
import com.example.gh_coursework.ui.private_point.PrivatePointsFragmentDirections
import com.example.gh_coursework.ui.private_route.PrivateRoutesFragmentDirections

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configCheckBox()
    }

    private fun configCheckBox() {
        binding.checkboxRoutePlace.setOnCheckedChangeListener { _, b ->
            val navController = binding.navHostFragmentActivityMain.findNavController()

            if (b) {
                binding.navHostFragmentActivityMain.findNavController()
                    .navigate(
                        PrivatePointsFragmentDirections
                            .actionPrivatePointsFragmentToPrivateRoutesFragment()
                    )
            } else {
                navController.popBackStack()
            }
        }
    }

    override fun onBackPressed() {
        with(binding) {
            if (!checkboxRoutePlace.isChecked) {
                finishAffinity()
            } else {
                checkboxRoutePlace.isChecked = !checkboxRoutePlace.isChecked
            }
        }
    }
}