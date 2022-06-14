package com.example.trampled_trails.ui.helper

import com.mapbox.geojson.Point
import com.mapbox.turf.TurfMeasurement
import java.text.DecimalFormat

fun getRouteDistance(pointsCoordinatesList: MutableList<Point>): String {
    var distance = 0.0

    for (index in 0 until pointsCoordinatesList.size - 1) {
        distance += TurfMeasurement.distance(pointsCoordinatesList[index], pointsCoordinatesList[index + 1])
    }

    return DecimalFormat("#.#").format(distance)
}