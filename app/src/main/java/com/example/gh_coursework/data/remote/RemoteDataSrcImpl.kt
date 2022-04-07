package com.example.gh_coursework.data.remote

import android.util.Log
import com.example.gh_coursework.data.datasource.TravelDatasource
import com.example.gh_coursework.data.remote.mapper.mapRouteDomainToPublicRouteEntity
import com.example.gh_coursework.data.remote.mapper.mapRoutePointDomainToPublicRoutePointEntity
import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.domain.entity.RoutePointDomain
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RemoteDataSrcImpl : TravelDatasource.Remote {
    private val db = Firebase.firestore
    override fun publishRoute(route: RouteDomain, routePoints: List<RoutePointDomain>) {
        db.collection("routes")
            .add(mapRouteDomainToPublicRouteEntity(route))
            .addOnSuccessListener {
                routePoints.forEach { routePoint ->
                    db.collection("routes")
                        .document(it.id)
                        .collection("points")
                        .add(mapRoutePointDomainToPublicRoutePointEntity(routePoint))
                }
            }
    }
}