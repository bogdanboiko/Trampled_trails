package com.example.gh_coursework.ui.private_route

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.gh_coursework.MapState
import com.example.gh_coursework.R
import com.example.gh_coursework.databinding.FragmentPrivateRouteBinding
import com.example.gh_coursework.ui.helper.convertDrawableToBitmap
import com.example.gh_coursework.ui.helper.createOnMapClickEvent
import com.example.gh_coursework.ui.private_point.PrivatePointsFragmentDirections
import com.example.gh_coursework.ui.private_route.adapter.RoutePointsListAdapter
import com.example.gh_coursework.ui.private_route.adapter.RoutePointsListCallback
import com.example.gh_coursework.ui.private_route.adapter.RoutesListAdapter
import com.example.gh_coursework.ui.private_route.adapter.RoutesListAdapterCallback
import com.example.gh_coursework.ui.private_route.mapper.mapPrivateRoutePointModelToPoint
import com.example.gh_coursework.ui.private_route.model.PrivateRouteModel
import com.example.gh_coursework.ui.private_route.model.PrivateRoutePointDetailsPreviewModel
import com.example.gh_coursework.ui.private_route.model.PrivateRoutePointModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.JsonPrimitive
import com.mapbox.api.directions.v5.DirectionsCriteria.PROFILE_WALKING
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
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
class PrivateRoutesFragment :
    Fragment(R.layout.fragment_private_route),
    RoutesListAdapterCallback,
    RoutePointsListCallback {

    private lateinit var binding: FragmentPrivateRouteBinding

    private val viewModel: RouteViewModel by viewModel()
    private val routesListAdapter = RoutesListAdapter(this as RoutesListAdapterCallback)
    private val pointsListAdapter = RoutePointsListAdapter(this as RoutePointsListCallback)

    private val currentRouteCoordinatesList = mutableListOf<PrivateRoutePointModel>()
    private lateinit var focusedRoute: PrivateRouteModel

    private lateinit var routesDialogBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var routePointsDialogBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var routeDetailsDialogBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var pointDetailsDialogBehavior: BottomSheetBehavior<ConstraintLayout>

    private lateinit var mapboxNavigation: MapboxNavigation
    private lateinit var mapboxMap: MapboxMap
    private lateinit var routeLineApi: MapboxRouteLineApi
    private lateinit var routeLineView: MapboxRouteLineView
    private var mapState = MutableLiveData(MapState.PRESENTATION)
    private var routeState = MutableLiveData<Boolean>()
    private val navigationLocationProvider = NavigationLocationProvider()

    private lateinit var center: Pair<Float, Float>

    @OptIn(MapboxExperimental::class)
    private lateinit var viewAnnotationManager: ViewAnnotationManager
    private lateinit var pointAnnotationManager: PointAnnotationManager

    private val regularOnMapClickListener = OnMapClickListener { point ->
        val newPoint = PrivateRoutePointModel(null, point.longitude(), point.latitude(), true)
        addWaypoint(newPoint)
        return@OnMapClickListener true
    }

    private val namedOnMapClickListener = OnMapClickListener { point ->
        val result = pointAnnotationManager.annotations.find {
            return@find it.point.latitude() == point.latitude()
                    && it.point.longitude() == point.longitude()
        }

        if (result == null) {
            val newPoint = PrivateRoutePointModel(null, point.longitude(), point.latitude(), false)
            addEmptyAnnotationToMap(newPoint)
            addWaypoint(newPoint)
        }

        return@OnMapClickListener true
    }

    private val onPointClickEvent = OnPointAnnotationClickListener { annotation ->

        if (annotation.getData()?.isJsonNull == false) {
            getPointDetailsDialog(annotation)
        } else if (annotation.getData()?.isJsonNull == true) {
            getRouteDetailsDialog()
        }

        true
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
                    .zoom(15.0)
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

        routeState.value = false

        configMap()
        configMapStateSwitcher()
        configMapSwitcherButton()
        configCancelButton()
        configRecyclers()
        configBottomSheetDialogs()
        initMapboxNavigation()
        initRouteLine()
        buildDefaultRoute()

        view.viewTreeObserver?.addOnGlobalLayoutListener {
            center = Pair(view.width / 2f, view.height / 2f)
        }

        mapboxNavigation.startTripSession(withForegroundService = false)
        mapboxMap.loadStyleUri(Style.MAPBOX_STREETS)
    }

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
        pointAnnotationManager.addClickListener(onPointClickEvent)
    }

    private fun configMapStateSwitcher() {
        binding.fab.setOnClickListener {
            if (mapState.value == MapState.CREATOR) {
                executeClickAtPoint()
            } else {
                mapState.value = MapState.CREATOR
                switchMapMod()
            }
        }
    }

    private fun executeClickAtPoint() {
        val clickEvent = createOnMapClickEvent(center)
        binding.mapView.dispatchTouchEvent(clickEvent.first)
        binding.mapView.dispatchTouchEvent(clickEvent.second)
    }

    private fun configMapSwitcherButton() {
        binding.mapRoutePointModSwitcher.setOnClickListener {
            findNavController().navigate(
                PrivateRoutesFragmentDirections
                    .actionPrivateRoutesFragmentToPrivatePointsFragment()
            )
        }
    }

    private fun configCancelButton() {
        routeState.observe(viewLifecycleOwner) {
            if (routeState.value == true) {
                binding.cancelButton.text = getString(R.string.txtCancelButtonSave)
                binding.cancelButton.icon =
                    view?.context?.let { it1 ->
                        AppCompatResources.getDrawable(
                            it1,
                            R.drawable.ic_confirm
                        )
                    }

                binding.cancelButton.setOnClickListener {
                    createRoute()
                    routeState.value = false
                    mapState.value = MapState.PRESENTATION
                    binding.cancelButton.visibility = View.INVISIBLE
                    routesDialogBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            } else if (routeState.value == false) {
                binding.cancelButton.text = getString(R.string.txtCancelButtonExit)
                binding.cancelButton.icon =
                    view?.context?.let { it1 -> AppCompatResources.getDrawable(it1, R.drawable.ic_close) }

                binding.cancelButton.setOnClickListener {
                    resetCurrentRoute()
                    buildDefaultRoute()
                    mapState.value = MapState.PRESENTATION
                    binding.cancelButton.visibility = View.INVISIBLE
                    routesDialogBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }
        }
    }

    private fun configRecyclers() {
        binding.bottomSheetDialogRoutes.routesRecyclerView.apply {
            adapter = routesListAdapter
        }

        binding.bottomSheetDialogRoutePoints.routePointsRecyclerView.apply {
            adapter = pointsListAdapter
        }
    }

    private fun configBottomSheetDialogs() {
        getRoutesDialog()
        getRoutePointsDialog()

        routesDialogBehavior =
            BottomSheetBehavior.from(binding.bottomSheetDialogRoutes.routesBottomSheetDialog)
        routesDialogBehavior.peekHeight = resources.displayMetrics.heightPixels / 3
        routesDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        routePointsDialogBehavior =
            BottomSheetBehavior.from(binding.bottomSheetDialogRoutePoints.routePointsBottomSheetDialog)
        routePointsDialogBehavior.peekHeight = resources.displayMetrics.heightPixels / 3
        routePointsDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        routeDetailsDialogBehavior =
            BottomSheetBehavior.from(binding.bottomSheetDialogRouteDetails.routeBottomSheetDialog)
        routeDetailsDialogBehavior.peekHeight = resources.displayMetrics.heightPixels / 3
        routeDetailsDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        pointDetailsDialogBehavior =
            BottomSheetBehavior.from(binding.bottomSheetDialogPointDetails.pointBottomSheetDialog)
        pointDetailsDialogBehavior.peekHeight = resources.displayMetrics.heightPixels / 3
        pointDetailsDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun getRoutesDialog() {
        binding.getRoutesList.setOnClickListener {
            routePointsDialogBehavior.peekHeight = 0
            routeDetailsDialogBehavior.peekHeight = 0
            pointDetailsDialogBehavior.peekHeight = 0

            routePointsDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            routeDetailsDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            pointDetailsDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN

            routesDialogBehavior.peekHeight = resources.displayMetrics.heightPixels / 3

            if (routesDialogBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                routesDialogBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            } else {
                routesDialogBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    private fun getRoutePointsDialog() {
        binding.getRoutePointsList.setOnClickListener {
            routesDialogBehavior.peekHeight = 0
            routeDetailsDialogBehavior.peekHeight = 0
            pointDetailsDialogBehavior.peekHeight = 0

            routesDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            routeDetailsDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            pointDetailsDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN

            routePointsDialogBehavior.peekHeight = resources.displayMetrics.heightPixels / 3

            if (routePointsDialogBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                routePointsDialogBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            } else {
                routePointsDialogBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    private fun getRouteDetailsDialog() {
        prepareRouteDetailsDialog(focusedRoute)

        routesDialogBehavior.peekHeight = 0
        routePointsDialogBehavior.peekHeight = 0
        pointDetailsDialogBehavior.peekHeight = 0

        routesDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        routePointsDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        pointDetailsDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        routeDetailsDialogBehavior.peekHeight = resources.displayMetrics.heightPixels / 3

        if (routeDetailsDialogBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
            routeDetailsDialogBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            routeDetailsDialogBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun getPointDetailsDialog(annotation: PointAnnotation) {
        viewLifecycleOwner.lifecycleScope.launch {
            annotation.getData()?.asLong?.let { pointId ->
                viewModel.getPointDetailsPreview(pointId).collect { details ->
                    preparePointDetailsDialog(annotation, details)
                }
            }
        }

        routesDialogBehavior.peekHeight = 0
        routePointsDialogBehavior.peekHeight = 0
        routeDetailsDialogBehavior.peekHeight = 0

        routesDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        routePointsDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        routeDetailsDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        pointDetailsDialogBehavior.peekHeight = resources.displayMetrics.heightPixels / 3

        if (pointDetailsDialogBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
            loadPointData(annotation)
            pointDetailsDialogBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            pointDetailsDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            loadPointData(annotation)
            pointDetailsDialogBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
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

    private fun buildDefaultRoute() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.routes.collect { route ->
                if (route.isNotEmpty()) {
                    if (route.last().coordinatesList.isNotEmpty()) {
                        buildRouteFromList((route.last().coordinatesList.map(::mapPrivateRoutePointModelToPoint)))

                        fetchAnnotatedRoutePoints(route.last())
                        focusedRoute = route.last()
                        eraseCameraToPoint(
                            route.last().coordinatesList[0].x,
                            route.last().coordinatesList[0].y
                        )
                    }

                    routesListAdapter.currentList = route
                }
            }
        }
    }

    private fun fetchAnnotatedRoutePoints(route: PrivateRouteModel) {
        val annotatedPointsList = mutableListOf<PrivateRoutePointDetailsPreviewModel>()

        pointAnnotationManager.deleteAll()
        pointsListAdapter.currentList = emptyList()

        if (route.coordinatesList.first().isRoutePoint) {
            addFlagAnnotationToMap(
                Point.fromLngLat(
                    route.coordinatesList.first().x,
                    route.coordinatesList.first().y,
                ),
                R.drawable.ic_start_flag
            )
        } else {
            addFlagAnnotationToMap(
                Point.fromLngLat(
                    route.coordinatesList.first().x,
                    route.coordinatesList.first().y + 0.0005,
                ),
                R.drawable.ic_start_flag
            )
        }

        if (route.coordinatesList.last().isRoutePoint) {
            addFlagAnnotationToMap(
                Point.fromLngLat(
                    route.coordinatesList.last().x,
                    route.coordinatesList.last().y,
                ),
                R.drawable.ic_finish_flag
            )
        } else {
            addFlagAnnotationToMap(
                Point.fromLngLat(
                    route.coordinatesList.last().x,
                    route.coordinatesList.last().y + 0.0005,
                ),
                R.drawable.ic_finish_flag
            )
        }

        route.coordinatesList.forEach { _route ->
            if (!_route.isRoutePoint) {
                viewLifecycleOwner.lifecycleScope.launch {
                    _route.pointId?.let { id ->
                        viewModel.getPointDetailsPreview(id).collect { details ->
                            details?.let {
                                annotatedPointsList.add(
                                    PrivateRoutePointDetailsPreviewModel(
                                        details.tagList,
                                        details.caption,
                                        details.description,
                                        _route.x,
                                        _route.y
                                    )
                                )
                            }

                            pointsListAdapter.currentList = annotatedPointsList
                        }
                    }
                }

                addAnnotationToMap(_route)
            }
        }
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

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun switchMapMod() {
        mapState.observe(viewLifecycleOwner) {
            routesDialogBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

            with(binding) {
                if (it == MapState.CREATOR) {
                    setEmptyRoute()
                    pointAnnotationManager.deleteAll()
                    pointAnnotationManager.removeClickListener(onPointClickEvent)

                    centralPointer.visibility = View.VISIBLE
                    pointTypeSwitchButton.visibility = View.VISIBLE
                    cancelButton.visibility = View.VISIBLE

                    getRoutePointsList.visibility = View.INVISIBLE
                    getRoutesList.visibility = View.INVISIBLE

                    routesDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    routePointsDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    routeDetailsDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    pointDetailsDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN

                    swapOnMapClickListener(pointTypeSwitchButton.isChecked)

                    pointTypeSwitchButton.addSwitchObserver { _, isChecked ->
                        swapOnMapClickListener(isChecked)
                    }

                    fab.setImageDrawable(
                        context?.getDrawable(
                            R.drawable.ic_confirm
                        )
                    )

                } else if (it == MapState.PRESENTATION) {
                    getRoutePointsList.visibility = View.VISIBLE
                    getRoutesList.visibility = View.VISIBLE

                    centralPointer.visibility = View.INVISIBLE
                    undoPointCreatingButton.visibility = View.INVISIBLE
                    resetRouteButton.visibility = View.INVISIBLE
                    pointTypeSwitchButton.visibility = View.INVISIBLE
                    cancelButton.visibility = View.INVISIBLE

                    mapboxMap.removeOnMapClickListener(regularOnMapClickListener)
                    mapboxMap.removeOnMapClickListener(namedOnMapClickListener)

                    pointAnnotationManager.addClickListener(onPointClickEvent)

                    fab.setImageDrawable(
                        context?.getDrawable(
                            R.drawable.ic_add
                        )
                    )
                }
            }
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

    private fun setRoute(routes: List<DirectionsRoute>) {
        val routeLines = routes.map { RouteLine(it, null) }

        routeLineApi.setRoutes(routeLines) { value ->
            mapboxMap.getStyle()?.apply {
                routeLineView.renderRouteDrawData(this, value)
            }
        }

        if (mapState.value == MapState.CREATOR) {
            binding.resetRouteButton.apply {
                show()
                setOnClickListener {
                    resetCurrentRoute()
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

    private fun setEmptyRoute() {
        val routeLines = emptyList<DirectionsRoute>().map { RouteLine(it, null) }

        routeLineApi.setRoutes(routeLines) { value ->
            mapboxMap.getStyle()?.apply {
                routeLineView.renderRouteDrawData(this, value)
            }
        }
    }

    private fun buildRoute() {
        mapboxNavigation.requestRoutes(
            RouteOptions.builder()
                .applyDefaultNavigationOptions()
                .profile(PROFILE_WALKING)
                .coordinatesList(currentRouteCoordinatesList.map(::mapPrivateRoutePointModelToPoint))
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

    private fun addWaypoint(point: PrivateRoutePointModel) {
        currentRouteCoordinatesList.add(point)

        routeState.value = true

        if (currentRouteCoordinatesList.size == 1) {
            if (binding.pointTypeSwitchButton.isChecked) {
                addFlagAnnotationToMap(
                    Point.fromLngLat(
                        currentRouteCoordinatesList[0].x,
                        currentRouteCoordinatesList[0].y
                    ),
                    R.drawable.ic_start_flag
                )
            } else {
                addFlagAnnotationToMap(
                    Point.fromLngLat(
                        currentRouteCoordinatesList[0].x,
                        currentRouteCoordinatesList[0].y + 0.0005
                    ),
                    R.drawable.ic_start_flag
                )
            }

            binding.undoPointCreatingButton.apply {
                show()
                setOnClickListener {
                    pointAnnotationManager.deleteAll()
                    resetCurrentRoute()
                    hide()
                }
            }
        }

        buildRoute()
    }

    private fun createRoute() {
        if (currentRouteCoordinatesList.isNotEmpty()) {
            val route = PrivateRouteModel(
                null,
                "Empty route caption",
                "Empty route description",
                0.0,
                currentRouteCoordinatesList.map { it.copy() },
                null
            )

            viewModel.addRoute(route)
            currentRouteCoordinatesList.clear()
        }
    }

    private fun deleteRoute(route: PrivateRouteModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            resetCurrentRoute()
            pointAnnotationManager.deleteAll()
            viewModel.deleteRoute(route)
        }
    }

    private fun rebuildRoute(route: PrivateRouteModel) {
        setEmptyRoute()
        pointAnnotationManager.deleteAll()

        buildRouteFromList((route.coordinatesList.map(::mapPrivateRoutePointModelToPoint)))
        fetchAnnotatedRoutePoints(route)
        eraseCameraToPoint(route.coordinatesList[0].x, route.coordinatesList[0].y)
    }

    private fun resetCurrentRoute() {
        setEmptyRoute()
        pointAnnotationManager.deleteAll()
        currentRouteCoordinatesList.clear()
        routeState.value = false

        binding.undoPointCreatingButton.visibility = View.INVISIBLE
        binding.resetRouteButton.visibility = View.INVISIBLE
    }

    private fun undoPointCreating() {
        if (currentRouteCoordinatesList.size > 2) {
            currentRouteCoordinatesList.remove(currentRouteCoordinatesList[currentRouteCoordinatesList.lastIndex])
            buildRoute()
        } else if (currentRouteCoordinatesList.size == 2) {
            resetCurrentRoute()
            pointAnnotationManager.deleteAll()
        }
    }

    override fun onRouteItemClick(route: PrivateRouteModel) {
        rebuildRoute(route)
        focusedRoute = route
        routesDialogBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onPointItemClick(point: PrivateRoutePointDetailsPreviewModel) {
        eraseCameraToPoint(point.x, point.y)

        routePointsDialogBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun addFlagAnnotationToMap(point: Point, resourceId: Int) {
        activity?.applicationContext?.let {
            bitmapFromDrawableRes(it, resourceId)?.let { image ->
                pointAnnotationManager.create(
                    createFlagAnnotationPoint(
                        image,
                        point
                    )
                )
                pointAnnotationManager.addLongClickListener(OnPointAnnotationLongClickListener {


                    true
                })
            }
        }
    }

    private fun createFlagAnnotationPoint(bitmap: Bitmap, point: Point): PointAnnotationOptions {
        return PointAnnotationOptions()
            .withPoint(point)
            .withIconImage(bitmap)
            .withIconAnchor(IconAnchor.BOTTOM_LEFT)
            .withIconOffset(listOf(-9.6, 3.8))
    }

    private fun addEmptyAnnotationToMap(point: PrivateRoutePointModel) {
        activity?.applicationContext?.let {
            bitmapFromDrawableRes(it, R.drawable.ic_pin_point)?.let { image ->
                pointAnnotationManager.create(
                    createAnnotationPoint(
                        image,
                        Point.fromLngLat(point.x, point.y)
                    )
                )
            }
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
            }
        }
    }

    private fun loadPointData(annotation: PointAnnotation) {
        viewLifecycleOwner.lifecycleScope.launch {
            annotation.getData()?.asLong?.let { pointId ->
                viewModel.getPointDetailsPreview(pointId).collect { details ->
                    preparePointDetailsDialog(annotation, details)
                }
            }
        }
    }

    private fun preparePointDetailsDialog(
        pointAnnotation: PointAnnotation,
        details: PrivateRoutePointDetailsPreviewModel?
    ) {
        binding.bottomSheetDialogPointDetails.apply {
            pointCaptionText.text = details?.caption ?: ""
            pointDescriptionText.text = details?.description ?: ""
            tagListTextView.text = details?.tagList?.joinToString(",", "Tags: ")
            { pointTagModel -> pointTagModel.name } ?: ""

            pointDetailsEditButton.setOnClickListener {
                findNavController().navigate(
                    PrivatePointsFragmentDirections
                        .actionPrivatePointsFragmentToPointDetailsFragment(pointAnnotation.getData()?.asLong!!)
                )
            }

            pointDetailsDeleteButton.setOnClickListener {
                pointAnnotation.getData()?.asLong?.let { pointId ->
                    viewModel.deletePoint(
                        pointId
                    )
                }

                pointAnnotationManager.delete(pointAnnotation)
                pointDetailsDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    private fun prepareRouteDetailsDialog(
        route: PrivateRouteModel
    ) {
        binding.bottomSheetDialogRouteDetails.apply {
            routeCaptionText.text = route.name
            routeDescriptionText.text = route.description
            routeRating.text = route.rating.toString()

            routeDetailsEditButton.setOnClickListener {
                route.routeId?.let {
                    findNavController().navigate(
                        PrivateRoutesFragmentDirections
                            .actionPrivateRoutesFragmentToRouteDetailsFragment(it)
                    )
                }
            }

            routeDetailsDeleteButton.setOnClickListener {
                deleteRoute(route)

                pointDetailsDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN
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

    private fun eraseCameraToPoint(x: Double, y: Double) {
        binding.mapView.camera.easeTo(
            CameraOptions.Builder()
                .center(Point.fromLngLat(x, y))
                .zoom(15.0)
                .build()
        )
    }
}