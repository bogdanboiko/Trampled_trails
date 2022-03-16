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


interface BottomSheetDialog {
    fun createRoute()
    fun deleteRoute(route: PrivateRouteModel)
    fun rebuildRoute(route: PrivateRouteModel)
}

class MainActivity :
    AppCompatActivity(),
    PermissionsListener,
    OnSwitchActivityLayoutVisibility,
    RoutesListCallback,
    RoutesListAdapterCallback {

    private lateinit var binding: ActivityMainBinding
    private val routesListAdapter = RoutesListAdapter(this as RoutesListAdapterCallback)

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var behavior: BottomSheetBehavior<LinearLayout>
    private val permissionsManager = PermissionsManager(this)
    private var routeState = MutableLiveData<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        behavior = BottomSheetBehavior.from(binding.bottomSheetDialogLayout.bottomSheetDialog)
        routeState.value = false

        configCancelButton()
        configRecycler()

        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            requestStoragePermission()
        } else {
            permissionsManager.requestLocationPermissions(this)
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
                    binding.cancelButton.visibility = View.INVISIBLE
                    behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            } else if (routeState.value == false) {
                binding.cancelButton.text = getString(R.string.txtCancelButtonExit)
                binding.cancelButton.icon = AppCompatResources.getDrawable(this, R.drawable.ic_close)

                binding.cancelButton.setOnClickListener {
                    binding.cancelButton.visibility = View.INVISIBLE
                    behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
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
            cancelButton.visibility = View.INVISIBLE
        }
    }

    override fun getRoutesList(routes: MutableList<PrivateRouteModel>) {
        routesListAdapter.currentList = routes
    }

    override fun isRoutePointExist(isExist: Boolean) {
        routeState.value = isExist
    }

    override fun onRouteItemLongPressed(route: PrivateRouteModel) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setMessage("Delete route?")
            .setPositiveButton("Yes") { dialog, _ ->
                dialogYesClick(route, dialog)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

        builder.show()
    }

    override fun onRouteItemClick(route: PrivateRouteModel) {
        (navHostFragment.childFragmentManager.fragments[0] as BottomSheetDialog)
            .rebuildRoute(route)
    }

    private fun dialogYesClick(route: PrivateRouteModel, dialog: DialogInterface) {
        (navHostFragment.childFragmentManager.fragments[0] as BottomSheetDialog)
            .deleteRoute(route)

        dialog.dismiss()
    }
}

enum class MapState {
    CREATOR,
    PRESENTATION
}