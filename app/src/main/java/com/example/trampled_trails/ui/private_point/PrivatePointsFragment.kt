package com.example.trampled_trails.ui.private_point

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeFragment
import com.example.trampled_trails.ui.data.MapState
import com.example.trampled_trails.R
import com.example.trampled_trails.databinding.FragmentPrivatePointsBinding
import com.example.trampled_trails.ui.helper.*
import com.example.trampled_trails.ui.homepage.GetSyncStateCallback
import com.example.trampled_trails.ui.homepage.data.SyncingProgressState
import com.example.trampled_trails.ui.point_details.model.PointTagModel
import com.example.trampled_trails.ui.private_image_details.adapter.ImagesPreviewAdapter
import com.example.trampled_trails.ui.private_point.adapter.PointsListAdapter
import com.example.trampled_trails.ui.private_point.adapter.PointsListCallback
import com.example.trampled_trails.ui.private_point.helper.configPrivatePointsFragmentBottomNavBar
import com.example.trampled_trails.ui.private_point.helper.syncPrivatePointsFragmentTheme
import com.example.trampled_trails.ui.private_point.model.PrivatePointDetailsModel
import com.example.trampled_trails.ui.private_point.tag_dialog.PointFilterByTagDialogFragment
import com.example.trampled_trails.ui.themes.MyAppTheme
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.JsonPrimitive
import com.mapbox.geojson.Point
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class PrivatePointsFragment : ThemeFragment(), PointsListCallback {

    private lateinit var binding: FragmentPrivatePointsBinding
    private lateinit var theme: MyAppTheme

    private lateinit var pointsFetchingJob: Job

    private lateinit var imagesPreviewAdapter: ImagesPreviewAdapter
    private lateinit var pointDetailsImagesLayoutManager: LinearLayoutManager
    private lateinit var pointsLayoutManager: LinearLayoutManager
    private val pointListAdapter = PointsListAdapter(this)

    private val viewModel: PointViewModel by viewModel()
    private var internetCheckCallback: InternetCheckCallback? = null

    private var pointCoordinates = emptyList<PrivatePointDetailsModel>()
    private var checkedTagList = emptyList<PointTagModel>()

    private lateinit var pointDetailsBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var pointsBottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private lateinit var mapboxMap: MapboxMap
    private lateinit var pointAnnotationManager: PointAnnotationManager
    private var mapState: MapState = MapState.PRESENTATION
        set(value) {
            when(value) {
                MapState.CREATOR -> mapModCreator()
                MapState.PRESENTATION -> mapModPresentation()
            }
            field = value
        }
    private var syncStateCallback: GetSyncStateCallback? = null

    private val addPointOnClick = OnMapClickListener { point ->
        pointAnnotationManager.annotations.find {
            return@find it.point.latitude() == point.latitude()
                    && it.point.longitude() == point.longitude()
        }.let { pointAnnotation ->
            if (pointAnnotation == null) {
                PrivatePointDetailsModel(
                    pointId = UUID.randomUUID().toString(),
                    x = point.longitude(),
                    y = point.latitude()
                ).also { newPoint -> viewModel.addPoint(newPoint)}
            }
        }

        binding.cancelButton.text = context?.resources?.getString(R.string.save_button_save)
        binding.cancelButton.icon =
            view?.context?.let { context ->
                AppCompatResources.getDrawable(
                    context,
                    R.drawable.ic_confirm
                )
            }

        return@OnMapClickListener true
    }

    private val onPointClickEvent = OnPointAnnotationClickListener { annotation ->
        getPointDetailsDialog(annotation)

        pointsBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        true
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        internetCheckCallback = context as? InternetCheckCallback
        syncStateCallback = context as? GetSyncStateCallback
    }

    override fun onDetach() {
        super.onDetach()

        internetCheckCallback = null
        syncStateCallback = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPrivatePointsBinding.inflate(inflater, container, false)

        binding.bottomSheetDialogPoints.root.layoutParams.height = resources.displayMetrics.heightPixels / 3

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configMap()
        configBottomNavBar()
        onNavigateToPrivateRoutesButtonClickListener()
        configMapModSwitcher()
        configSaveButton()
        configBottomSheetDialog()
        onNavigateToHomepageButtonClickListener()
        fetchPoints()

        viewLifecycleOwner.lifecycleScope.launch {
            syncStateCallback?.getStateSubscribeTo()?.collect { state ->
                when (state) {
                    is SyncingProgressState.Loading -> binding.progressBar.loadBackground.visibility =
                        View.VISIBLE
                    is SyncingProgressState.FinishedSync -> binding.progressBar.loadBackground.visibility =
                        View.GONE
                }
            }
        }

        setFragmentResultListener(PointFilterByTagDialogFragment.REQUEST_KEY) { key, bundle ->
            bundle.getParcelableArray("tags")?.toList()?.let { tagList ->
                if (tagList.isEmpty()) {
                    binding.bottomSheetDialogPoints.emptyDataPlaceholder.text =
                        context?.resources?.getString(R.string.placeholder_private_points_list_empty)
                }

                fetchPoints()
            }
        }

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    requireActivity().finishAffinity()
                }
            }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    //sync app theme
    override fun syncTheme(appTheme: AppTheme) {
        theme = appTheme as MyAppTheme

        syncPrivatePointsFragmentTheme(theme, binding, requireContext())
    }

    private fun configMap() {
        mapboxMap = binding.mapView.getMapboxMap().also {
            it.loadStyleUri(Style.MAPBOX_STREETS)
        }

        binding.mapView.compass.enabled = false

        pointAnnotationManager = binding.mapView.annotations.createPointAnnotationManager()
        pointAnnotationManager.addClickListener(onPointClickEvent)
    }

    private fun configBottomNavBar() {
        internetCheckCallback?.isInternetAvailable()?.let { isAvailable -> view?.let { view ->
            configPrivatePointsFragmentBottomNavBar(isAvailable, binding, requireContext(), view)
            }
        }
    }

    private fun onNavigateToPrivateRoutesButtonClickListener() {
        binding.mapRoutePointModSwitcher.setOnClickListener {
            findNavController().navigate(
                PrivatePointsFragmentDirections
                    .actionPrivatePointsFragmentToPrivateRoutesFragment()
            )
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun configMapModSwitcher() {
        binding.createButton.setOnClickListener {
            if (mapState == MapState.CREATOR) {
                executeClickAtPoint()
            } else {
                mapState = MapState.CREATOR

                binding.cancelButton.text = context?.resources?.getString(R.string.save_button_disable)
                binding.cancelButton.icon =
                    view?.context?.let { context ->
                        AppCompatResources.getDrawable(
                            context,
                            R.drawable.ic_close
                        )
                    }
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun mapModCreator() {
        with(binding) {
            groupButtonsItems.isVisible = false
            groupCreatorItems.isVisible = true

            createButton.setImageDrawable(
                context?.getDrawable(
                    R.drawable.ic_confirm
                )
            )
        }

        mapboxMap.addOnMapClickListener(addPointOnClick)
        pointAnnotationManager.removeClickListener(onPointClickEvent)

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun mapModPresentation() {
        with(binding) {
            groupButtonsItems.isVisible = true
            groupCreatorItems.isVisible = false

            createButton.setImageDrawable(
                context?.getDrawable(
                    R.drawable.ic_add
                )
            )
        }

        mapboxMap.removeOnMapClickListener(addPointOnClick)
        pointAnnotationManager.addClickListener(onPointClickEvent)
    }

    private fun configSaveButton() {
        binding.cancelButton.setOnClickListener {
            mapState = MapState.PRESENTATION
        }
    }

    //bottom sheets set up start
    private fun configBottomSheetDialog() {
        PagerSnapHelper().attachToRecyclerView(binding.pointDetailsBottomSheetDialogLayout.imageRecycler)

        pointDetailsImagesLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        pointDetailsBottomSheetBehavior = BottomSheetBehavior.from(binding.pointDetailsBottomSheetDialogLayout.pointBottomSheetDialog)
        pointDetailsBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        pointsLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        pointsBottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetDialogPoints.pointsBottomSheetDialog)
        pointsBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        getPointsDialog()
    }

    private fun getPointsDialog() {
        binding.bottomSheetDialogPoints.pointsRecyclerView.apply {
            adapter = pointListAdapter
            layoutManager = pointsLayoutManager
        }

        binding.getPointsList.setOnClickListener {
            pointDetailsBottomSheetBehavior.peekHeight = 0
            pointsBottomSheetBehavior.peekHeight = resources.displayMetrics.heightPixels / 3

            if (pointsBottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
                pointsBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        binding.bottomSheetDialogPoints.pointFilterByTagButton.setOnClickListener {
            findNavController().navigate(
                PrivatePointsFragmentDirections.actionPrivatePointsFragmentToPointFilterByTagsDialogFragment(
                    checkedTagList.toTypedArray()
                )
            )
        }
    }

    private fun getPointDetailsDialog(annotation: PointAnnotation) {
        loadPointDetailsData(annotation)

        pointsBottomSheetBehavior.peekHeight = 0
        pointDetailsBottomSheetBehavior.peekHeight = resources.displayMetrics.heightPixels / 3

        if (pointDetailsBottomSheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
            pointDetailsBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }
    //bottom sheets set up end

    private fun onNavigateToHomepageButtonClickListener() {
        binding.homepageButton.setOnClickListener {
            findNavController().navigate(
                PrivatePointsFragmentDirections
                    .actionPrivatePointsFragmentToHomepageFragment()
            )
        }
    }

    private fun fetchPoints() {
        if (this::pointsFetchingJob.isInitialized) {
            pointsFetchingJob.cancel()
        }

        pointsFetchingJob = viewLifecycleOwner.lifecycleScope.launch {
            viewModel.points.collect { points ->
                var filteredPoints = mutableListOf<PrivatePointDetailsModel>()

                if (checkedTagList.isNotEmpty()) {
                    points.forEach { point ->
                        checkedTagList.forEach tags@{
                            if (point.tagList.contains(it)) {
                                filteredPoints.add(point)
                                return@tags
                            }
                        }
                    }

                    if (filteredPoints.isEmpty()) {
                        binding.bottomSheetDialogPoints.emptyDataPlaceholder.text =
                            context?.resources?.getString(R.string.placeholder_private_no_point_found_by_tags)
                    }
                } else {
                    filteredPoints = points.toMutableList()
                }

                pointAnnotationManager.deleteAll()
                filteredPoints.forEach {
                    addAnnotationToMap(it)
                }

                if (filteredPoints.isNotEmpty()) {
                    binding.bottomSheetDialogPoints.emptyDataPlaceholder.visibility = View.GONE
                } else {
                    binding.bottomSheetDialogPoints.emptyDataPlaceholder.visibility = View.VISIBLE
                }

                pointCoordinates = filteredPoints
                pointListAdapter.submitList(filteredPoints)
            }
        }
    }

    private fun executeClickAtPoint() {
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

    private fun addAnnotationToMap(point: PrivatePointDetailsModel) {
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

    private fun loadPointDetailsData(annotation: PointAnnotation) {
        annotation.getData()?.asString?.let { pointId ->
            pointCoordinates.find { it.pointId == pointId }?.let { point ->
                eraseCameraToPoint(point.x, point.y, binding.mapView)
                preparePointDetailsDialog(annotation, point)
            }
        }
    }

    private fun preparePointDetailsDialog(
        pointAnnotation: PointAnnotation,
        point: PrivatePointDetailsModel
    ) {
        binding.pointDetailsBottomSheetDialogLayout.apply {
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


        imagesPreviewAdapter = ImagesPreviewAdapter {
            findNavController().navigate(
                PrivatePointsFragmentDirections.actionPrivatePointsFragmentToPrivateImageDetails(
                    point.pointId,
                    this@PrivatePointsFragment.pointDetailsImagesLayoutManager.findFirstVisibleItemPosition()
                )
            )
        }

        imageRecycler.apply {
            adapter = imagesPreviewAdapter
            layoutManager = this@PrivatePointsFragment.pointDetailsImagesLayoutManager
        }

        imagesPreviewAdapter.submitList(point.imageList)

        pointDetailsEditButton.setOnClickListener {
            if (!point.routeId.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    R.string.can_not_edit_route_point,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                findNavController().navigate(
                    PrivatePointsFragmentDirections
                        .actionPrivatePointsFragmentToPointDetailsFragment(point.pointId)
                )
            }
        }

        pointDetailsDeleteButton.setOnClickListener {
            if (!point.routeId.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    R.string.can_not_delete_route_point,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                viewModel.deletePoint(point)

                pointAnnotationManager.delete(pointAnnotation)
                pointDetailsBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }
}

    //called from PointsListAdapter when user clicked on item
    override fun onPointItemClick(pointDetails: PrivatePointDetailsModel) {
        eraseCameraToPoint(pointDetails.x, pointDetails.y, binding.mapView)
        pointsBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }
}