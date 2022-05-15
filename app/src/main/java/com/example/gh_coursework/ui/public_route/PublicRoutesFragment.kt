package com.example.gh_coursework.ui.public_route

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeFragment
import com.example.gh_coursework.R
import com.example.gh_coursework.databinding.FragmentPublicRouteBinding
import com.example.gh_coursework.ui.helper.*
import com.example.gh_coursework.ui.public_route.adapter.*
import com.example.gh_coursework.ui.public_route.helper.configPublicFragmentBottomNavBar
import com.example.gh_coursework.ui.public_route.helper.syncPublicFragmentTheme
import com.example.gh_coursework.ui.public_route.model.PublicRouteModel
import com.example.gh_coursework.ui.public_route.model.RoutePointModel
import com.example.gh_coursework.ui.public_route.tag_dialog.PublicRouteFilterByTagFragment
import com.example.gh_coursework.ui.themes.MyAppTheme
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel

@OptIn(ExperimentalPreviewMapboxNavigationAPI::class)
class PublicRoutesFragment :
    ThemeFragment(),
    RoutesListAdapterCallback,
    RoutePointsListCallback {

    private lateinit var binding: FragmentPublicRouteBinding
    private lateinit var theme: MyAppTheme

    private lateinit var routePointsJob: Job
    private lateinit var routesFetchingJob: Job
    private lateinit var routeImagesPreviewAdapter: PublicImageAdapter
    private lateinit var pointImagesPreviewAdapter: PublicImageAdapter
    private lateinit var pointImageLayoutManager: LinearLayoutManager
    private lateinit var routeImageLayoutManager: LinearLayoutManager

    private val viewModelPublic: PublicRouteViewModel by viewModel()
    private val arguments by navArgs<PublicRoutesFragmentArgs>()
    private var internetCheckCallback: InternetCheckCallback? = null
    private var getUserIdCallback: GetUserIdCallback? = null

    private val routesListAdapter = RoutesListAdapter(this as RoutesListAdapterCallback)
    private val pointsListAdapter = RoutePointsListAdapter(this as RoutePointsListCallback)

    private var tagsFilter = emptyList<String>()
    private var favourites = mutableListOf<String>()
    private var isRouteFavourite = false
    private var isSortedByFavourites = false
    private var currentRoutePointsList = mutableListOf<RoutePointModel>()
    private lateinit var focusedPublicRoute: PublicRouteModel

    private lateinit var routesDialogBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var routePointsDialogBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var routeDetailsDialogBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var pointDetailsDialogBehavior: BottomSheetBehavior<ConstraintLayout>

    private lateinit var mapboxNavigation: MapboxNavigation
    private lateinit var mapboxMap: MapboxMap
    private lateinit var routeLineApi: MapboxRouteLineApi
    private lateinit var routeLineView: MapboxRouteLineView
    private lateinit var pointAnnotationManager: PointAnnotationManager
    private val navigationLocationProvider = NavigationLocationProvider()

    private val onAnnotatedPointClickEvent = OnPointAnnotationClickListener { annotation ->
        if (internetCheckCallback?.isInternetAvailable() == true) {
            if (annotation.getData()?.isJsonNull == false) {
                getPointDetailsDialog(annotation)
            } else if (annotation.getData()?.isJsonNull == true) {
                getRouteDetailsDialog()
            }
        } else {
            Toast.makeText(requireContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT)
                .show()
        }

        true
    }

    private val routesObserver = RoutesObserver { routeUpdateResult ->
        if (routeUpdateResult.routes.isNotEmpty()) {
            // generate route geometries and render them
            routeUpdateResult.routes.map { RouteLine(it, null) }.let { routeLines ->
                routeLineApi.setRoutes(routeLines) { value ->
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
                    eraseCameraToPoint(enhancedLocation.longitude, enhancedLocation.latitude, binding.mapView)
                }
            }
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentPublicRouteBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configMap()
        configBottomNavBar()
        fetchSavedPublicRoutes()
        setUpBottomSheetsRecyclers()
        configImageRecyclers()
        configBottomSheetDialogs()
        onNavigateToHomepageButtonClickListener()
        initMapboxNavigation()
        initRouteLine()

        if (this::focusedPublicRoute.isInitialized && internetCheckCallback?.isInternetAvailable() == true) {
            rebuildRoute(focusedPublicRoute)
        }

        fetchRoutes()

        setFragmentResultListener(PublicRouteFilterByTagFragment.REQUEST_KEY) { _, bundle ->
            bundle.getStringArray("tags")?.toList()?.let { tagsList ->
                if (tagsList.isEmpty()) {
                    binding.bottomSheetDialogRoutes.emptyDataPlaceholder.text =
                        context?.resources?.getString(R.string.placeholder_private_routes_empty_list)
                }

                fetchRoutes()
            }
        }

        mapboxNavigation.startTripSession(withForegroundService = false)
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

    //sync app theme
    override fun syncTheme(appTheme: AppTheme) {
        theme = appTheme as MyAppTheme
        syncPublicFragmentTheme(theme, binding, requireContext())
    }

    private fun configBottomNavBar() {
        view?.let { configPublicFragmentBottomNavBar(arguments, binding, it) }
    }

    private fun fetchSavedPublicRoutes() {
        if (internetCheckCallback?.isInternetAvailable() == true) {
            viewLifecycleOwner.lifecycleScope.launch {
                favourites = getUserIdCallback?.let {
                        viewModelPublic.getFavouriteRoutes(it.getUserId()).first()
                    } as MutableList<String>
            }
        } else {
            Toast.makeText(requireContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
        }
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

    private fun setUpBottomSheetsRecyclers() {
        binding.bottomSheetDialogRoutes.routesRecyclerView.apply {
            adapter = routesListAdapter
        }

        binding.bottomSheetDialogRoutePoints.routePointsRecyclerView.apply {
            adapter = pointsListAdapter
        }
    }

    //bottom sheets config start
    private fun configBottomSheetDialogs() {
        getRoutesDialog()
        getRoutePointsDialog()

        binding.bottomSheetDialogRoutes.emptyDataPlaceholder.visibility = View.GONE
        binding.bottomSheetDialogRoutePoints.emptyDataPlaceholder.text =
            resources.getText(R.string.placeholder_public_route_points)
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

    @SuppressLint("ResourceAsColor")
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

            if (FirebaseAuth.getInstance().currentUser != null) {
                binding.bottomSheetDialogRoutes.routeFilterByFavouriteButton.visibility =
                    View.VISIBLE

                binding.bottomSheetDialogRoutes.routeFilterByFavouriteButton.setOnClickListener {
                    if (isSortedByFavourites) {
                        binding.bottomSheetDialogRoutes.emptyDataPlaceholder.visibility = View.GONE
                        binding.bottomSheetDialogRoutes.routeFilterByTagButton.visibility =
                            View.VISIBLE
                        fetchRoutes()
                        binding.bottomSheetDialogRoutes.routeFilterByFavouriteButton.imageTintList =
                            ColorStateList.valueOf(R.color.black)
                        isSortedByFavourites = false
                    } else {
                        binding.bottomSheetDialogRoutes.routeFilterByTagButton.visibility =
                            View.GONE
                        fetchFavouriteRoutes()
                        binding.bottomSheetDialogRoutes.routeFilterByFavouriteButton.imageTintList =
                            ColorStateList.valueOf(R.color.yellow_dark)
                        isSortedByFavourites = true
                    }
                }
            } else {
                binding.bottomSheetDialogRoutes.routeFilterByFavouriteButton.visibility = View.GONE
            }
        }

        binding.bottomSheetDialogRoutes.routeFilterByTagButton.setOnClickListener {
            findNavController().navigate(
                PublicRoutesFragmentDirections.actionPublicRouteFragmentToPublicRouteFilterByTagsDialogFragment(
                    tagsFilter.toTypedArray()
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
                routePointsDialogBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            } else {
                routePointsDialogBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    private fun getRouteDetailsDialog() {
        prepareRouteDetailsDialog(focusedPublicRoute)

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
        loadAnnotatedPointData(annotation)

        routesDialogBehavior.peekHeight = 0
        routePointsDialogBehavior.peekHeight = 0
        routeDetailsDialogBehavior.peekHeight = 0

        routesDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        routePointsDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        routeDetailsDialogBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        pointDetailsDialogBehavior.peekHeight = resources.displayMetrics.heightPixels / 3

        if (pointDetailsDialogBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
            loadAnnotatedPointData(annotation)
            pointDetailsDialogBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            loadAnnotatedPointData(annotation)
            pointDetailsDialogBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }
    //bottom sheets config end

    private fun onNavigateToHomepageButtonClickListener() {
        binding.homepageButton.setOnClickListener {
            findNavController().navigate(
                PublicRoutesFragmentDirections.actionPublicRouteFragmentToHomepageFragment()
            )
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
        if (internetCheckCallback?.isInternetAvailable() == true) {
            if (this::routesFetchingJob.isInitialized) {
                routesFetchingJob.cancel()
            }

            routesFetchingJob = viewLifecycleOwner.lifecycleScope.launch {
                viewModelPublic.fetchTaggedRoutes(tagsFilter).collect { route ->
                    routesListAdapter.submitData(route)
                }
            }
        } else {
            Toast.makeText(requireContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun fetchFavouriteRoutes() {
        if (internetCheckCallback?.isInternetAvailable() == true) {
            if (this::routesFetchingJob.isInitialized) {
                routesFetchingJob.cancel()
            }

            routesFetchingJob = viewLifecycleOwner.lifecycleScope.launch {
                getUserIdCallback?.getUserId()
                    ?.let { userId ->
                        viewModelPublic.getFavouriteRoutes(userId).collect {
                            if (it.isNotEmpty()) {
                                binding.bottomSheetDialogRoutes.emptyDataPlaceholder.visibility =
                                    View.GONE
                                viewModelPublic.fetchFavouriteRoutes(it).collect { route ->
                                    routesListAdapter.submitData(route)
                                }
                            } else {
                                binding.bottomSheetDialogRoutes.emptyDataPlaceholder.visibility =
                                    View.VISIBLE
                                binding.bottomSheetDialogRoutes.emptyDataPlaceholder.text =
                                    resources.getText(R.string.placeholder_public_favourite_routes_not_found)
                                binding.bottomSheetDialogRoutes.routeFilterByTagButton.visibility =
                                    View.GONE
                                routesListAdapter.submitData(PagingData.empty())
                            }
                        }
                    }
            }

        } else {
            Toast.makeText(requireContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT)
                .show()
        }
    }

    //called from RoutesListAdapter when user clicked on item
    override fun onRouteItemClick(publicRoute: PublicRouteModel) {
        if (internetCheckCallback?.isInternetAvailable() == true) {
            rebuildRoute(publicRoute)
            focusedPublicRoute = publicRoute
            routesDialogBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            Toast.makeText(requireContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun rebuildRoute(publicRoute: PublicRouteModel) {
        focusedPublicRoute = publicRoute

        if (this::routePointsJob.isInitialized) {
            routePointsJob.cancel()
        }

        routePointsJob = viewLifecycleOwner.lifecycleScope.launch {
            viewModelPublic.fetchRoutePoints(publicRoute.routeId)
                .collect { pointsList ->
                    if (pointsList.isNotEmpty()) {
                        currentRoutePointsList =
                            pointsList.map { it.copy() } as MutableList<RoutePointModel>

                        buildRouteFromList(currentRoutePointsList.map {
                            Point.fromLngLat(
                                it.x,
                                it.y
                            )
                        })
                        fetchAnnotatedRoutePoints()
                        eraseCameraToPoint(
                            currentRoutePointsList[0].x,
                            currentRoutePointsList[0].y,
                            binding.mapView
                        )
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
        val imageList = mutableListOf<String>()
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
        if (theme.id() == 0) {
            R.drawable.ic_pin_point_light
        } else {
            R.drawable.ic_pin_point_dark
        }.let { pointIcon ->
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

    private fun setRoute(routes: List<DirectionsRoute>) {
        routes.map { RouteLine(it, null) }.let { routeLines ->
            routeLineApi.setRoutes(routeLines) { value ->
                mapboxMap.getStyle()?.apply {
                    routeLineView.renderRouteDrawData(this, value)
                }
            }
        }
    }

    //called from RoutePointsListAdapter when user clicked on item
    override fun onPointItemClick(pointId: String) {
        if (internetCheckCallback?.isInternetAvailable() == true) {
            currentRoutePointsList.find { it.pointId == pointId }?.let { pointPreview ->
                eraseCameraToPoint(pointPreview.x, pointPreview.y, binding.mapView)
                routePointsDialogBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        } else {
            Toast.makeText(requireContext(), R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadAnnotatedPointData(annotation: PointAnnotation) {
        annotation.getData()?.asString?.let { pointId ->
            currentRoutePointsList.find { it.pointId == pointId }?.let { point ->
                eraseCameraToPoint(point.x, point.y, binding.mapView)
                preparePointDetailsDialog(point)
            }
        }
    }

    private fun preparePointDetailsDialog(point: RoutePointModel) {

        binding.bottomSheetDialogPointDetails.apply {
            if (point.caption.isEmpty() && point.description.isEmpty()) {
                emptyDataPlaceholder.visibility = View.VISIBLE
            } else {
                emptyDataPlaceholder.visibility = View.GONE
            }

            pointCaptionText.text = point.caption
            pointDescriptionText.text = point.description

            pointImagesPreviewAdapter = PublicImageAdapter {
                findNavController().navigate(
                    PublicRoutesFragmentDirections.actionPublicRouteFragmentToPublicImageFragment(
                        point.imageList.toTypedArray(),
                        pointImageLayoutManager.findFirstVisibleItemPosition()
                    )
                )
            }

            imageRecycler.apply {
                adapter = pointImagesPreviewAdapter
                layoutManager = pointImageLayoutManager
            }

            pointImagesPreviewAdapter.submitList(point.imageList)
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun prepareRouteDetailsDialog(
        publicRoute: PublicRouteModel
    ) {
        binding.bottomSheetDialogRouteDetails.apply {
            isRouteFavourite = favourites.contains(publicRoute.routeId)

            if (FirebaseAuth.getInstance().currentUser == null) {
                routeDetailsAddToFavouriteButton.visibility = View.GONE
            } else {
                routeDetailsAddToFavouriteButton.visibility = View.VISIBLE
            }

            if (isRouteFavourite) {
                routeDetailsAddToFavouriteButton.imageTintList =
                    ColorStateList.valueOf(R.color.yellow_dark)
            } else {
                routeDetailsAddToFavouriteButton.imageTintList =
                    ColorStateList.valueOf(R.color.black)
            }

            if (internetCheckCallback?.isInternetAvailable() == true) {
                routeDetailsAddToFavouriteButton.setOnClickListener {
                    if (isRouteFavourite) {
                        viewModelPublic.removeRouteFromFavourites(
                            publicRoute.routeId,
                            getUserIdCallback?.getUserId().toString()
                        )
                        routeDetailsAddToFavouriteButton.imageTintList =
                            ColorStateList.valueOf(R.color.black)
                        isRouteFavourite = false
                    } else {
                        viewModelPublic.addRouteToFavourites(
                            publicRoute.routeId,
                            getUserIdCallback?.getUserId().toString()
                        )
                        routeDetailsAddToFavouriteButton.imageTintList =
                            ColorStateList.valueOf(R.color.yellow_dark)
                        isRouteFavourite = true
                    }

                    runBlocking {
                        fetchSavedPublicRoutes()
                        if (isSortedByFavourites) {
                            delay(500)
                            fetchFavouriteRoutes()
                        }
                    }
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    R.string.no_internet_connection,
                    Toast.LENGTH_SHORT
                ).show()
            }

            emptyDataPlaceholder.visibility = View.GONE
            routeCaptionText.text = publicRoute.name
            routeDescriptionText.text = publicRoute.description

            if (publicRoute.tagsList.isEmpty()) {
                tagListTextView.text = ""
                tagListTextView.visibility = View.GONE
            } else {
                tagListTextView.text = publicRoute.tagsList.joinToString(
                    ",",
                    "Tags: "
                )
                tagListTextView.visibility = View.VISIBLE
            }

            routeImagesPreviewAdapter = PublicImageAdapter {
                findNavController().navigate(
                    PublicRoutesFragmentDirections.actionPublicRouteFragmentToPublicImageFragment(
                        publicRoute.imageList.toTypedArray(),
                        pointImageLayoutManager.findFirstVisibleItemPosition()
                    )
                )
            }

            imageRecycler.apply {
                adapter = routeImagesPreviewAdapter
                layoutManager = routeImageLayoutManager
            }

            val imageList = mutableListOf<String>()
            imageList.addAll(publicRoute.imageList)
            currentRoutePointsList.forEach {
                imageList.addAll(it.imageList)
            }

            routeImagesPreviewAdapter.submitList(publicRoute.imageList)
        }
    }
}