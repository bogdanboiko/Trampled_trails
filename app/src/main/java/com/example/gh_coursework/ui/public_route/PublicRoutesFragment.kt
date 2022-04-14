package com.example.gh_coursework.ui.public_route

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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.gh_coursework.R
import com.example.gh_coursework.databinding.FragmentPrivateRouteBinding
import com.example.gh_coursework.databinding.FragmentPublicRouteBinding
import com.example.gh_coursework.ui.helper.convertDrawableToBitmap
import com.example.gh_coursework.ui.helper.createAnnotationPoint
import com.example.gh_coursework.ui.helper.createFlagAnnotationPoint
import com.example.gh_coursework.ui.private_point.PrivatePointsFragmentDirections
import com.example.gh_coursework.ui.public_route.adapter.*
import com.example.gh_coursework.ui.public_route.model.PublicRouteModel
import com.example.gh_coursework.ui.public_route.model.RoutePointModel
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
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@OptIn(ExperimentalPreviewMapboxNavigationAPI::class)
class PublicRoutesFragment :
    Fragment(R.layout.fragment_public_route),
    RoutesListAdapterCallback,
    RoutePointsListCallback {

    private lateinit var routeImagesPreviewAdapter: PublicImageAdapter
    private lateinit var pointImagesPreviewAdapter: PublicImageAdapter

    private lateinit var pointImageLayoutManager: LinearLayoutManager
    private lateinit var routeImageLayoutManager: LinearLayoutManager
    private lateinit var binding: FragmentPublicRouteBinding

    private val viewModelPublic: PublicRouteViewModel by viewModel()
    private val routesListAdapter = RoutesListAdapter(this as RoutesListAdapterCallback)
    private val pointsListAdapter = RoutePointsListAdapter(this as RoutePointsListCallback)

    private var currentRoutePointsList = mutableListOf<RoutePointModel>()
    private lateinit var focusedPublicRoute: PublicRouteModel
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
    private val navigationLocationProvider = NavigationLocationProvider()


    private val onAnnotatedPointClickEvent = OnPointAnnotationClickListener { annotation ->
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
        binding = FragmentPublicRouteBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configMap()
        configBottomNavBar()
        setUpBottomSheetsRecyclers()
        configImageRecyclers()
        configBottomSheetDialogs()
        initMapboxNavigation()
        initRouteLine()
        fetchRoutes()

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

    private fun configBottomNavBar() {
        binding.bottomNavigationView.menu.getItem(0).isChecked = true
        binding.bottomNavigationView.menu.getItem(2).setOnMenuItemClickListener {
            findNavController().popBackStack()
            return@setOnMenuItemClickListener true
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

    private fun configBottomSheetDialogs() {
        getRoutesDialog()
        getRoutePointsDialog()
        binding.bottomSheetDialogRoutes.emptyDataPlaceholder.visibility = View.GONE
        binding.bottomSheetDialogRoutePoints.emptyDataPlaceholder.text =
            resources.getText(R.string.public_route_point_placeholder)
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
            viewModelPublic.routeList
                .collect { route ->
                    routesListAdapter.submitData(route)
                }
        }
    }

    override fun onRouteItemClick(publicRoute: PublicRouteModel) {
        rebuildRoute(publicRoute)
        focusedPublicRoute = publicRoute
        routesDialogBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
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
                            currentRoutePointsList[0].y
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
        val routeLines = routes.map { RouteLine(it, null) }

        routeLineApi.setRoutes(routeLines) { value ->
            mapboxMap.getStyle()?.apply {
                routeLineView.renderRouteDrawData(this, value)
            }
        }
    }

    override fun onPointItemClick(pointId: String) {
        val pointPreview = currentRoutePointsList.find {
            it.pointId == pointId
        }

        pointPreview?.let { eraseCameraToPoint(pointPreview.x, pointPreview.y) }
        routePointsDialogBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun loadAnnotatedPointData(annotation: PointAnnotation) {
        annotation.getData()?.asString?.let { pointId ->
            val point = currentRoutePointsList.find { it.pointId == pointId }

            if (point != null) {
                preparePointDetailsDialog(annotation, point)
            }
        }
    }

    private fun preparePointDetailsDialog(
        pointAnnotation: PointAnnotation,
        point: RoutePointModel
    ) {
        binding.bottomSheetDialogPointDetails.apply {
            if (point.caption.isEmpty() && point.description.isEmpty()) {
                emptyDataPlaceholder.visibility = View.VISIBLE
            } else {
                pointCaptionText.text = point.caption
                pointDescriptionText.text = point.description
                emptyDataPlaceholder.visibility = View.GONE
            }

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

    private fun prepareRouteDetailsDialog(
        publicRoute: PublicRouteModel
    ) {
        binding.bottomSheetDialogRouteDetails.apply {
            if (publicRoute.name.isEmpty() && publicRoute.description.isEmpty() && publicRoute.tagsList.isEmpty()) {
                emptyDataPlaceholder.visibility = View.VISIBLE
            } else {
                routeCaptionText.text = publicRoute.name
                routeDescriptionText.text = publicRoute.description
                emptyDataPlaceholder.visibility = View.GONE
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

            routeImagesPreviewAdapter.submitList(publicRoute.imageList)
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