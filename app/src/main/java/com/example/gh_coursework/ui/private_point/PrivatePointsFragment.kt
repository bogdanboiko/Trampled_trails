package com.example.gh_coursework.ui.private_point

import android.content.Context
import android.graphics.Bitmap
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
import com.example.gh_coursework.databinding.FragmentPrivatePointsBinding
import com.example.gh_coursework.databinding.ItemAnnotationViewBinding
import com.example.gh_coursework.ui.helper.convertDrawableToBitmap
import com.example.gh_coursework.ui.helper.createOnMapClickEvent
import com.example.gh_coursework.ui.private_point.model.PrivatePointDetailsPreviewModel
import com.example.gh_coursework.ui.private_point.model.PrivatePointModel
import com.google.gson.JsonPrimitive
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.ViewAnnotationAnchor
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.*
import com.mapbox.maps.plugin.gestures.OnMapClickListener
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.gestures.removeOnMapClickListener
import com.mapbox.maps.viewannotation.ViewAnnotationManager
import com.mapbox.maps.viewannotation.viewAnnotationOptions
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PrivatePointsFragment : Fragment(R.layout.fragment_private_points), OnAddButtonPressed {
    private val viewModel: PointViewModel by viewModel()
    private var pointCoordinates = emptyList<PrivatePointModel>()
    private lateinit var viewAnnotationManager: ViewAnnotationManager
    private lateinit var mapboxMap: MapboxMap
    private lateinit var binding: FragmentPrivatePointsBinding
    private lateinit var pointAnnotationManager: PointAnnotationManager
    private lateinit var center: Pair<Float, Float>
    private val onMapClickListener = OnMapClickListener { point ->
        val newPoint = PrivatePointModel(null, point.longitude(), point.latitude())
        viewModel.addPoint(newPoint)
        return@OnMapClickListener true
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
    }

    private fun fetchPoints() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.points.collect { data ->
                data.minus(pointCoordinates).forEach {
                    addAnnotationToMap(it)
                }

                pointCoordinates = data
            }
        }
    }

    override fun switchMapMod(mapState: MapState) {
        if (mapState == MapState.CREATOR) {
            binding.centralPointer.visibility = View.VISIBLE
            mapboxMap.addOnMapClickListener(onMapClickListener)
        } else {
            binding.centralPointer.visibility = View.INVISIBLE
            mapboxMap.removeOnMapClickListener(onMapClickListener)
        }
    }

    override fun onAddButtonPressed() {
        executeClickAtPoint()
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

    private fun prepareViewAnnotation(
        pointAnnotation: PointAnnotation,
        details: PrivatePointDetailsPreviewModel?) {
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
                    PrivatePointsFragmentDirections
                        .actionPrivatePointsFragmentToPointDetailsFragment(pointAnnotation.getData()?.asInt!!)
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