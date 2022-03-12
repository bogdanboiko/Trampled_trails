package com.example.gh_coursework.ui.private_route

import com.mapbox.geojson.Point

class WaypointsSet {

    private val waypoints = mutableListOf<Waypoint>()

    fun addRegular(point: Point) {
        waypoints.add(Waypoint(point, WaypointType.Regular))
    }

    fun addNamed(point: Point, name: String, description: String) {
        waypoints.add(Waypoint(point, WaypointType.Named(name, description)))
    }

    fun undoLastPointCreation() {
        waypoints.remove(waypoints[waypoints.lastIndex])
    }

    fun clear() {
        waypoints.clear()
    }

    fun getCoordinatesList(): List<Point> {
        return waypoints.map {it.point}
    }

    private sealed class WaypointType {
        data class Named(
            val title: String,
            val description: String) : WaypointType()
        object Regular : WaypointType()
    }

    private data class Waypoint(val point: Point, val type: WaypointType)
}