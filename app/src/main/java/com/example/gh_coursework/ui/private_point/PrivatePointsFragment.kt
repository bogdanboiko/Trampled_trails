package com.example.gh_coursework.ui.private_point

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.example.gh_coursework.MapState
import com.example.gh_coursework.OnAddButtonPressed
import com.example.gh_coursework.R
import com.example.gh_coursework.databinding.FragmentPrivatePointsBinding
import com.example.gh_coursework.databinding.ItemAnnotationViewBinding
import com.example.gh_coursework.ui.helper.convertDrawableToBitmap
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

class PrivatePointsFragment : Fragment(R.layout.fragment_private_points), OnAddButtonPressed {
    private lateinit var viewAnnotationManager: ViewAnnotationManager
    private lateinit var mapboxMap: MapboxMap
    private lateinit var binding: FragmentPrivatePointsBinding
    private lateinit var pointAnnotationManager: PointAnnotationManager
    private lateinit var center: Pair<Float, Float>
    private val onMapClickListener = OnMapClickListener { point ->
        addAnnotationToMap(point)
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
        val downTime = SystemClock.uptimeMillis()
        val eventTime = SystemClock.uptimeMillis() + 10
        val downAction = MotionEvent.obtain(
            downTime, eventTime, MotionEvent.ACTION_DOWN,
            center.first , center.second, 0
        )
        val upAction = MotionEvent.obtain(
            downTime, eventTime, MotionEvent.ACTION_UP,
            center.first , center.second, 0
        )
        binding.mapView.dispatchTouchEvent(downAction)
        binding.mapView.dispatchTouchEvent(upAction)
    }

    private fun addAnnotationToMap(point: Point) {
        activity?.applicationContext?.let {
            bitmapFromDrawableRes(it, R.drawable.ic_def_point)?.let { image ->
                pointAnnotationManager.addClickListener(OnPointAnnotationClickListener { annotation ->
                    prepareViewAnnotation(annotation)
                    true
                })

                pointAnnotationManager.create(createAnnotationPoint(image, point))
            }
        }
    }

    private fun prepareViewAnnotation(pointAnnotation: PointAnnotation) {
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
            textNativeView.text = "lat=%.2f\nlon=%.2f".format(
                pointAnnotation.geometry.latitude(),
                pointAnnotation.geometry.longitude()
            )

            closeNativeView.setOnClickListener {
                viewAnnotationManager.removeViewAnnotation(viewAnnotation)
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