package com.example.gh_coursework.ui.private_route

import com.mapbox.geojson.Point

class WaypointsSet {

    private val waypoints = mutableListOf<Point>()

    fun addRegular(point: Point) {
        waypoints.add(point)
    }

    fun clear() {
        waypoints.clear()
    }

    fun coordinatesList(): List<Point> {
        return waypoints
    }
}