package com.example.gh_coursework.ui.private_route

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gh_coursework.MapState
import com.example.gh_coursework.OnAddButtonPressed
import com.example.gh_coursework.R
import com.example.gh_coursework.databinding.FragmentPrivateRouteBinding
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.gestures.OnMapClickListener
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.gestures.removeOnMapClickListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.navigation.base.ExperimentalPreviewMapboxNavigationAPI
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.MapboxNavigationProvider
import com.mapbox.navigation.core.directions.session.RoutesObserver
import com.mapbox.navigation.core.trip.session.LocationMatcherResult
import com.mapbox.navigation.core.trip.session.LocationObserver
import com.mapbox.navigation.ui.maps.location.NavigationLocationProvider
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineApi
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineView
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineOptions
import com.mapbox.navigation.ui.maps.route.line.model.RouteLine

@OptIn(ExperimentalPreviewMapboxNavigationAPI::class)
class PrivateRoutesFragment : Fragment(R.layout.fragment_private_route), OnAddButtonPressed {

    private lateinit var binding: FragmentPrivateRouteBinding

    private lateinit var mapboxNavigation: MapboxNavigation
    private lateinit var mapboxMap: MapboxMap
    private lateinit var routeLineApi: MapboxRouteLineApi
    private lateinit var routeLineView: MapboxRouteLineView
    private lateinit var routeHelper: RoutesHelper
    private val navigationLocationProvider = NavigationLocationProvider()
    private val addedWaypoints = WaypointsSet()

    private lateinit var center: Pair<Float, Float>
    private val onMapClickListener = OnMapClickListener { point ->
        routeHelper.addWaypoint(point)
        return@OnMapClickListener true
    }

    private val routesObserver = RoutesObserver { routeUpdateResult ->
        if (routeUpdateResult.routes.isNotEmpty()) {
            // generate route geometries and render them
            val routeLines = routeUpdateResult.routes.map { RouteLine(it, null) }
            routeLineApi.setRoutes(
                routeLines
            ) { value ->
                mapboxMap.getStyle()?.apply {
                    routeLineView.renderRouteDrawData(this, value)
                }
            }
        } else {
            // remove the route line and route arrow from the map
            val style = mapboxMap.getStyle()
            if (style != null) {
                routeLineApi.clearRouteLine { value ->
                    routeLineView.renderClearRouteLineValue(
                        style,
                        value
                    )
                }
            }
        }
    }

    private val locationObserver = object : LocationObserver {
        var firstLocationUpdateReceived = false

        override fun onNewRawLocation(rawLocation: Location) {
            // not handled
        }

        override fun onNewLocationMatcherResult(locationMatcherResult: LocationMatcherResult) {
            val enhancedLocation = locationMatcherResult.enhancedLocation
            navigationLocationProvider.changePosition(
                location = enhancedLocation,
                keyPoints = locationMatcherResult.keyPoints,
            )

            if (!firstLocationUpdateReceived) {
                firstLocationUpdateReceived = true
                moveCameraTo(enhancedLocation)
            }
        }

        private fun moveCameraTo(location: Location) {
            val mapAnimationOptions = MapAnimationOptions.Builder().duration(0).build()
            binding.mapView.camera.easeTo(
                CameraOptions.Builder()
                    .center(Point.fromLngLat(location.longitude, location.latitude))
                    .zoom(16.0)
                    .build(),
                mapAnimationOptions
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPrivateRouteBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configMap()
        initMapboxNavigation()
        initRouteLine()

        routeHelper = RoutesHelper(addedWaypoints, mapboxNavigation, binding)

        view.viewTreeObserver?.addOnGlobalLayoutListener {
            center = Pair(view.width / 2f, view.height / 2f)
        }

        mapboxNavigation.startTripSession(withForegroundService = false)
        mapboxMap.loadStyleUri(Style.MAPBOX_STREETS)
    }

    private fun configMap() {
        mapboxMap = binding.mapView.getMapboxMap()
        binding.mapView.location.apply {
            setLocationProvider(navigationLocationProvider)
            enabled = true
        }
    }

    private fun initMapboxNavigation() {
        mapboxNavigation = MapboxNavigationProvider.create(
            NavigationOptions.Builder(activity!!.applicationContext)
                .accessToken(getString(R.string.mapbox_access_token))
                .build()
        )
    }

    private fun initRouteLine() {
        val mapboxRouteLineOptions =
            MapboxRouteLineOptions.Builder(activity!!.applicationContext)
                .withRouteLineBelowLayerId("road-label")
                .build()
        routeLineApi = MapboxRouteLineApi(mapboxRouteLineOptions)
        routeLineView = MapboxRouteLineView(mapboxRouteLineOptions)
    }

    override fun onStart() {
        super.onStart()
        mapboxNavigation.registerRoutesObserver(routesObserver)
        mapboxNavigation.registerLocationObserver(locationObserver)
    }

    override fun onStop() {
        super.onStop()
        mapboxNavigation.unregisterRoutesObserver(routesObserver)
        mapboxNavigation.unregisterLocationObserver(locationObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        mapboxNavigation.onDestroy()
    }

    override fun switchMapMod(mapState: MapState) {
        if (mapState == MapState.CREATOR) {
            binding.centralPointer.visibility = View.VISIBLE
            mapboxMap.addOnMapClickListener(onMapClickListener)
        } else {
            binding.centralPointer.visibility = View.INVISIBLE
            mapboxMap.removeOnMapClickListener(onMapClickListener)
        }
    }

    override fun onAddButtonPressed() {
        executeClickAtPoint()
    }

    private fun executeClickAtPoint() {
        val downTime = SystemClock.uptimeMillis()
        val eventTime = SystemClock.uptimeMillis() + 10
        val downAction = MotionEvent.obtain(
            downTime, eventTime, MotionEvent.ACTION_DOWN,
            center.first, center.second, 0
        )
        val upAction = MotionEvent.obtain(
            downTime, eventTime, MotionEvent.ACTION_UP,
            center.first, center.second, 0
        )
        binding.mapView.dispatchTouchEvent(downAction)
        binding.mapView.dispatchTouchEvent(upAction)
    }
}
