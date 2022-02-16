package com.example.gh_coursework.ui.private_point

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.example.gh_coursework.R
import com.example.gh_coursework.databinding.ItemAnnotationViewBinding
import com.example.gh_coursework.databinding.PrivatePointsFragmentBinding
import com.example.gh_coursework.ui.helper.convertDrawableToBitmap
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.ViewAnnotationAnchor
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationClickListener
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.OnMapClickListener
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.gestures.removeOnMapClickListener
import com.mapbox.maps.viewannotation.ViewAnnotationManager
import com.mapbox.maps.viewannotation.viewAnnotationOptions

class PrivatePointsFragment : Fragment(R.layout.private_points_fragment) {
    private lateinit var viewAnnotationManager: ViewAnnotationManager
    private lateinit var mapboxMap: MapboxMap
    private lateinit var binding: PrivatePointsFragmentBinding
    private lateinit var pointAnnotationManager: PointAnnotationManager
    private val onMapClickListener = OnMapClickListener { point ->
        addAnnotationToMap(point)
        return@OnMapClickListener true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = PrivatePointsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configMap()
        configAddPointButton()
        configCancelButton()
        view.viewTreeObserver.addOnGlobalLayoutListener {
            configSetPointButton(view.width / 2f, view.height / 2f)
        }
    }

    private fun configMap() {
        mapboxMap = binding.mapView.getMapboxMap().also {
            viewAnnotationManager = binding.mapView.viewAnnotationManager
            it.loadStyleUri(Style.MAPBOX_STREETS)
        }
        pointAnnotationManager = binding.mapView.annotations.createPointAnnotationManager()
    }

    private fun configAddPointButton() {
        binding.addPointButton.setOnClickListener {
            with(binding) {
                addPointButton.visibility = View.INVISIBLE
                imageView.visibility = View.VISIBLE
                setPointButton.visibility = View.VISIBLE
                cancelPointButton.visibility = View.VISIBLE
            }

            mapboxMap.addOnMapClickListener(onMapClickListener)
        }
    }

    private fun configCancelButton() {
        binding.cancelPointButton.setOnClickListener {
            with(binding) {
                addPointButton.visibility = View.VISIBLE
                imageView.visibility = View.INVISIBLE
                setPointButton.visibility = View.INVISIBLE
                cancelPointButton.visibility = View.INVISIBLE
            }

            mapboxMap.removeOnMapClickListener(onMapClickListener)
        }
    }

    private fun configSetPointButton(width: Float, height: Float) {
        val downTime = SystemClock.uptimeMillis()
        val eventTime = SystemClock.uptimeMillis() + 10
        val downAction = MotionEvent.obtain(
            downTime, eventTime, MotionEvent.ACTION_DOWN,
            width, height, 0)
        val upAction = MotionEvent.obtain(
            downTime, eventTime, MotionEvent.ACTION_UP,
            width, height, 0)
        binding.setPointButton.setOnClickListener {
            binding.mapView.dispatchTouchEvent(downAction)
            binding.mapView.dispatchTouchEvent(upAction)
        }
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
                pointAnnotation.geometry.longitude())

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