package com.example.gh_coursework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            if (b) {
                binding.navHostFragmentActivityMain.findNavController()
                    .navigate(PrivatePointsFragmentDirections
                        .actionPrivatePointsFragmentToPrivateRoutesFragment())
            } else {
                binding.navHostFragmentActivityMain.findNavController()
                    .navigate(PrivateRoutesFragmentDirections
                        .actionPrivateRoutesFragmentToPrivatePointsFragment())
            }
        }
    }
}