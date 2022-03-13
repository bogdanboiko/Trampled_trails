package com.example.gh_coursework.ui.private_route

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.gh_coursework.MapState
import com.example.gh_coursework.OnAddButtonPressed
import com.example.gh_coursework.R
import com.example.gh_coursework.databinding.FragmentPrivateRouteBinding
import com.example.gh_coursework.databinding.ItemAnnotationViewBinding
import com.example.gh_coursework.ui.helper.convertDrawableToBitmap
import com.example.gh_coursework.ui.helper.createOnMapClickEvent
import com.example.gh_coursework.ui.private_route.mapper.mapPointToPrivateRoutePointModel
import com.example.gh_coursework.ui.private_route.mapper.mapPrivateRoutePointModelToPoint
import com.example.gh_coursework.ui.private_route.model.PrivateRouteModel
import com.example.gh_coursework.ui.private_route.model.PrivateRoutePointDetailsPreviewModel
import com.example.gh_coursework.ui.private_route.model.PrivateRoutePointModel
import com.google.gson.JsonPrimitive
import com.mapbox.api.directions.v5.DirectionsCriteria.PROFILE_WALKING
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.*
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.*
import com.mapbox.maps.plugin.gestures.OnMapClickListener
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.gestures.removeOnMapClickListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.viewannotation.ViewAnnotationManager
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import com.mapbox.navigation.base.ExperimentalPreviewMapboxNavigationAPI
import com.mapbox.navigation.base.extensions.applyDefaultNavigationOptions
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.base.route.RouterCallback
import com.mapbox.navigation.base.route.RouterFailure
import com.mapbox.navigation.base.route.RouterOrigin
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
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@OptIn(ExperimentalPreviewMapboxNavigationAPI::class)
class PrivateRoutesFragment : Fragment(R.layout.fragment_private_route), OnAddButtonPressed {

    private lateinit var binding: FragmentPrivateRouteBinding

    private val viewModel: RouteViewModel by viewModel()
    private var pointCoordinates = emptyList<PrivateRoutePointModel>()

    private lateinit var mapboxNavigation: MapboxNavigation
    private lateinit var mapboxMap: MapboxMap
    private lateinit var routeLineApi: MapboxRouteLineApi
    private lateinit var routeLineView: MapboxRouteLineView
    private var mapState: MapState = MapState.PRESENTATION
    private val navigationLocationProvider = NavigationLocationProvider()
    private val addedWaypoints = WaypointsSet()

    private lateinit var center: Pair<Float, Float>

    @OptIn(MapboxExperimental::class)
    private lateinit var viewAnnotationManager: ViewAnnotationManager
    private lateinit var pointAnnotationManager: PointAnnotationManager
    private val regularOnMapClickListener = OnMapClickListener { point ->
        addWaypoint(point, true)
        return@OnMapClickListener true
    }
    private val namedOnMapClickListener = OnMapClickListener { point ->
        addWaypoint(point, false)
        val newPoint = PrivateRoutePointModel(null, point.longitude(), point.latitude(), false)
        viewModel.addPoint(newPoint)
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
        fetchPoints()
        buildDefaultRoute()
        initMapboxNavigation()
        initRouteLine()

        view.viewTreeObserver?.addOnGlobalLayoutListener {
            center = Pair(view.width / 2f, view.height / 2f)
        }

        mapboxNavigation.startTripSession(withForegroundService = false)
        mapboxMap.loadStyleUri(Style.MAPBOX_STREETS)
    }

    /*
    Map config
     */
    @OptIn(MapboxExperimental::class)
    private fun configMap() {
        mapboxMap = binding.mapView.getMapboxMap().also {
            viewAnnotationManager = binding.mapView.viewAnnotationManager
            it.loadStyleUri(Style.MAPBOX_STREETS)
        }

        binding.mapView.location.apply {
            setLocationProvider(navigationLocationProvider)
            enabled = true
        }

        pointAnnotationManager = binding.mapView.annotations.createPointAnnotationManager()
    }

    private fun fetchPoints() {
        pointCoordinates.forEach {
            if (!it.isRoutePoint) {
                addAnnotationToMap(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.points.collect { data ->
                data.minus(pointCoordinates).forEach {
                    if (!it.isRoutePoint) {
                        addAnnotationToMap(it)
                    }
                }

                pointCoordinates = data
            }
        }
    }

    private fun buildDefaultRoute() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.routes.collect { route ->
                if (route.isNotEmpty()) {
                    if (route.last().coordinatesList.isNotEmpty()) {
                        buildRouteFromList((route.first().coordinatesList.map(::mapPrivateRoutePointModelToPoint)))
                    }
                }
            }
        }
    }

    private fun initMapboxNavigation() {
        mapboxNavigation = MapboxNavigationProvider.create(
            NavigationOptions.Builder(requireActivity().applicationContext)
                .accessToken(getString(R.string.mapbox_access_token))
                .build()
        )
    }

    private fun initRouteLine() {
        val mapboxRouteLineOptions =
            MapboxRouteLineOptions.Builder(requireActivity().applicationContext)
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
        this.mapState = mapState

        if (mapState == MapState.CREATOR) {
            binding.centralPointer.visibility = View.VISIBLE
            binding.pointTypeSwitchButton.visibility = View.VISIBLE

            swapOnMapClickListener(binding.pointTypeSwitchButton.isChecked)

            binding.pointTypeSwitchButton.addSwitchObserver { _, isChecked ->
                swapOnMapClickListener(isChecked)
            }
        } else if (mapState == MapState.PRESENTATION) {
            if (addedWaypoints.getCoordinatesList().isNotEmpty()) {
                viewModel.addRoute(
                    PrivateRouteModel(
                        null,
                        addedWaypoints.getCoordinatesList().map(::mapPointToPrivateRoutePointModel)
                    )
                )
                addedWaypoints.clear()
            }

            binding.centralPointer.visibility = View.INVISIBLE
            binding.undoPointCreatingButton.visibility = View.INVISIBLE
            binding.resetRouteButton.visibility = View.INVISIBLE
            binding.pointTypeSwitchButton.visibility = View.INVISIBLE

            mapboxMap.removeOnMapClickListener(regularOnMapClickListener)
            mapboxMap.removeOnMapClickListener(namedOnMapClickListener)
        }
    }

    private fun swapOnMapClickListener(isChecked: Boolean) {
        if (isChecked) {
            mapboxMap.removeOnMapClickListener(namedOnMapClickListener)
            mapboxMap.addOnMapClickListener(regularOnMapClickListener)

            binding.centralPointer.setImageResource(R.drawable.ic_pin_route)
        } else {
            mapboxMap.removeOnMapClickListener(regularOnMapClickListener)
            mapboxMap.addOnMapClickListener(namedOnMapClickListener)

            binding.centralPointer.setImageResource(R.drawable.ic_pin_point)
        }
    }

    private fun buildRoute() {
        mapboxNavigation.requestRoutes(
            RouteOptions.builder()
                .applyDefaultNavigationOptions()
                .profile(PROFILE_WALKING)
                .coordinatesList(addedWaypoints.getCoordinatesList())
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

    private fun buildRouteFromList(coordinatesList: List<Point>) {
        mapboxNavigation.requestRoutes(
            RouteOptions.builder()
                .applyDefaultNavigationOptions()
                .profile(PROFILE_WALKING)
                .coordinatesList(coordinatesList)
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

    /*
    Creating route point on screen
     */
    override fun onAddButtonPressed() {
        executeClickAtPoint()
    }

    private fun executeClickAtPoint() {
        val clickEvent = createOnMapClickEvent(center)
        binding.mapView.dispatchTouchEvent(clickEvent.first)
        binding.mapView.dispatchTouchEvent(clickEvent.second)
    }

    /*
    Adding point to points list, route build and set up
     */
    private fun addWaypoint(point: Point, isChecked: Boolean) {
        if (isChecked) {
            addedWaypoints.addRegular(point)
        } else {
            addedWaypoints.addNamed(point, "Name", "Description")
        }

        buildRoute()
    }

    fun setRoute(routes: List<DirectionsRoute>) {
        mapboxNavigation.setRoutes(routes)

        if (mapState == MapState.CREATOR) {
            binding.resetRouteButton.apply {
                show()
                setOnClickListener {
                    resetCurrentRoute(mapboxNavigation)
                    hide()
                }
            }

            binding.undoPointCreatingButton.apply {
                show()
                setOnClickListener {
                    undoPointCreating()

                    if (routes.isEmpty()) {
                        hide()
                    }
                }
            }
        }
    }

    /*
    Reset route or undo point creation
     */
    private fun resetCurrentRoute(mapboxNavigation: MapboxNavigation) {
        if (mapboxNavigation.getRoutes().isNotEmpty()) {
            mapboxNavigation.setRoutes(emptyList())
            addedWaypoints.clear()
        }

        binding.undoPointCreatingButton.visibility = View.INVISIBLE
        binding.resetRouteButton.visibility = View.INVISIBLE
    }

    private fun undoPointCreating() {
        if (addedWaypoints.getCoordinatesList().size > 2) {
            addedWaypoints.undoLastPointCreation()
            buildRoute()
        } else if (addedWaypoints.getCoordinatesList().size == 2) {
            resetCurrentRoute(mapboxNavigation)
        }
    }

    private fun addAnnotationToMap(point: PrivateRoutePointModel) {
        activity?.applicationContext?.let {
            bitmapFromDrawableRes(it, R.drawable.ic_pin_point)?.let { image ->
                pointAnnotationManager.create(
                    createAnnotationPoint(
                        image,
                        Point.fromLngLat(point.x, point.y)
                    ).withData(JsonPrimitive(point.pointId))
                )

                pointAnnotationManager.addClickListener(OnPointAnnotationClickListener { annotation ->
                    viewLifecycleOwner.lifecycleScope.launch {
                        annotation.getData()?.asInt?.let { pointId ->
                            viewModel.getPointDetailsPreview(pointId).collect { details ->
                                prepareViewAnnotation(annotation, details)
                            }
                        }
                    }

                    true
                })
            }
        }
    }

    @OptIn(MapboxExperimental::class)
    private fun prepareViewAnnotation(
        pointAnnotation: PointAnnotation,
        details: PrivateRoutePointDetailsPreviewModel?
    ) {
        val viewAnnotation =
            viewAnnotationManager.getViewAnnotationByFeatureId(pointAnnotation.featureIdentifier)
                ?: viewAnnotationManager.addViewAnnotation(
                    resId = R.layout.item_annotation_view,
                    options = viewAnnotationOptions {
                        geometry(pointAnnotation.geometry)
                        anchor(ViewAnnotationAnchor.BOTTOM)
                        associatedFeatureId(pointAnnotation.featureIdentifier)
                        offsetY(pointAnnotation.iconImageBitmap?.height)
                    }
                )

        ItemAnnotationViewBinding.bind(viewAnnotation).apply {
            pointCaptionText.text = details?.caption
            previewDescriptionText.text = details?.description

            viewDetailsButton.setOnClickListener {
                findNavController().navigate(
                    PrivateRoutesFragmentDirections
                        .actionPrivateRoutesFragmentToPointDetailsFragment(0)
                )
            }

            closeNativeView.setOnClickListener {
                viewAnnotationManager.removeViewAnnotation(viewAnnotation)
            }

            deleteButton.setOnClickListener {
                pointAnnotation.getData()?.asInt?.let { pointId ->
                    viewModel.deletePoint(
                        pointId
                    )
                }
                viewAnnotationManager.removeViewAnnotation(viewAnnotation)
                pointAnnotationManager.delete(pointAnnotation)
            }
        }
    }

    private fun createAnnotationPoint(bitmap: Bitmap, point: Point): PointAnnotationOptions {
        return PointAnnotationOptions()
            .withPoint(point)
            .withIconImage(bitmap)
            .withIconAnchor(IconAnchor.BOTTOM)
    }

    private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
        convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))
}