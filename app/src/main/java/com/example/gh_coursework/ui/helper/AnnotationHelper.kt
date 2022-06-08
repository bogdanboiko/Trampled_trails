package com.example.gh_coursework.ui.helper

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions

fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
    if (sourceDrawable == null) {
        return null
    }
    return if (sourceDrawable is BitmapDrawable) {
        sourceDrawable.bitmap
    } else {
        val constantState = sourceDrawable.constantState ?: return null
        val drawable = constantState.newDrawable().mutate()
        val bitmap: Bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth, drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        bitmap
    }
}

fun createAnnotationPoint(bitmap: Bitmap, point: Point): PointAnnotationOptions {
    return PointAnnotationOptions()
        .withPoint(point)
        .withIconImage(bitmap)
        .withIconAnchor(IconAnchor.BOTTOM)
}

fun createFlagAnnotationPoint(bitmap: Bitmap, point: Point): PointAnnotationOptions {
    return PointAnnotationOptions()
        .withPoint(point)
        .withIconImage(bitmap)
        .withIconAnchor(IconAnchor.BOTTOM_LEFT)
        .withIconOffset(listOf(-9.6, 3.8))
}

fun eraseCameraToPoint(x: Double, y: Double, mapView: MapView) {
    mapView.camera.easeTo(
        CameraOptions.Builder()
            .center(Point.fromLngLat(x, y))
            .zoom(14.0)
            .build()
    )
}