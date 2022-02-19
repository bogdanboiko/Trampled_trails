package com.example.gh_coursework.ui.private_route

import com.example.gh_coursework.databinding.FragmentPrivateRouteBinding
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.geojson.Point
import com.mapbox.navigation.base.extensions.applyDefaultNavigationOptions
import com.mapbox.navigation.base.route.RouterCallback
import com.mapbox.navigation.base.route.RouterFailure
import com.mapbox.navigation.base.route.RouterOrigin
import com.mapbox.navigation.core.MapboxNavigation

class RoutesHelper(
    private val addedWaypoints: WaypointsSet,
    private val mapboxNavigation: MapboxNavigation,
    private val binding: FragmentPrivateRouteBinding
) {

    fun addWaypoint(destination: Point) {
        addedWaypoints.addRegular(destination)

        mapboxNavigation.requestRoutes(
            RouteOptions.builder()
                .applyDefaultNavigationOptions()
                .coordinatesList(addedWaypoints.coordinatesList())
                .build(),
            object : RouterCallback {
                override fun onRoutesReady(
                    routes: List<DirectionsRoute>,
                    routerOrigin: RouterOrigin
                ) {
                    setRoute(routes)
                }

                override fun onFailure(
                    reasons: List<RouterFailure>,
                    routeOptions: RouteOptions
                ) {
                    // no impl
                }

                override fun onCanceled(routeOptions: RouteOptions, routerOrigin: RouterOrigin) {
                    // no impl
                }
            }
        )
    }

    fun setRoute(routes: List<DirectionsRoute>) {
        // set routes, where the first route in the list is the primary route that
        // will be used for active guidance
        mapboxNavigation.setRoutes(routes)

        // show the "Reset the route" button
        binding.multipleWaypointResetRouteButton.apply {
            show()
            setOnClickListener {
                resetCurrentRoute(mapboxNavigation)
                hide()
            }
        }
    }

    // Resets the current route
    private fun resetCurrentRoute(mapboxNavigation: MapboxNavigation) {
        if (mapboxNavigation.getRoutes().isNotEmpty()) {
            mapboxNavigation.setRoutes(emptyList()) // reset route
            addedWaypoints.clear() // reset stored waypoints
        }
    }
}