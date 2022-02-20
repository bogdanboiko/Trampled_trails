package com.example.gh_coursework

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.gh_coursework.databinding.ActivityMainBinding
import com.example.gh_coursework.ui.point_details.OnSwitchActivityLayoutVisibility
import com.example.gh_coursework.ui.private_point.PrivatePointsFragmentDirections
import com.example.gh_coursework.ui.private_route.PrivateRoutesFragmentDirections
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import kotlin.collections.ArrayList

interface OnAddButtonPressed {
    fun switchMapMod(mapState: MapState)
    fun onAddButtonPressed()
}

class MainActivity : AppCompatActivity(), PermissionsListener, OnSwitchActivityLayoutVisibility {
    private lateinit var navController: NavController
    private val permissionsManager = PermissionsManager(this)
    private lateinit var binding: ActivityMainBinding
    private var mapState = MutableLiveData(MapState.PRESENTATION)
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        configNavigation()
        configFabButton()
        configCancelButton()
        configMapStateObserver()

        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            requestStoragePermission()
        } else {
            permissionsManager.requestLocationPermissions(this)
        }
    }

    private fun configMapStateObserver() {
        mapState.observe(this) {
            if (it == MapState.PRESENTATION) {
                binding.fab.setImageDrawable(applicationContext.getDrawable(R.drawable.ic_add))
                binding.cancelButton.visibility = View.INVISIBLE
            } else if (it == MapState.CREATOR) {
                binding.fab.setImageDrawable(applicationContext.getDrawable(R.drawable.ic_confirm))
            }

            if (navHostFragment.childFragmentManager.fragments[0] is OnAddButtonPressed) {
                (navHostFragment.childFragmentManager.fragments[0] as OnAddButtonPressed)
                    .switchMapMod(it)
            }
        }
    }

    private fun configMapTypeSwitcherButton() {
        if (navController.currentDestination?.id == R.id.privatePointsFragment) {
            binding.mapRoutePointModSwitcher.background =
                applicationContext.getDrawable(R.drawable.ic_points)
        } else if (navController.currentDestination?.id == R.id.privateRoutesFragment) {
            binding.mapRoutePointModSwitcher.background =
                applicationContext.getDrawable(R.drawable.ic_routes)
        }
    }

    private fun configCancelButton() {
        binding.cancelButton.setOnClickListener {
            mapState.value = MapState.PRESENTATION
            binding.cancelButton.visibility = View.INVISIBLE
        }
    }

    private fun configFabButton() {
        binding.fab.setOnClickListener {
            if (mapState.value == MapState.PRESENTATION) {
                mapState.value = MapState.CREATOR
                binding.cancelButton.visibility = View.VISIBLE
            } else if (mapState.value == MapState.CREATOR) {
                (navHostFragment.childFragmentManager.fragments[0] as OnAddButtonPressed)
                    .onAddButtonPressed()
            }
        }
    }

    private fun configNavigation() {
        navController = navHostFragment.findNavController()

        binding.mapRoutePointModSwitcher.setOnClickListener {

            if (navController.currentDestination?.id == R.id.privatePointsFragment) {
                if (!navController.popBackStack(R.id.privateRoutesFragment, false)) {
                    navController.navigate(
                        PrivatePointsFragmentDirections
                            .actionPrivatePointsFragmentToPrivateRoutesFragment()
                    )
                }

            } else if (navController.currentDestination?.id == R.id.privateRoutesFragment) {
                if (!navController.popBackStack(R.id.privatePointsFragment, false)) {
                    navController
                        .navigate(
                            PrivateRoutesFragmentDirections
                                .actionPrivateRoutesFragmentToPrivatePointsFragment()
                        )
                }
            }

            configMapTypeSwitcherButton()
            mapState.value = MapState.PRESENTATION
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        configMapTypeSwitcherButton()
        mapState.value = MapState.PRESENTATION
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        Toast.makeText(
            this,
            "This app needs location and storage permissions in order to show its functionality.",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            requestStoragePermission()
        } else {
            Toast.makeText(
                this,
                "You didn't grant the permissions required to use the app",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun requestStoragePermission() {
        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val permissionsNeeded: MutableList<String> = ArrayList()
        if (
            ContextCompat.checkSelfPermission(this, permission) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            permissionsNeeded.add(permission)
            ActivityCompat.requestPermissions(
                this,
                permissionsNeeded.toTypedArray(),
                10
            )
        }
    }

    override fun switchActivityLayoutState(state: Int) {
        with(binding) {
            bottomAppBar.visibility = state
            fab.visibility = state
            mapRoutePointModSwitcher.visibility = state
        }
    }
}

enum class MapState {
    CREATOR,
    PRESENTATION
}