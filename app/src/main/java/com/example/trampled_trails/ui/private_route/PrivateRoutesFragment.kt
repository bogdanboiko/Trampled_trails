package com.example.trampled_trails.ui.private_route

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeFragment
import com.example.trampled_trails.ActivityViewModel
import com.example.trampled_trails.ui.data.MapState
import com.example.trampled_trails.R
import com.example.trampled_trails.databinding.FragmentPrivateRouteBinding
import com.example.trampled_trails.ui.helper.*
import com.example.trampled_trails.ui.private_image_details.adapter.ImagesPreviewAdapter
import com.example.trampled_trails.ui.private_image_details.model.ImageModel
import com.example.trampled_trails.ui.private_route.adapter.RoutePointsListAdapter
import com.example.trampled_trails.ui.private_route.adapter.RoutePointsListCallback
import com.example.trampled_trails.ui.private_route.adapter.RoutesListAdapter
import com.example.trampled_trails.ui.private_route.adapter.RoutesListAdapterCallback
import com.example.trampled_trails.ui.private_route.helper.configPrivateRouteFragmentBottomNavBar
import com.example.trampled_trails.ui.private_route.helper.syncPrivateRoutesFragmentTheme
import com.example.trampled_trails.ui.private_route.mapper.mapPrivateRoutePointModelToPoint
import com.example.trampled_trails.ui.private_route.model.RouteModel
import com.example.trampled_trails.ui.private_route.model.RoutePointModel
import com.example.trampled_trails.ui.private_route.tag_dialog.RouteFilterByTagDialogFragment
import com.example.trampled_trails.ui.route_details.model.RouteTagModel
import com.example.trampled_trails.ui.themes.MyAppTheme
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.JsonPrimitive
import com.mapbox.api.directions.v5.DirectionsCriteria.PROFILE_WALKING
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.compass.compass
import com.mapbox.maps.plugin.gestures.OnMapClickListener
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.gestures.removeOnMapClickListener
import com.mapbox.maps.plugin.locationcomponent.location
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

@OptIn(ExperimentalPreviewMapboxNavigationAPI::class)
class PrivateRoutesFragment :
    ThemeFragment(),
    RoutesListAdapterCallback,
    RoutePointsListCallback {

    private lateinit var binding: FragmentPrivateRouteBinding
    private lateinit var theme: MyAppTheme

    private lateinit var routesFetchingJob: Job
    private lateinit var routeImagesPreviewAdapter: ImagesPreviewAdapter
    private lateinit var pointImagesPreviewAdapter: ImagesPreviewAdapter
    private lateinit var pointImageLayoutManager: LinearLayoutManager
    private lateinit var routeImageLayoutManager: LinearLayoutManager

    private val routesListAdapter = RoutesListAdapter(this as RoutesListAdapterCallback)
    private val pointsListAdapter = RoutePointsListAdapter(this as RoutePointsListCallback)

    private val viewModelMain: ActivityViewModel by viewModel()
    private val viewModelPrivate: PrivateRouteViewModel by viewModel()
    private var internetCheckCallback: InternetCheckCallback? = null
    private var getUserIdCallback: GetUserIdCallback? = null

    private var isPublic = false
    private var currentRoutePointsList = mutableListOf<RoutePointModel>()
    private val creatingRouteCoordinatesList = mutableListOf<RoutePointModel>()
    private var filteredTags = emptyList<RouteTagModel>()
    private var creatingRouteId: String? = null
    private var previousRouteId: String? = null
    private lateinit var focusedRoute: RouteModel
    private lateinit var lastSeenCoordinate: Point
    private lateinit var routePointsJob: Job

    private lateinit var routesDialogBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var routePointsDialogBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var routeDetailsDialogBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var pointDetailsDialogBehavior: BottomSheetBehavior<ConstraintLayout>

    private lateinit var mapboxNavigation: MapboxNavigation
    private lateinit var mapboxMap: MapboxMap
    private lateinit var routeLineApi: MapboxRouteLineApi
    private lateinit var routeLineView: MapboxRouteLineView
    private lateinit var pointAnnotationManager: PointAnnotationManager
    private var mapState: MapState = MapState.PRESENTATION
        set(value) {
            when(value) {
                MapState.CREATOR -> mapModCreator()
                MapState.PRESENTATION -> mapModPresentation()
            }
            field = value
        }
    private var isRouteSaveable = MutableLiveData(false)
    private val navigationLocationProvider = NavigationLocationProvider()

    private val onClickAddDefaultRoutePoint = OnMapClickListener { point ->
        creatingRouteId?.let {
            RoutePointModel(
                x = point.longitude(),
                y = point.latitude(),
                routeId = it,
                isRoutePoint = true,
                position = (currentRoutePointsList.size - 1).toLong()
            )
        }?.also { newPoint -> addWaypoint(newPoint) }

        configUndoPointCreationButton()
        configResetRouteButton()

        return@OnMapClickListener true
    }

    private val onClickAddAnnotatedRoutePoint = OnMapClickListener { point ->
        pointAnnotationManager.annotations.find {
            return@find it.point.latitude() == point.latitude()
                    && it.point.longitude() == point.longitude()
        }.let { pointAnnotation ->
            if (pointAnnotation == null) {
                creatingRouteId?.let {
                    RoutePointModel(
                        x = point.longitude(),
                        y = point.latitude(),
                        routeId = it,
                        isRoutePoint = false,
                        position = (currentRoutePointsList.size - 1).toLong()
                    )
                }?.also { newPoint ->
                    addEmptyAnnotationToMap(newPoint)
                    addWaypoint(newPoint)
                }
            }

            configUndoPointCreationButton()
            configResetRouteButton()
        }

        return@OnMapClickListener true
    }

    private val onAnnotatedPointClickEvent = OnPointAnnotationClickListener { annotation ->
        if (annotation.getData()?.isJsonNull == false) {
            getPointDetailsDialog(annotation)
        } else  {
            getRouteDetailsDialog()
        }

        true
    }

    private val routesObserver = RoutesObserver { routeUpdateResult ->
        if (routeUpdateResult.routes.isNotEmpty()) {
            // generate route geometries and render them
            routeUpdateResult.routes.map { RouteLine(it, null) }.let { routeLines ->
                routeLineApi.setRoutes(
                    routeLines
                ) { value ->
                    mapboxMap.getStyle()?.apply {
                        routeLineView.renderRouteDrawData(this, value)
                    }
                }
            }
        } else {
            // remove the route line and route arrow from the map
            mapboxMap.getStyle()?.let { style ->
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
            locationMatcherResult.enhancedLocation.let { enhancedLocation ->
                navigationLocationProvider.changePosition(
                    location = enhancedLocation,
                    keyPoints = locationMatcherResult.keyPoints,
                )

                if (!firstLocationUpdateReceived) {
                    firstLocationUpdateReceived = true
                    moveCameraTo(enhancedLocation)
                }
            }
        }

        private fun moveCameraTo(location: Location) {
            eraseCameraToPoint(
                location.longitude,
                location.latitude,
                binding.mapView
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        internetCheckCallback = context as? InternetCheckCallback
        getUserIdCallback = context as? GetUserIdCallback
    }

    override fun onDetach() {
        super.onDetach()

        internetCheckCallback = null
        getUserIdCallback = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPrivateRouteBinding.inflate(inflater, container, false)

        binding.bottomSheetDialogRoutes.root.layoutParams.height = resources.displayMetrics.heightPixels / 3
        binding.bottomSheetDialogRoutePoints.root.layoutParams.height = resources.displayMetrics.heightPixels / 3

        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configMap()
        configMapStateSwitcher()
        configBottomNavBar()
        onNavigateToPrivatePointsButtonClickListener()
        onNavigateToHomepageButtonClickListener()
        configSaveRouteButton()
        setUpBottomSheetsRecyclers()
        configImageRecyclers()
        configBottomSheetDialogs()
        initMapboxNavigation()
        initRouteLine()

        if (this::focusedRoute.isInitialized) {
            previousRouteId = focusedRoute.routeId
        }

        fetchRoutes()

        setFragmentResultListener(RouteFilterByTagDialogFragment.REQUEST_KEY) { key, bundle ->
            bundle.getParcelableArray("tags")?.toList()?.let { tagList ->
                if (tagList.isEmpty()) {
                    binding.bottomSheetDialogRoutes.emptyDataPlaceholder.text =
                        context?.resources?.getString(R.string.placeholder_private_routes_empty_list)
                }

                resetCurrentRoute()
                fetchRoutes()
            }
        }

        mapboxNavigation.startTripSession(withForegroundService = false)
    }

    override fun onStart() {
        super.onStart()
        mapboxNavigation.registerRoutesObserver(routesObserver)
        mapboxNavigation.registerLocationObserver(locationObserver)

        if (this::lastSeenCoordinate.isInitialized) {
            eraseCameraToPoint(lastSeenCoordinate.longitude(), lastSeenCoordinate.latitude(), binding.mapView)
        }
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

    //sync app theme
    override fun syncTheme(appTheme: AppTheme) {
        theme = appTheme as MyAppTheme
        syncPrivateRoutesFragmentTheme(theme, binding, requireContext())
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
            it.loadStyleUri(Style.MAPBOX_STREETS)
        }

        binding.mapView.location.apply {
            setLocationProvider(navigationLocationProvider)
            enabled = true
        }

        binding.mapView.compass.enabled = false

        pointAnnotationManager = binding.mapView.annotations.createPointAnnotationManager()
        pointAnnotationManager.addClickListener(onAnnotatedPointClickEvent)
    }

    private fun configMapStateSwitcher() {
        binding.createButton.setOnClickListener {
            if (mapState == MapState.CREATOR) {
                executeOnScreenCenterClick()
            } else {
                mapState = MapState.CREATOR
            }
        }
    }

    private fun executeOnScreenCenterClick() {
        createOnMapClickEvent(
            Pair(
                resources.displayMetrics.widthPixels / 2,
                resources.displayMetrics.heightPixels / 2
            )
        ).also { clickEvent ->
            binding.mapView.dispatchTouchEvent(clickEvent.first)
            binding.mapView.dispatchTouchEvent(clickEvent.second)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun mapModCreator() {
        with(binding) {
            creatingRouteId = UUID.randomUUID().toString()
            setEmptyRoute()
            pointAnnotationManager.deleteAll()
            pointAnnotationManager.removeClickListener(onAnnotatedPointClickEvent)

            groupCreatorItems.isVisible = true
            groupButtonsItems.isVisible = false

            routesDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            routePointsDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            routeDetailsDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            pointDetailsDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN

            switchOnMapClickListener(pointTypeSwitchButton.isChecked)

            pointTypeSwitchButton.addSwitchObserver { _, isChecked ->
                switchOnMapClickListener(isChecked)
            }

            createButton.setImageDrawable(
                context?.getDrawable(
                    R.drawable.ic_confirm
                )
            )
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun mapModPresentation() {
        with(binding) {
            creatingRouteId = null

            groupCreatorItems.isVisible = false
            groupButtonsItems.isVisible = true

            undoPointCreatingButton.visibility = View.INVISIBLE
            resetRouteButton.visibility = View.INVISIBLE

            mapboxMap.removeOnMapClickListener(onClickAddDefaultRoutePoint)
            mapboxMap.removeOnMapClickListener(onClickAddAnnotatedRoutePoint)

            pointAnnotationManager.addClickListener(onAnnotatedPointClickEvent)

            createButton.setImageDrawable(
                context?.getDrawable(
                    R.drawable.ic_add
                )
            )
        }
    }

    private fun configBottomNavBar() {
        internetCheckCallback?.isInternetAvailable()?.let { isAvailable -> view?.let { view ->
                configPrivateRouteFragmentBottomNavBar(isAvailable, binding, requireContext(), view)
                }
            }
    }

    private fun switchOnMapClickListener(isChecked: Boolean) {
        val pointIcon: Int
        val routeIcon: Int

        if (theme.id() == 0) {
            pointIcon = R.drawable.ic_pin_route_light
            routeIcon = R.drawable.ic_pin_point_light
        } else {
            pointIcon = R.drawable.ic_pin_route_dark
            routeIcon = R.drawable.ic_pin_point_dark
        }

        mapboxMap.removeOnMapClickListener(onClickAddAnnotatedRoutePoint)
        mapboxMap.removeOnMapClickListener(onClickAddDefaultRoutePoint)

        if (isChecked) {
            mapboxMap.addOnMapClickListener(onClickAddDefaultRoutePoint)
            binding.centralPointer.setImageResource(pointIcon)
        } else if (!isChecked) {
            mapboxMap.addOnMapClickListener(onClickAddAnnotatedRoutePoint)
            binding.centralPointer.setImageResource(routeIcon)
        }
    }

    private fun onNavigateToPrivatePointsButtonClickListener() {
        binding.mapRoutePointModSwitcher.setOnClickListener {
            findNavController().navigate(
                PrivateRoutesFragmentDirections
                    .actionPrivateRoutesFragmentToPrivatePointsFragment()
            )
        }
    }

    private fun onNavigateToHomepageButtonClickListener() {
        binding.homepageButton.setOnClickListener {
            lastSeenCoordinate = binding.mapView.getMapboxMap().cameraState.center

            findNavController().navigate(
                PrivateRoutesFragmentDirections
                    .actionPrivateRoutesFragmentToHomepageFragment()
            )
        }
    }


    private fun configSaveRouteButton() {
        isRouteSaveable.observe(viewLifecycleOwner) { isSaveable ->
            if (isSaveable) {
                binding.saveRouteButton.text = getString(R.string.save_button_save)
                binding.saveRouteButton.icon =
                    view?.context?.let { context ->
                        AppCompatResources.getDrawable(
                            context,
                            R.drawable.ic_confirm
                        )
                    }

                binding.saveRouteButton.setOnClickListener {
                    saveRoute()
                    mapState = MapState.PRESENTATION
                    isRouteSaveable.value = false
                }
            } else {
                binding.saveRouteButton.text = getString(R.string.save_button_disable)
                binding.saveRouteButton.icon =
                    view?.context?.let { context ->
                        AppCompatResources.getDrawable(
                            context,
                            R.drawable.ic_close
                        )
                    }

                binding.saveRouteButton.setOnClickListener {
                    resetCurrentRoute()
                    mapState = MapState.PRESENTATION
                }
            }
        }
    }

    private fun saveRoute() {
        if (creatingRouteCoordinatesList.size > 1) {
            creatingRouteId?.let {
                RouteModel(
                    routeId = it,
                    isPublic = false
                )
            }?.let { route ->
                viewModelPrivate.addRoute(route, creatingRouteCoordinatesList.map { it.copy() })
            }

            creatingRouteCoordinatesList.clear()

            if (internetCheckCallback?.isInternetAvailable() == true) {
                viewLifecycleOwner.lifecycleScope.launch {
                    getUserIdCallback?.getUserId()?.let { userId ->
                        viewModelMain.uploadPoints(userId)
                        viewModelMain.uploadRoutes(userId)
                    }
                }
            }
        }
    }

    private fun configUndoPointCreationButton() {
        if (mapState == MapState.CREATOR && creatingRouteCoordinatesList.size > 0) {
            binding.undoPointCreatingButton.apply {
                show()
                setOnClickListener {
                    undoPointCreating()

                    if (creatingRouteCoordinatesList.size == 0) {
                        hide()
                    }
                }
            }
        }
    }

    private fun configResetRouteButton() {
        if (mapState == MapState.CREATOR && creatingRouteCoordinatesList.size > 1) {
            binding.resetRouteButton.apply {
                show()
                setOnClickListener {
                    resetCurrentRoute()
                    hide()
                }
            }
        }
    }

    private fun resetCurrentRoute() {
        setEmptyRoute()
        pointAnnotationManager.deleteAll()
        creatingRouteCoordinatesList.clear()
        isRouteSaveable.value = false

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
                if (!creatingRouteCoordinatesList.last().isRoutePoint) {
                    pointAnnotationManager.delete(pointAnnotationManager.annotations.last())
                }

                binding.resetRouteButton.visibility = View.GONE
                creatingRouteCoordinatesList.remove(creatingRouteCoordinatesList.last())
                isRouteSaveable.value = false
                setEmptyRoute()
            }

            creatingRouteCoordinatesList.size == 1 -> {
                resetCurrentRoute()
            }
        }
    }

    private fun setEmptyRoute() {
        emptyList<DirectionsRoute>().map { RouteLine(it, null) }.let { routeLines ->
            routeLineApi.setRoutes(routeLines) { value ->
                mapboxMap.getStyle()?.apply {
                    routeLineView.renderRouteDrawData(this, value)
                }
            }
        }
    }

    //bottom sheets set up start
    private fun setUpBottomSheetsRecyclers() {
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

        routePointsDialogBehavior =
            BottomSheetBehavior.from(binding.bottomSheetDialogRoutePoints.routePointsBottomSheetDialog)

        routeDetailsDialogBehavior =
            BottomSheetBehavior.from(binding.bottomSheetDialogRouteDetails.routeBottomSheetDialog)

        pointDetailsDialogBehavior =
            BottomSheetBehavior.from(binding.bottomSheetDialogPointDetails.pointBottomSheetDialog)
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
                routesDialogBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        binding.bottomSheetDialogRoutes.routeFilterByTagButton.setOnClickListener {
            findNavController().navigate(
                PrivateRoutesFragmentDirections.actionPrivateRoutesFragmentToRouteFilterByTagsDialogFragment(
                    filteredTags.toTypedArray()
                )
            )
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
                routePointsDialogBehavior.state = BottomSheetBehavior.STATE_EXPANDED
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
        }
    }

    private fun getPointDetailsDialog(annotation: PointAnnotation) {

        loadAnnotatedPointData(annotation)

        routesDialogBehavior.peekHeight = 0
        routePointsDialogBehavior.peekHeight = 0
        routeDetailsDialogBehavior.peekHeight = 0

        routesDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        routePointsDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        routeDetailsDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        pointDetailsDialogBehavior.peekHeight = resources.displayMetrics.heightPixels / 3

        if (pointDetailsDialogBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
            pointDetailsDialogBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }
    //bottom sheets set up end

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
        if (this::routesFetchingJob.isInitialized) {
            routesFetchingJob.cancel()
        }

        routesFetchingJob = viewLifecycleOwner.lifecycleScope.launch {
            viewModelPrivate.routes.distinctUntilChanged().collect { routes ->
                var filteredRoutes = mutableListOf<RouteModel>()

                if (filteredTags.isNotEmpty()) {
                    routes.forEach { route ->
                        filteredTags.forEach tags@{
                            if (route.tagsList.contains(it)) {
                                filteredRoutes.add(route)
                                return@tags
                            }
                        }
                    }

                    if (filteredRoutes.isEmpty()) {
                        binding.bottomSheetDialogRoutes.emptyDataPlaceholder.text =
                            context?.resources?.getString(R.string.placeholder_private_routes_not_found_by_tags)
                    }
                } else {
                    filteredRoutes = routes.toMutableList()
                }

                routesListAdapter.submitList(filteredRoutes)

                if (filteredRoutes.isNotEmpty()) {
                    if (previousRouteId != null) {
                        filteredRoutes.find { it.routeId == previousRouteId }
                            ?.let { rebuildRoute(it) }
                            ?: rebuildRoute(routes.last())
                        previousRouteId = null
                    } else {
                        rebuildRoute(filteredRoutes.last())
                    }

                    binding.bottomSheetDialogRoutes.emptyDataPlaceholder.visibility =
                        View.GONE
                } else if (filteredRoutes.isEmpty()) {
                    pointsListAdapter.submitList(emptyList())

                    binding.bottomSheetDialogRoutes.emptyDataPlaceholder.visibility =
                        View.VISIBLE
                    binding.bottomSheetDialogRoutePoints.emptyDataPlaceholder.visibility =
                        View.VISIBLE
                }
            }
        }
    }

    //called from RoutesListAdapter when user clicked on item
    override fun onRouteItemClick(route: RouteModel) {
        rebuildRoute(route)
        focusedRoute = route
        routesDialogBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun rebuildRoute(route: RouteModel) {
        val pointsCoordinatesList = mutableListOf<Point>()
        focusedRoute = route

        if (this::routePointsJob.isInitialized) {
            routePointsJob.cancel()
        }

        routePointsJob = viewLifecycleOwner.lifecycleScope.launch {
            viewModelPrivate.getRoutePointsList(route.routeId)
                .distinctUntilChanged()
                .collect { pointsList ->
                    if (pointsList.isNotEmpty()) {
                        currentRoutePointsList =
                            pointsList.map { it.copy() } as MutableList<RoutePointModel>

                        currentRoutePointsList.forEach {
                            pointsCoordinatesList.add(Point.fromLngLat(it.x, it.y))
                        }

                        buildRouteFromList(currentRoutePointsList.map(::mapPrivateRoutePointModelToPoint))
                        fetchAnnotatedRoutePoints()
                        eraseCameraToPoint(
                            currentRoutePointsList[0].x,
                            currentRoutePointsList[0].y,
                            binding.mapView
                        )
                        focusedRoute.distance = getRouteDistance(pointsCoordinatesList)
                    }
                }
        }
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

    private fun fetchAnnotatedRoutePoints() {
        val annotatedPoints = mutableListOf<RoutePointModel>()
        val imageList = mutableListOf<ImageModel>()
        val startFlag: Int
        val finishFlag: Int

        if (theme.id() == 0) {
            startFlag = R.drawable.ic_start_flag_light
            finishFlag = R.drawable.ic_finish_flag_light
        } else {
            startFlag = R.drawable.ic_start_flag_dark
            finishFlag = R.drawable.ic_finish_flag_dark
        }

        pointAnnotationManager.deleteAll()
        binding.bottomSheetDialogRoutePoints.emptyDataPlaceholder.visibility =
            View.VISIBLE

        if (currentRoutePointsList.first().isRoutePoint) {
            addFlagAnnotationToMap(
                Point.fromLngLat(
                    currentRoutePointsList.first().x,
                    currentRoutePointsList.first().y,
                ),
                startFlag
            )
        } else {
            addFlagAnnotationToMap(
                Point.fromLngLat(
                    currentRoutePointsList.first().x,
                    currentRoutePointsList.first().y + 0.00005,
                ),
                startFlag
            )
        }

        if (currentRoutePointsList.last().isRoutePoint) {
            addFlagAnnotationToMap(
                Point.fromLngLat(
                    currentRoutePointsList.last().x,
                    currentRoutePointsList.last().y,
                ),
                finishFlag
            )
        } else {
            addFlagAnnotationToMap(
                Point.fromLngLat(
                    currentRoutePointsList.last().x,
                    currentRoutePointsList.last().y + 0.00005,
                ),
                finishFlag
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

    private fun addAnnotationToMap(point: RoutePointModel) {
        val pointIcon: Int = if (theme.id() == 0) {
            R.drawable.ic_pin_point_light
        } else {
            R.drawable.ic_pin_point_dark
        }

        activity?.applicationContext?.let {
            convertDrawableToBitmap(
                AppCompatResources.getDrawable(
                    it,
                    pointIcon
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

    private fun addWaypoint(point: RoutePointModel) {
        val flag: Int = if (theme.id() == 0) {
            R.drawable.ic_start_flag_light
        } else {
            R.drawable.ic_start_flag_dark
        }

        creatingRouteCoordinatesList.add(point)

        if (creatingRouteCoordinatesList.size == 1) {
            if (binding.pointTypeSwitchButton.isChecked) {
                addFlagAnnotationToMap(
                    Point.fromLngLat(
                        creatingRouteCoordinatesList[0].x,
                        creatingRouteCoordinatesList[0].y
                    ),
                    flag
                )
            } else {
                addFlagAnnotationToMap(
                    Point.fromLngLat(
                        creatingRouteCoordinatesList[0].x,
                        creatingRouteCoordinatesList[0].y + 0.00005
                    ),
                    flag
                )
            }
        } else if (creatingRouteCoordinatesList.size == 2) {
            isRouteSaveable.value = true
        }

        buildRoute()
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

    private fun setRoute(routes: List<DirectionsRoute>) {
        routes.map { RouteLine(it, null) }.let { routeLines ->
            routeLineApi.setRoutes(routeLines) { value ->
                mapboxMap.getStyle()?.apply {
                    routeLineView.renderRouteDrawData(this, value)
                }
            }
        }
    }

    private fun addEmptyAnnotationToMap(point: RoutePointModel) {
        val pointIcon: Int = if (theme.id() == 0) {
            R.drawable.ic_pin_point_light
        } else {
            R.drawable.ic_pin_point_dark
        }

        activity?.applicationContext?.let {
            convertDrawableToBitmap(
                AppCompatResources.getDrawable(
                    it,
                    pointIcon
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

    //called from RoutePointsListAdapter when user clicked on item
    override fun onPointItemClick(pointId: String) {
        currentRoutePointsList.find {
            it.pointId == pointId
        }?.let { pointPreview -> eraseCameraToPoint(pointPreview.x, pointPreview.y, binding.mapView) }

        routePointsDialogBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun loadAnnotatedPointData(annotation: PointAnnotation) {
        annotation.getData()?.asString?.let { pointId ->
            currentRoutePointsList.find { it.pointId == pointId }?.let { point ->
                eraseCameraToPoint(point.x, point.y, binding.mapView)
                preparePointDetailsDialog(annotation, point)
            }
        }
    }

    private fun preparePointDetailsDialog(
        pointAnnotation: PointAnnotation,
        point: RoutePointModel
    ) {
        binding.bottomSheetDialogPointDetails.apply {
            if (point.caption.isEmpty() && point.description.isEmpty() && point.tagList.isEmpty()) {
                emptyDataPlaceholder.visibility = View.VISIBLE
            } else {
                emptyDataPlaceholder.visibility = View.GONE
            }

            pointCaptionText.text = point.caption
            pointDescriptionText.text = point.description

            if (point.tagList.isEmpty()) {
                tagListTextView.text = ""
                tagListTextView.visibility = View.GONE
            } else {
                tagListTextView.text = point.tagList.joinToString(
                    ",",
                    "Tags: "
                ) { pointTagModel -> pointTagModel.name }
                tagListTextView.visibility = View.VISIBLE
            }


            pointImagesPreviewAdapter = ImagesPreviewAdapter {
                findNavController().navigate(
                    PrivateRoutesFragmentDirections.actionPrivateRoutesFragmentToPrivatePointImageDetails(
                        point.pointId,
                        pointImageLayoutManager.findFirstVisibleItemPosition()
                    )
                )
            }

            imageRecycler.apply {
                adapter = pointImagesPreviewAdapter
                layoutManager = pointImageLayoutManager
            }

            pointImagesPreviewAdapter.submitList(point.imageList)

            pointDetailsEditButton.setOnClickListener {
                if (isPublic) {
                    Toast.makeText(
                        requireContext(),
                        R.string.make_route_private_before_editing_it_point,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    findNavController().navigate(
                        PrivateRoutesFragmentDirections
                            .actionPrivateRoutesFragmentToPointDetailsFragment(
                                pointAnnotation.getData()?.asString!!
                            )
                    )
                }
            }

            pointDetailsDeleteButton.setOnClickListener {
                if (isPublic) {
                    Toast.makeText(
                        requireContext(),
                        R.string.make_route_private_before_deleting_it_point,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    binding.bottomSheetDialogRoutePoints.emptyDataPlaceholder.visibility = View.VISIBLE

                    if (currentRoutePointsList.size == 2) {
                        deleteRoute(focusedRoute)
                    } else {
                        viewModelPrivate.deletePoint(point)
                        pointAnnotationManager.delete(pointAnnotation)

                        binding.bottomSheetDialogRoutePoints.emptyDataPlaceholder.visibility =
                            View.GONE
                    }

                    pointDetailsDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
        }
    }

    private fun prepareRouteDetailsDialog(
        route: RouteModel
    ) {
        val imageList = mutableListOf<ImageModel>()
        isPublic = route.isPublic

        binding.bottomSheetDialogRouteDetails.apply {

            //is route data is empty
            if (route.name?.isEmpty() == true && route.description?.isEmpty() == true && route.tagsList.isEmpty()) {
                emptyDataPlaceholder.visibility = View.VISIBLE
            } else {
                emptyDataPlaceholder.visibility = View.GONE
            }

            routeDistance.text = getString(R.string.route_distance, route.distance)
            routeCaptionText.text = route.name
            routeDescriptionText.text = route.description

            if (route.tagsList.isEmpty()) {
                tagListTextView.text = ""
                tagListTextView.visibility = View.GONE
            } else {
                tagListTextView.text = route.tagsList.joinToString(
                    ",",
                    "Tags: "
                ) { pointTagModel -> pointTagModel.name }
                tagListTextView.visibility = View.VISIBLE
            }

            if (isPublic) {
                btnChangeRouteAccess.setImageResource(R.drawable.ic_lock)
            } else {
                btnChangeRouteAccess.setImageResource(R.drawable.ic_upload)
            }

            routeDetailsEditButton.setOnClickListener {
                if (isPublic) {
                    Toast.makeText(
                        requireContext(),
                        R.string.make_route_private_before_editing,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    findNavController().navigate(
                        PrivateRoutesFragmentDirections
                            .actionPrivateRoutesFragmentToRouteDetailsFragment(route.routeId)
                    )
                }
            }


            btnChangeRouteAccess.setOnClickListener {
                previousRouteId = route.routeId

                if (internetCheckCallback?.isInternetAvailable() == true) {
                    if (isPublic) {
                        viewModelPrivate.changeRouteAccess(route.routeId, false)
                        btnChangeRouteAccess.setImageResource(R.drawable.ic_upload)
                        isPublic = false
                    } else {
                        val user = FirebaseAuth.getInstance().currentUser

                        if (user != null) {
                            if (route.name.isNullOrEmpty() || route.description.isNullOrEmpty()) {
                                Toast.makeText(
                                    context,
                                    R.string.placeholder_fill_route_data,
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                btnChangeRouteAccess.setImageResource(R.drawable.ic_lock)
                                viewModelPrivate.changeRouteAccess(route.routeId, true)
                                isPublic = true
                            }
                        } else {
                            Toast.makeText(
                                context,
                                R.string.placeholder_sign_in_before_publishing_route,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        R.string.no_internet_connection,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            routeImagesPreviewAdapter = ImagesPreviewAdapter {
                findNavController().navigate(
                    PrivateRoutesFragmentDirections.actionPrivateRoutesFragmentToPrivateRouteImageDetails(
                        route.routeId,
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
                if (isPublic) {
                    Toast.makeText(
                        requireContext(),
                        R.string.make_route_private_before_deleting,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    deleteRoute(route)

                    if (internetCheckCallback?.isInternetAvailable() == true) {
                        viewLifecycleOwner.lifecycleScope.launch {
                            viewModelMain.deleteRemotePoints()
                            viewModelMain.deleteRemoteRoutes()
                        }
                    }

                    routeDetailsDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
        }
    }

    private fun deleteRoute(route: RouteModel) {
        resetCurrentRoute()
        viewModelPrivate.deleteRoute(route)
    }
}