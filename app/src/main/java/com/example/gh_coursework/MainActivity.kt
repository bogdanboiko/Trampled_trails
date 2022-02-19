package com.example.gh_coursework

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.gh_coursework.databinding.ActivityMainBinding
import com.example.gh_coursework.ui.private_point.PrivatePointsFragmentDirections
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager

interface OnAddButtonPressed {
    fun switchMapMod(mapState: MapState)
    fun onAddButtonPressed()
}

class MainActivity : AppCompatActivity(), PermissionsListener {
    private val permissionsManager = PermissionsManager(this)
    private lateinit var binding: ActivityMainBinding
    private var mapState: MapState = MapState.PRESENTATION
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        //configCheckBox()
        configFabButton()
        configCancelButton()

        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            requestStoragePermission()
        } else {
            permissionsManager.requestLocationPermissions(this)
        }
    }

    private fun configCancelButton() {
        binding.cancelButton.setOnClickListener {
            mapState = MapState.PRESENTATION
            (navHostFragment.childFragmentManager.fragments[0] as OnAddButtonPressed)
                .switchMapMod(mapState)
            binding.fab.setImageDrawable(applicationContext.getDrawable(R.drawable.ic_add))
            binding.cancelButton.visibility = View.INVISIBLE
        }
    }

    private fun configFabButton() {
        binding.fab.setOnClickListener {
            if (mapState == MapState.PRESENTATION) {
                binding.fab.setImageDrawable(applicationContext.getDrawable(R.drawable.ic_confirm))
                mapState = MapState.CREATOR
                (navHostFragment.childFragmentManager.fragments[0] as OnAddButtonPressed)
                    .switchMapMod(mapState)
                binding.cancelButton.visibility = View.VISIBLE
            } else if (mapState == MapState.CREATOR) {
                (navHostFragment.childFragmentManager.fragments[0] as OnAddButtonPressed)
                    .onAddButtonPressed()
            }
        }
    }

//    private fun configCheckBox() {
//        val navController = navHostFragment.findNavController()
//
//        binding.checkboxRoutePlace.setOnCheckedChangeListener { _, b ->
//            if (b) {
//                navController.navigate(
//                    PrivatePointsFragmentDirections
//                        .actionPrivatePointsFragmentToPrivateRoutesFragment()
//                )
//            } else {
//                navController
//                    .navigate(
//                        PrivatePointsFragmentDirections
//                            .actionPrivatePointsFragmentToPrivateRoutesFragment()
//                    )
//            }
//        }
//    }

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
}

enum class MapState {
    CREATOR,
    PRESENTATION
}