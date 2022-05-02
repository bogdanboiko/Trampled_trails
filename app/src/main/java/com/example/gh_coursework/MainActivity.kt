package com.example.gh_coursework

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeActivity
import com.example.gh_coursework.databinding.ActivityMainBinding
import com.example.gh_coursework.ui.helper.GetUserIdCallback
import com.example.gh_coursework.ui.helper.InternetCheckCallback
import com.example.gh_coursework.ui.homepage.LoginCallback
import com.example.gh_coursework.ui.themes.DarkTheme
import com.example.gh_coursework.ui.themes.LightTheme
import com.google.firebase.auth.FirebaseAuth
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity :
    ThemeActivity(),
    PermissionsListener,
    LoginCallback,
    InternetCheckCallback,
    GetUserIdCallback {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ActivityViewModel by viewModel()
    private val permissionsManager = PermissionsManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            requestStoragePermission()
        } else {
            permissionsManager.requestLocationPermissions(this)
        }

        syncData()
    }

    override fun onPause() {
        uploadData()
        super.onPause()
    }

    override fun onSuccessLogin() {
        syncData()
    }

    override fun onSuccessLogOut() {
        lifecycleScope.launch {
            syncData()
        }.invokeOnCompletion {
            viewModel.deleteAll()
        }
    }

    private fun syncData() {
        if (isInternetAvailable()) {
            viewModel.syncDataWithFirebase(getUserId())
        }
    }

    private fun uploadData() {
        if (isInternetAvailable()) {
            viewModel.uploadDataToFirebase(getUserId())
        }
    }

    @SuppressLint("HardwareIds")
    override fun getUserId(): String {
        return if (FirebaseAuth.getInstance().currentUser == null) {
            Settings.Secure.getString(this.contentResolver,
                Settings.Secure.ANDROID_ID)
        } else {
            FirebaseAuth.getInstance().currentUser?.uid.toString()
        }
    }

    override fun isInternetAvailable(): Boolean {
        val connectivityManager: ConnectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager.run {
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
                    return when {
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> true
                        else -> false
                    }
                }
            }
        } else {
            val networkConnection = connectivityManager.activeNetworkInfo ?: return false
            return networkConnection.isConnected &&
                    (networkConnection.type == ConnectivityManager.TYPE_WIFI
                            || networkConnection.type == ConnectivityManager.TYPE_MOBILE)
        }

        return false
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

    override fun getStartTheme(): AppTheme {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        return if (sharedPref.getBoolean("theme_switch", false)) {
            LightTheme()
        } else {
            DarkTheme()
        }
    }

    override fun syncTheme(appTheme: AppTheme) {
        // useless
    }
}

enum class MapState {
    CREATOR,
    PRESENTATION
}