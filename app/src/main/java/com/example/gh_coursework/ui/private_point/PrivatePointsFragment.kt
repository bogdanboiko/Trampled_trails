package com.example.gh_coursework.ui.private_point

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.gh_coursework.MapState
import com.example.gh_coursework.R
import com.example.gh_coursework.databinding.FragmentPrivatePointsBinding
import com.example.gh_coursework.databinding.ItemAnnotationViewBinding
import com.example.gh_coursework.ui.helper.convertDrawableToBitmap
import com.example.gh_coursework.ui.helper.createOnMapClickEvent
import com.example.gh_coursework.ui.private_point.model.PrivatePointDetailsPreviewModel
import com.example.gh_coursework.ui.private_point.model.PrivatePointModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.JsonPrimitive
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.ViewAnnotationAnchor
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.*
import com.mapbox.maps.plugin.gestures.OnMapClickListener
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.gestures.removeOnMapClickListener
import com.mapbox.maps.viewannotation.ViewAnnotationManager
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class PrivatePointsFragment : Fragment(R.layout.fragment_private_points) {
    private val viewModel: PointViewModel by viewModel()
    private var pointCoordinates = emptyList<PrivatePointModel>()
    private lateinit var viewAnnotationManager: ViewAnnotationManager
    private lateinit var mapboxMap: MapboxMap
    private lateinit var binding: FragmentPrivatePointsBinding
    private lateinit var sheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var mapState: MapState = MapState.PRESENTATION
    private lateinit var pointAnnotationManager: PointAnnotationManager
    private lateinit var center: Pair<Float, Float>

    private val onMapClickListener = OnMapClickListener { point ->
        val newPoint = PrivatePointModel(null, point.longitude(), point.latitude(), false)
        viewModel.addPoint(newPoint)
        return@OnMapClickListener true
    }

    private val onPointClickEvent = OnPointAnnotationClickListener  { annotation ->
        viewLifecycleOwner.lifecycleScope.launch {

            annotation.getData()?.asInt?.let { pointId ->
                viewModel.getPointDetailsPreview(pointId).collect { details ->
                    prepareDetailsDialog(annotation, details)
                }
            }
        }
        if (sheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
            loadPointData(annotation)
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            loadPointData(annotation)
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.mapView.camera.easeTo(
            CameraOptions.Builder()
                .center(Point.fromLngLat(annotation.point.longitude(), annotation.point.latitude()))
                .zoom(12.0)
                .build()
        )

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
        configMapSwitcherButton()
        configBottomSheetDialog()
        configMapModSwitcher()
        configCancelButton()
        configBottomSheetDialog()
        fetchPoints()
        view.viewTreeObserver?.addOnGlobalLayoutListener {
            center = Pair(view.width / 2f, view.height / 2f)
        }
    }

    private fun configMap() {
        mapboxMap = binding.mapView.getMapboxMap().also {
            viewAnnotationManager = binding.mapView.viewAnnotationManager
            it.loadStyleUri(Style.MAPBOX_STREETS)
        }

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
        sheetBehavior =
            BottomSheetBehavior.from(binding.bottomSheetDialogLayout.pointBottomSheetDialog)

        sheetBehavior.peekHeight = resources.displayMetrics.heightPixels / 2
        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
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

    private fun executeClickAtPoint() {
        val clickEvent = createOnMapClickEvent(center)
        binding.mapView.dispatchTouchEvent(clickEvent.first)
        binding.mapView.dispatchTouchEvent(clickEvent.second)
    }

    private fun addAnnotationToMap(point: PrivatePointModel) {
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

            annotation.getData()?.asInt?.let { pointId ->
                viewModel.getPointDetailsPreview(pointId).collect { details ->
                    prepareDetailsDialog(annotation, details)
                }
            }
        }
    }

    private fun prepareDetailsDialog(
        pointAnnotation: PointAnnotation,
        details: PrivatePointDetailsPreviewModel?
    ) {
        binding.bottomSheetDialogLayout.apply {
            pointCaptionText.text = details?.caption ?: ""
            pointDescriptionText.text = details?.description ?: ""

            pointDetailsEditButton.setOnClickListener {
                findNavController().navigate(
                    PrivatePointsFragmentDirections
                        .actionPrivatePointsFragmentToPointDetailsFragment(pointAnnotation.getData()?.asLong!!)
                )
            }

            pointDetailsDeleteButton.setOnClickListener {
                pointAnnotation.getData()?.asInt?.let { pointId ->
                    viewModel.deletePoint(
                        pointId
                    )
                }

                pointAnnotationManager.delete(pointAnnotation)
                sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
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