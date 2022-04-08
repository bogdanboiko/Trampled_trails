package com.example.gh_coursework.data.remote

import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import com.example.gh_coursework.data.datasource.TravelDatasource
import com.example.gh_coursework.data.remote.mapper.mapRouteDomainToPublicRouteEntity
import com.example.gh_coursework.data.remote.mapper.mapRoutePointDomainToPublicRoutePointEntity
import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.domain.entity.RoutePointDomain
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class RemoteDataSrcImpl : TravelDatasource.Remote {
    private val db = Firebase.firestore
    private val storage = FirebaseStorage.getInstance("gs://gh-coursework.appspot.com")
    private val ref = storage.reference

    override fun publishRoute(route: RouteDomain, routePoints: List<RoutePointDomain>) {
        val imageUrls = mutableListOf<String>()

        route.imageList.forEach { imageUrl ->
            val generatedImageId = UUID.randomUUID().toString()
            ref.child("route_images/$generatedImageId")
                .putFile(Uri.parse(imageUrl.image)).addOnSuccessListener {
                    imageUrls.add(generatedImageId)
                }
        }

        db.collection("routes")
            .add(mapRouteDomainToPublicRouteEntity(route, imageUrls))
            .addOnSuccessListener { routeDocument ->
                routePoints.forEach { routePoint ->
                    db.collection("routes")
                        .document(routeDocument.id)
                        .collection("points")
                        .add(mapRoutePointDomainToPublicRoutePointEntity(routePoint))
                }
            }
    }
}