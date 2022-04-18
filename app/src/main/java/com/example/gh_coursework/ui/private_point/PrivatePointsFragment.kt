package com.example.gh_coursework.ui.private_point

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.gh_coursework.MapState
import com.example.gh_coursework.R
import com.example.gh_coursework.databinding.FragmentPrivatePointsBinding
import com.example.gh_coursework.ui.adapter.ImagesPreviewAdapter
import com.example.gh_coursework.ui.helper.convertDrawableToBitmap
import com.example.gh_coursework.ui.helper.createAnnotationPoint
import com.example.gh_coursework.ui.helper.createOnMapClickEvent
import com.example.gh_coursework.ui.point_details.model.PointTagModel
import com.example.gh_coursework.ui.private_point.adapter.PointsListAdapter
import com.example.gh_coursework.ui.private_point.adapter.PointsListCallback
import com.example.gh_coursework.ui.private_point.model.PrivatePointDetailsModel
import com.example.gh_coursework.ui.private_point.model.PrivatePointModel
import com.example.gh_coursework.ui.private_point.tag_dialog.PointFilterByTagDialogFragment
import com.example.gh_coursework.ui.private_route.model.RouteModel
import com.example.gh_coursework.ui.route_details.model.RouteTagModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.JsonPrimitive
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.animation.camera
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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class PrivatePointsFragment : Fragment(R.layout.fragment_private_points), PointsListCallback {
    private lateinit var pointsJob: Job
    private lateinit var imagesPreviewAdapter: ImagesPreviewAdapter
    private lateinit var pointDetailsImagesLayoutManager: LinearLayoutManager
    private lateinit var pointsLayoutManager: LinearLayoutManager
    private val viewModel: PointViewModel by viewModel()
    private var pointCoordinates = emptyList<PrivatePointDetailsModel>()
    private val pointListAdapter = PointsListAdapter(this)
    private lateinit var mapboxMap: MapboxMap
    private lateinit var binding: FragmentPrivatePointsBinding
    private var checkedTagList = emptyList<PointTagModel>()
    private lateinit var pointDetailsSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private lateinit var pointListBottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private var mapState: MapState = MapState.PRESENTATION
    private lateinit var pointAnnotationManager: PointAnnotationManager

    private val onMapClickListener = OnMapClickListener { point ->
        val result = pointAnnotationManager.annotations.find {
            return@find it.point.latitude() == point.latitude()
                    && it.point.longitude() == point.longitude()
        }

        if (result == null) {
            val newPoint = PrivatePointModel(null, point.longitude(), point.latitude(), false)
            viewModel.addPoint(newPoint)
        }

        return@OnMapClickListener true
    }

    private val onPointClickEvent = OnPointAnnotationClickListener { annotation ->
        viewLifecycleOwner.lifecycleScope.launch {

            annotation.getData()?.asLong?.let { pointId ->
                pointCoordinates.find { it.pointId == pointId }
                    ?.let { prepareDetailsDialog(annotation, it) }
            }
        }

        pointDetailsSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        eraseCameraToPoint(annotation.point.longitude(), annotation.point.latitude())

        true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPrivatePointsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configMap()
        configBottomNavBar()
        configMapSwitcherButton()
        configMapModSwitcher()
        configCancelButton()
        configPointsButton()
        configBottomSheetDialog()
        onNavigateToHomepageButtonClickListener()
        fetchPoints()

        setFragmentResultListener(PointFilterByTagDialogFragment.REQUEST_KEY) { key, bundle ->
            val tagArray = bundle.getParcelableArray("tags")
            if (tagArray != null) {
                checkedTagList = tagArray.toList() as List<PointTagModel>

                if (tagArray.isEmpty()) {
                    binding.bottomSheetDialogPoints.emptyDataPlaceholder.text =
                        context?.resources?.getString(R.string.private_no_point_placeholder)
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

    private fun configBottomNavBar() {
        binding.bottomNavigationView.menu.getItem(2).isChecked = true
        binding.bottomNavigationView.menu.getItem(0).setOnMenuItemClickListener {
            findNavController().navigate(
                PrivatePointsFragmentDirections.actionPrivatePointsFragmentToPublicRoutesFragment()
            )

            return@setOnMenuItemClickListener true
        }
    }

    private fun configMap() {
        mapboxMap = binding.mapView.getMapboxMap().also {
            it.loadStyleUri(Style.MAPBOX_STREETS)
        }

        binding.mapView.compass.enabled = false

        pointAnnotationManager = binding.mapView.annotations.createPointAnnotationManager()
        pointAnnotationManager.addClickListener(onPointClickEvent)
    }

    private fun configMapSwitcherButton() {
        binding.mapRoutePointModSwitcher.setOnClickListener {
            findNavController().navigate(
                PrivatePointsFragmentDirections
                    .actionPrivatePointsFragmentToPrivateRoutesFragment()
            )
        }
    }

    private fun configBottomSheetDialog() {
        PagerSnapHelper().attachToRecyclerView(binding.pointDetailsBottomSheetDialogLayout.imageRecycler)
        pointDetailsImagesLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        pointDetailsSheetBehavior =
            BottomSheetBehavior.from(binding.pointDetailsBottomSheetDialogLayout.pointBottomSheetDialog)

        pointDetailsSheetBehavior.peekHeight = resources.displayMetrics.heightPixels / 3
        pointDetailsSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        pointsLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        pointListBottomSheetBehavior =
            BottomSheetBehavior.from(binding.bottomSheetDialogPoints.pointsBottomSheetDialog)
        pointListBottomSheetBehavior.peekHeight = resources.displayMetrics.heightPixels / 3
        pointListBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        binding.bottomSheetDialogPoints.pointsRecyclerView.apply {
            adapter = pointListAdapter
            layoutManager = pointsLayoutManager
        }

        binding.bottomSheetDialogPoints.pointFilterByTagButton.setOnClickListener {
            findNavController().navigate(
                PrivatePointsFragmentDirections.actionPrivatePointsFragmentToPointFilterByTagsDialogFragment(
                    checkedTagList.toTypedArray()
                )
            )
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun configMapModSwitcher() {
        binding.fab.setOnClickListener {
            if (mapState == MapState.CREATOR) {
                executeClickAtPoint()
            } else {
                with(binding) {
                    centralPointer.visibility = View.VISIBLE
                    cancelButton.visibility = View.VISIBLE
                    fab.setImageDrawable(
                        context?.getDrawable(
                            R.drawable.ic_confirm
                        )
                    )
                }

                mapboxMap.addOnMapClickListener(onMapClickListener)
                pointAnnotationManager.removeClickListener(onPointClickEvent)
                mapState = MapState.CREATOR
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun configCancelButton() {
        binding.cancelButton.setOnClickListener {
            with(binding) {
                centralPointer.visibility = View.INVISIBLE
                cancelButton.visibility = View.INVISIBLE
                fab.setImageDrawable(
                    context?.getDrawable(
                        R.drawable.ic_add
                    )
                )
            }

            mapboxMap.removeOnMapClickListener(onMapClickListener)
            pointAnnotationManager.addClickListener(onPointClickEvent)
            mapState = MapState.PRESENTATION
        }
    }

    private fun configPointsButton() {
        binding.getPointsList.setOnClickListener {
            pointListBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun onNavigateToHomepageButtonClickListener() {
        binding.homepageButton.setOnClickListener {
            findNavController().navigate(
                PrivatePointsFragmentDirections
                    .actionPrivatePointsFragmentToHomepageFragment()
            )
        }
    }

    private fun fetchPoints() {
        if (this::pointsJob.isInitialized) {
            pointsJob.cancel()
        }

        pointAnnotationManager.deleteAll()
        pointsJob = viewLifecycleOwner.lifecycleScope.launch {
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
                            context?.resources?.getString(R.string.private_no_point_found_by_tags_placeholder)
                    }
                } else {
                    filteredPoints = points.toMutableList()
                }

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
        val clickEvent = createOnMapClickEvent(
            Pair(
                resources.displayMetrics.widthPixels / 2,
                resources.displayMetrics.heightPixels / 2
            )
        )
        binding.mapView.dispatchTouchEvent(clickEvent.first)
        binding.mapView.dispatchTouchEvent(clickEvent.second)
    }

    private fun addAnnotationToMap(point: PrivatePointDetailsModel) {
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

    private fun prepareDetailsDialog(
        pointAnnotation: PointAnnotation,
        details: PrivatePointDetailsModel
    ) {
        binding.pointDetailsBottomSheetDialogLayout.apply {
            pointCaptionText.text = details.caption
            pointDescriptionText.text = details.description
            if (details.tagList.isNotEmpty()) {
                tagListTextView.text = details.tagList.joinToString(
                    ",",
                    "Tags: "
                ) { pointTagModel -> pointTagModel.name }
            } else {
                tagListTextView.text = ""
            }

            imagesPreviewAdapter = ImagesPreviewAdapter {
                findNavController().navigate(
                    PrivatePointsFragmentDirections.actionPrivatePointsFragmentToPrivateImageDetails(
                        details.pointId,
                        this@PrivatePointsFragment.pointDetailsImagesLayoutManager.findFirstVisibleItemPosition()
                    )
                )
            }

            imageRecycler.apply {
                adapter = imagesPreviewAdapter
                layoutManager = this@PrivatePointsFragment.pointDetailsImagesLayoutManager
            }

            imagesPreviewAdapter.submitList(details.imageList)

            if (details.caption.isEmpty() && details.description.isEmpty()) {
                emptyDataPlaceholder.visibility = View.VISIBLE
            } else {
                emptyDataPlaceholder.visibility = View.INVISIBLE
            }

            pointDetailsEditButton.setOnClickListener {
                findNavController().navigate(
                    PrivatePointsFragmentDirections
                        .actionPrivatePointsFragmentToPointDetailsFragment(details.pointId)
                )
            }

            pointDetailsDeleteButton.setOnClickListener {
                viewModel.deletePoint(details.pointId)

                pointAnnotationManager.delete(pointAnnotation)
                pointDetailsSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    override fun onPointItemClick(pointDetails: PrivatePointDetailsModel) {
        eraseCameraToPoint(pointDetails.x, pointDetails.y)
        pointListBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
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