package com.example.gh_coursework

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.gh_coursework.databinding.ActivityMainBinding
import com.example.gh_coursework.ui.point_details.OnSwitchActivityLayoutVisibility
import com.example.gh_coursework.ui.private_point.PrivatePointsFragmentDirections
import com.example.gh_coursework.ui.private_route.PrivateRoutesFragmentDirections
import com.example.gh_coursework.ui.private_route.RoutesListAdapter
import com.example.gh_coursework.ui.private_route.RoutesListAdapterCallback
import com.example.gh_coursework.ui.private_route.RoutesListCallback
import com.example.gh_coursework.ui.private_route.model.PrivateRouteModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager

interface OnAddButtonPressed {
    fun switchMapMod(mapState: MapState)
    fun onAddButtonPressed()
}

interface BottomSheetDialog {
    fun createRoute()
    fun deleteRoute(routeId: Int)
}

class MainActivity :
    AppCompatActivity(),
    PermissionsListener,
    OnSwitchActivityLayoutVisibility,
    RoutesListCallback,
    RoutesListAdapterCallback {

    private lateinit var binding: ActivityMainBinding
    private val routesListAdapter = RoutesListAdapter(this as RoutesListAdapterCallback)

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var behavior: BottomSheetBehavior<LinearLayout>
    private val permissionsManager = PermissionsManager(this)
    private var mapState = MutableLiveData(MapState.PRESENTATION)
    private var routeState = MutableLiveData<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        behavior = BottomSheetBehavior.from(binding.bottomSheetDialogLayout.bottomSheetDialog)
        routeState.value = false

        configNavigation()
        configMapStateObserver()
        configMapTypeSwitcherButton()
        configCancelButton()
        configFabButton()
        configRecycler()

        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            requestStoragePermission()
        } else {
            permissionsManager.requestLocationPermissions(this)
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

    private fun configMapStateObserver() {
        mapState.observe(this) {
            if (it == MapState.PRESENTATION) {
                binding.bottomSheetDialogLayout.fab.setImageDrawable(
                    applicationContext.getDrawable(
                        R.drawable.ic_add
                    )
                )
                binding.cancelButton.visibility = View.INVISIBLE
            } else if (it == MapState.CREATOR) {
                binding.bottomSheetDialogLayout.fab.setImageDrawable(
                    applicationContext.getDrawable(
                        R.drawable.ic_confirm
                    )
                )
            }

            if (navHostFragment.childFragmentManager.fragments[0] is OnAddButtonPressed) {
                (navHostFragment.childFragmentManager.fragments[0] as OnAddButtonPressed)
                    .switchMapMod(it)
            }

            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
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
        routeState.observe(this) {
            if (routeState.value == true) {
                binding.cancelButton.text = getString(R.string.txtCancelButtonSave)
                binding.cancelButton.icon = AppCompatResources.getDrawable(this, R.drawable.ic_confirm)

                binding.cancelButton.setOnClickListener {
                    (navHostFragment.childFragmentManager.fragments[0] as BottomSheetDialog)
                        .createRoute()
                    mapState.value = MapState.PRESENTATION
                    binding.cancelButton.visibility = View.INVISIBLE
                    behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            } else if (routeState.value == false) {
                binding.cancelButton.text = getString(R.string.txtCancelButtonExit)
                binding.cancelButton.icon = AppCompatResources.getDrawable(this, R.drawable.ic_close)

                binding.cancelButton.setOnClickListener {
                    mapState.value = MapState.PRESENTATION
                    binding.cancelButton.visibility = View.INVISIBLE
                    behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        }
    }

    private fun configFabButton() {
        binding.bottomSheetDialogLayout.fab.setOnClickListener {
            if (mapState.value == MapState.PRESENTATION) {
                mapState.value = MapState.CREATOR
                binding.cancelButton.visibility = View.VISIBLE
            } else if (mapState.value == MapState.CREATOR) {
                (navHostFragment.childFragmentManager.fragments[0] as OnAddButtonPressed)
                    .onAddButtonPressed()
            }
        }
    }

    private fun configRecycler() {
        binding.bottomSheetDialogLayout.routesRecyclerView.apply {
            adapter = routesListAdapter
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
            binding.bottomSheetDialogLayout.bottomAppBar.visibility = state
            binding.bottomSheetDialogLayout.fab.visibility = state
            mapRoutePointModSwitcher.visibility = state
            cancelButton.visibility = View.INVISIBLE
        }
    }

    override fun getRoutesList(routes: MutableList<PrivateRouteModel>) {
        routesListAdapter.currentList = routes
    }

    override fun isRoutePointExist(isExist: Boolean) {
        routeState.value = isExist
    }

    override fun onRouteItemLongPressed(routeId: Int) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setMessage("Delete route?")
            .setPositiveButton("Yes") { dialog, _ ->
                dialogYesClick(routeId, dialog)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

        builder.show()
    }

    private fun dialogYesClick(routeId: Int, dialog: DialogInterface) {
        (navHostFragment.childFragmentManager.fragments[0] as BottomSheetDialog)
            .deleteRoute(routeId)

        dialog.dismiss()
    }
}

enum class MapState {
    CREATOR,
    PRESENTATION
}