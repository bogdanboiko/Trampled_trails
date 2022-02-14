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
import com.example.gh_coursework.R
import com.example.gh_coursework.databinding.PrivatePointsFragmentBinding
import com.example.gh_coursework.ui.helper.convertDrawableToBitmap
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.AnnotationPlugin
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.OnMapClickListener
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.gestures.removeOnMapClickListener

class PrivatePointsFragment : Fragment(R.layout.private_points_fragment) {
    private var mapboxMap: MapboxMap? = null
    private lateinit var binding: PrivatePointsFragmentBinding
    private var mapView: MapView? = null
    private var annotationApi: AnnotationPlugin? = null
    private var pointAnnotationManager: PointAnnotationManager? = null
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

    private fun configAddPointButton() {
        binding.addPointButton.setOnClickListener {
            with(binding) {
                addPointButton.visibility = View.INVISIBLE
                imageView.visibility = View.VISIBLE
                setPointButton.visibility = View.VISIBLE
                cancelPointButton.visibility = View.VISIBLE
            }

            mapboxMap?.addOnMapClickListener(onMapClickListener)
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

    private fun configCancelButton() {
        binding.cancelPointButton.setOnClickListener {
            with(binding) {
                addPointButton.visibility = View.VISIBLE
                imageView.visibility = View.INVISIBLE
                setPointButton.visibility = View.INVISIBLE
                cancelPointButton.visibility = View.INVISIBLE
            }

            mapboxMap?.removeOnMapClickListener(onMapClickListener)
        }
    }

    private fun configMap() {
        mapView = binding.mapView
        annotationApi = mapView?.annotations
        pointAnnotationManager = annotationApi?.createPointAnnotationManager()
        mapboxMap = mapView?.getMapboxMap()?.also {
            it.loadStyleUri(Style.MAPBOX_STREETS)
        }
    }

    private fun addAnnotationToMap(point: Point) {
        activity?.applicationContext?.let {
            bitmapFromDrawableRes(it, R.drawable.ic_def_point)?.let { image ->
                pointAnnotationManager?.create(createAnnotationPoint(image, point))
            }
        }
    }

    private fun createAnnotationPoint(bitmap: Bitmap, point: Point): PointAnnotationOptions {
        return PointAnnotationOptions()
            .withPoint(point)
            .withIconImage(bitmap)
    }

    private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
        convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))
}