package com.example.gh_coursework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.gh_coursework.databinding.ActivityMainBinding
import com.example.gh_coursework.ui.private_point.PrivatePointsFragmentDirections

interface OnAddButtonPressed {
    fun switchMapMod(mapState: MapState)
    fun onAddButtonPressed()
}

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mapState: MapState = MapState.PRESENTATION
    private var navHostFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main)
        configCheckBox()
        configFabButton()
    }

    private fun configFabButton() {
        binding.fab.setOnClickListener {
            if (mapState == MapState.PRESENTATION) {
                binding.fab.setImageDrawable(applicationContext.getDrawable(R.drawable.ic_confirm))
                mapState = MapState.CREATOR
                (navHostFragment?.childFragmentManager?.fragments?.get(0) as OnAddButtonPressed)
                    .switchMapMod(mapState)
            } else if (mapState == MapState.CREATOR) {
                (navHostFragment?.childFragmentManager?.fragments?.get(0) as OnAddButtonPressed)
                    .onAddButtonPressed()
            }
        }
    }

    private fun configCheckBox() {
        val navController = binding.navHostFragmentActivityMain.findNavController()

        binding.checkboxRoutePlace.setOnCheckedChangeListener { _, b ->
            if (b) {
                navController.navigate(PrivatePointsFragmentDirections
                            .actionPrivatePointsFragmentToPrivateRoutesFragment())
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

enum class MapState {
    CREATOR,
    PRESENTATION
}