package com.example.gh_coursework.ui.private_route

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.gh_coursework.MapState
import com.example.gh_coursework.R
import com.example.gh_coursework.databinding.FragmentPrivateRouteBinding
import com.example.gh_coursework.ui.adapter.ImagesPreviewAdapter
import com.example.gh_coursework.ui.helper.convertDrawableToBitmap
import com.example.gh_coursework.ui.helper.createAnnotationPoint
import com.example.gh_coursework.ui.helper.createFlagAnnotationPoint
import com.example.gh_coursework.ui.helper.createOnMapClickEvent
import com.example.gh_coursework.ui.model.ImageModel
import com.example.gh_coursework.ui.private_route.adapter.RoutePointsListAdapter
import com.example.gh_coursework.ui.private_route.adapter.RoutePointsListCallback
import com.example.gh_coursework.ui.private_route.adapter.RoutesListAdapter
import com.example.gh_coursework.ui.private_route.adapter.RoutesListAdapterCallback
import com.example.gh_coursework.ui.private_route.mapper.mapPrivateRoutePointModelToPoint
import com.example.gh_coursework.ui.private_route.model.RouteModel
import com.example.gh_coursework.ui.private_route.model.RoutePointModel
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
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
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

    private lateinit var routeImagesPreviewAdapter: ImagesPreviewAdapter
    private lateinit var pointImagesPreviewAdapter: ImagesPreviewAdapter

    private lateinit var pointImageLayoutManager: LinearLayoutManager
    private lateinit var routeImageLayoutManager: LinearLayoutManager
    private lateinit var binding: FragmentPrivateRouteBinding

    private val viewModel: RouteViewModel by viewModel()
    private val routesListAdapter = RoutesListAdapter(this as RoutesListAdapterCallback)
    private val pointsListAdapter = RoutePointsListAdapter(this as RoutePointsListCallback)

    private var currentRoutePointsList = mutableListOf<RoutePointModel>()
    private val creatingRouteCoordinatesList = mutableListOf<RoutePointModel>()
    private lateinit var focusedRoute: RouteModel

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

    @OptIn(MapboxExperimental::class)
    private lateinit var viewAnnotationManager: ViewAnnotationManager
    private lateinit var pointAnnotationManager: PointAnnotationManager

    private val regularOnMapClickListener = OnMapClickListener { point ->
        val newPoint = RoutePointModel(
            null,
            "",
            "",
            emptyList(),
            emptyList(),
            point.longitude(),
            point.latitude(),
            true
        )
        addWaypoint(newPoint)
        return@OnMapClickListener true
    }

    private val namedOnMapClickListener = OnMapClickListener { point ->
        val result = pointAnnotationManager.annotations.find {
            return@find it.point.latitude() == point.latitude()
                    && it.point.longitude() == point.longitude()
        }

        if (result == null) {
            val newPoint = RoutePointModel(
                null,
                "",
                "",
                emptyList(),
                emptyList(),
                point.longitude(),
                point.latitude(),
                false
            )
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
        configImageRecyclers()
        configBottomSheetDialogs()
        initMapboxNavigation()
        initRouteLine()
        fetchRoutes()

        mapboxNavigation.startTripSession(withForegroundService = false)
    }

    private fun configImageRecyclers() {
        PagerSnapHelper().attachToRecyclerView(binding.bottomSheetDialogPointDetails.imageRecycler)
        PagerSnapHelper().attachToRecyclerView(binding.bottomSheetDialogRouteDetails.imageRecycler)

        pointImageLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        routeImageLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
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
        val clickEvent = createOnMapClickEvent(
            Pair(
                resources.displayMetrics.widthPixels / 2,
                resources.displayMetrics.heightPixels / 2
            )
        )
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
                }
            } else if (routeState.value == false) {
                binding.cancelButton.text = getString(R.string.txtCancelButtonExit)
                binding.cancelButton.icon =
                    view?.context?.let { it1 ->
                        AppCompatResources.getDrawable(
                            it1,
                            R.drawable.ic_close
                        )
                    }

                binding.cancelButton.setOnClickListener {
                    resetCurrentRoute()
                    fetchRoutes()
                    mapState.value = MapState.PRESENTATION
                    binding.cancelButton.visibility = View.INVISIBLE
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

        loadPointData(annotation)

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

    private fun fetchRoutes() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.routes
                .collect { route ->
                    if (route.isNotEmpty()) {
                        rebuildRoute(route.last())
                        routesListAdapter.submitList(route)
                        binding.bottomSheetDialogRoutes.emptyDataPlaceholder.visibility =
                            View.GONE
                    } else if (route.isEmpty()) {
                        routesListAdapter.submitList(route)
                        pointsListAdapter.submitList(emptyList())

                        binding.bottomSheetDialogRoutes.emptyDataPlaceholder.visibility =
                            View.VISIBLE
                        binding.bottomSheetDialogRoutePoints.emptyDataPlaceholder.visibility =
                            View.VISIBLE
                    }
                }
        }
    }

    private fun fetchAnnotatedRoutePoints() {
        val annotatedPoints = mutableListOf<RoutePointModel>()
        val imageList = mutableListOf<ImageModel>()

        pointAnnotationManager.deleteAll()
        binding.bottomSheetDialogRoutePoints.emptyDataPlaceholder.visibility =
            View.VISIBLE

        if (currentRoutePointsList.first().isRoutePoint) {
            addFlagAnnotationToMap(
                Point.fromLngLat(
                    currentRoutePointsList.first().x,
                    currentRoutePointsList.first().y,
                ),
                R.drawable.ic_start_flag
            )
        } else {
            addFlagAnnotationToMap(
                Point.fromLngLat(
                    currentRoutePointsList.first().x,
                    currentRoutePointsList.first().y + 0.00005,
                ),
                R.drawable.ic_start_flag
            )
        }

        if (currentRoutePointsList.last().isRoutePoint) {
            addFlagAnnotationToMap(
                Point.fromLngLat(
                    currentRoutePointsList.last().x,
                    currentRoutePointsList.last().y,
                ),
                R.drawable.ic_finish_flag
            )
        } else {
            addFlagAnnotationToMap(
                Point.fromLngLat(
                    currentRoutePointsList.last().x,
                    currentRoutePointsList.last().y + 0.00005,
                ),
                R.drawable.ic_finish_flag
            )
        }

        currentRoutePointsList.forEach {
            if (!it.isRoutePoint) {
                addAnnotationToMap(it)
                annotatedPoints.add(it)
                binding.bottomSheetDialogRoutePoints.emptyDataPlaceholder.visibility =
                    View.GONE

                if (it.imageList.isNotEmpty()) {
                    imageList.addAll(it.imageList)
                }
            }

            pointsListAdapter.submitList(annotatedPoints)
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
        } else if (!isChecked) {
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
                .coordinatesList(creatingRouteCoordinatesList.map(::mapPrivateRoutePointModelToPoint))
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

    private fun addWaypoint(point: RoutePointModel) {
        creatingRouteCoordinatesList.add(point)

        if (creatingRouteCoordinatesList.size == 1) {
            if (binding.pointTypeSwitchButton.isChecked) {
                addFlagAnnotationToMap(
                    Point.fromLngLat(
                        creatingRouteCoordinatesList[0].x,
                        creatingRouteCoordinatesList[0].y
                    ),
                    R.drawable.ic_start_flag
                )
            } else {
                addFlagAnnotationToMap(
                    Point.fromLngLat(
                        creatingRouteCoordinatesList[0].x,
                        creatingRouteCoordinatesList[0].y + 0.00005
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

        routeState.value = true
        buildRoute()
    }

    private fun createRoute() {
        if (creatingRouteCoordinatesList.size > 1) {
            val route = RouteModel(
                null,
                "",
                "",
                null,
                emptyList(),
                emptyList()
            )

            viewModel.addRoute(route, creatingRouteCoordinatesList.map { it.copy() })
            creatingRouteCoordinatesList.clear()
        }
    }

    private fun deleteRoute(route: RouteModel) {
        resetCurrentRoute()
        viewModel.deleteRoute(route)
    }

    private fun rebuildRoute(route: RouteModel) {
        focusedRoute = route
        viewLifecycleOwner.lifecycleScope.launch {
            route.routeId?.let { routeId ->
                viewModel.getRoutePointsList(routeId).collect { pointsList ->
                    if (pointsList.isNotEmpty()) {
                        currentRoutePointsList =
                            pointsList.map { it.copy() } as MutableList<RoutePointModel>

                        buildRouteFromList(currentRoutePointsList.map(::mapPrivateRoutePointModelToPoint))
                        fetchAnnotatedRoutePoints()
                        eraseCameraToPoint(
                            currentRoutePointsList[0].x,
                            currentRoutePointsList[0].y
                        )
                    }
                }
            }
        }
    }

    private fun resetCurrentRoute() {
        setEmptyRoute()
        pointAnnotationManager.deleteAll()
        creatingRouteCoordinatesList.clear()
        routeState.value = false

        binding.undoPointCreatingButton.visibility = View.GONE
        binding.resetRouteButton.visibility = View.GONE
    }

    private fun undoPointCreating() {
        when {
            creatingRouteCoordinatesList.size > 2 -> {
                if (!creatingRouteCoordinatesList.last().isRoutePoint) {
                    pointAnnotationManager.delete(pointAnnotationManager.annotations.last())
                }

                creatingRouteCoordinatesList.remove(creatingRouteCoordinatesList[creatingRouteCoordinatesList.lastIndex])
                buildRoute()
            }

            creatingRouteCoordinatesList.size == 2 -> {
                if (!creatingRouteCoordinatesList.last().isRoutePoint && pointAnnotationManager.annotations.size == 2) {
                    pointAnnotationManager.delete(pointAnnotationManager.annotations.last())
                }

                creatingRouteCoordinatesList.remove(creatingRouteCoordinatesList[creatingRouteCoordinatesList.lastIndex])
                routeState.value = false
                setEmptyRoute()
            }

            creatingRouteCoordinatesList.size == 1 -> {
                resetCurrentRoute()
            }
        }
    }

    override fun onRouteItemClick(route: RouteModel) {
        rebuildRoute(route)
        focusedRoute = route
        routesDialogBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onPointItemClick(pointId: Long) {
        val pointPreview = currentRoutePointsList.find {
            it.pointId == pointId
        }

        pointPreview?.let { eraseCameraToPoint(pointPreview.x, pointPreview.y) }
        routePointsDialogBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun addFlagAnnotationToMap(point: Point, resourceId: Int) {
        activity?.applicationContext?.let {
            convertDrawableToBitmap(AppCompatResources.getDrawable(it, resourceId))?.let { image ->
                pointAnnotationManager.create(
                    createFlagAnnotationPoint(
                        image,
                        point
                    )
                )
            }
        }
    }

    private fun addEmptyAnnotationToMap(point: RoutePointModel) {
        activity?.applicationContext?.let {
            convertDrawableToBitmap(
                AppCompatResources.getDrawable(
                    it,
                    R.drawable.ic_pin_point
                )
            )?.let { image ->
                pointAnnotationManager.create(
                    createAnnotationPoint(
                        image,
                        Point.fromLngLat(point.x, point.y)
                    )
                )
            }
        }
    }

    private fun addAnnotationToMap(point: RoutePointModel) {
        activity?.applicationContext?.let {
            convertDrawableToBitmap(
                AppCompatResources.getDrawable(
                    it,
                    R.drawable.ic_pin_point
                )
            )?.let { image ->
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
        annotation.getData()?.asLong?.let { pointId ->
            val point = currentRoutePointsList.find { it.pointId == pointId }

            if (point != null) {
                preparePointDetailsDialog(annotation, point)
            }
        }
    }

    private fun preparePointDetailsDialog(
        pointAnnotation: PointAnnotation,
        details: RoutePointModel
    ) {
        binding.bottomSheetDialogPointDetails.apply {
            if (pointCaptionText.text.isEmpty() && pointDescriptionText.text.isEmpty() && tagListTextView.text.isEmpty()) {
                emptyDataPlaceholder.visibility = View.VISIBLE
            } else {
                pointCaptionText.text = details.caption
                pointDescriptionText.text = details.description
                tagListTextView.text = details.tagList.joinToString(",", "Tags: ")
                { pointTagModel -> pointTagModel.name }
                emptyDataPlaceholder.visibility = View.GONE
            }

            pointImagesPreviewAdapter = ImagesPreviewAdapter {
                findNavController().navigate(
                    PrivateRoutesFragmentDirections.actionPrivateRoutesFragmentToPrivatePointImageDetails(
                        details.pointId!!,
                        pointImageLayoutManager.findFirstVisibleItemPosition()
                    )
                )
            }

            imageRecycler.apply {
                adapter = pointImagesPreviewAdapter
                layoutManager = pointImageLayoutManager
            }

            pointImagesPreviewAdapter.submitList(details.imageList)

            pointDetailsEditButton.setOnClickListener {
                findNavController().navigate(
                    PrivateRoutesFragmentDirections
                        .actionPrivateRoutesFragmentToPointDetailsFragment(
                            pointAnnotation.getData()?.asLong!!
                        )
                )
            }

            pointDetailsDeleteButton.setOnClickListener {
                binding.bottomSheetDialogRoutePoints.emptyDataPlaceholder.visibility = View.VISIBLE

                if (currentRoutePointsList.size == 2) {
                    deleteRoute(focusedRoute)
                } else {
                    pointAnnotation.getData()?.asLong?.let { pointId ->
                        viewModel.deletePoint(pointId)
                        pointAnnotationManager.delete(pointAnnotation)

                        binding.bottomSheetDialogRoutePoints.emptyDataPlaceholder.visibility =
                            View.GONE
                    }
                }

                pointDetailsDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    private fun prepareRouteDetailsDialog(
        route: RouteModel
    ) {
        val imageList = mutableListOf<ImageModel>()

        binding.bottomSheetDialogRouteDetails.apply {
            if (routeCaptionText.text.isEmpty() && routeDescriptionText.text.isEmpty()) {
                emptyDataPlaceholder.visibility = View.VISIBLE
            } else {
                routeCaptionText.text = route.name
                routeDescriptionText.text = route.description
                emptyDataPlaceholder.visibility = View.GONE
            }

            routeDetailsEditButton.setOnClickListener {
                route.routeId?.let {
                    findNavController().navigate(
                        PrivateRoutesFragmentDirections
                            .actionPrivateRoutesFragmentToRouteDetailsFragment(it)
                    )
                }
            }

            routeImagesPreviewAdapter = ImagesPreviewAdapter() {
                findNavController().navigate(
                    PrivateRoutesFragmentDirections.actionPrivateRoutesFragmentToPrivateRouteImageDetails(
                        route.routeId!!,
                        routeImageLayoutManager.findFirstVisibleItemPosition()
                    )
                )
            }

            imageRecycler.apply {
                adapter = routeImagesPreviewAdapter
                layoutManager = routeImageLayoutManager
            }

            imageList.addAll(route.imageList)
            currentRoutePointsList.forEach {
                imageList.addAll(it.imageList)
            }

            routeImagesPreviewAdapter.submitList(imageList)

            routeDetailsDeleteButton.setOnClickListener {
                deleteRoute(route)

                routeDetailsDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    private fun eraseCameraToPoint(x: Double, y: Double) {
        binding.mapView.camera.easeTo(
            CameraOptions.Builder()
                .center(Point.fromLngLat(x, y))
                .zoom(14.0)
                .build()
        )
    }
}