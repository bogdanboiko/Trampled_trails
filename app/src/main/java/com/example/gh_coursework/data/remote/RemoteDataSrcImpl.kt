package com.example.gh_coursework.data.remote

import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import com.example.gh_coursework.data.datasource.TravelDatasource
import com.example.gh_coursework.data.remote.mapper.mapRouteDomainToPublicRouteEntity
import com.example.gh_coursework.data.remote.mapper.mapRoutePointDomainToPublicRoutePointEntity
import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.domain.entity.RoutePointDomain
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class RemoteDataSrcImpl : TravelDatasource.Remote {
    private val db = Firebase.firestore
    private val storage = FirebaseStorage.getInstance("gs://gh-coursework-f4257.appspot.com")
    private val ref = storage.reference

    override fun publishRoute(route: RouteDomain, routePoints: List<RoutePointDomain>) {
        db.collection("routes")
            .add(mapRouteDomainToPublicRouteEntity(route))
            .addOnSuccessListener { routeDocument ->
                Log.e("e", "eeee")
                val routeImageUrls = mutableListOf<String>()

                route.imageList.forEach { imageUrl ->
                    val generatedImageId = UUID.randomUUID().toString()
                    val routeImageReference = ref.child("route_images/$generatedImageId")
                    routeImageReference.putFile(Uri.parse(imageUrl.image)).addOnSuccessListener {
                        routeImageReference.downloadUrl.apply {
                            this.addOnSuccessListener {
                                routeImageUrls.add(it.toString())
                            }
                            this.addOnCompleteListener {
                                if (imageUrl == route.imageList.last()) {
                                    routeDocument.update(hashMapOf("imageList" to routeImageUrls) as Map<String, Any>)
                                }
                            }
                        }
                    }
                }

                routePoints.forEach { routePoint ->
                    db.collection("routes")
                        .document(routeDocument.id)
                        .collection("points")
                        .add(mapRoutePointDomainToPublicRoutePointEntity(routePoint))
                        .addOnSuccessListener { routePointDocument ->
                            val pointImageUrls = mutableListOf<String>()

                            routePoint.imageList.forEach { imageUrl ->
                                val image = Uri.parse(imageUrl.image)
                                if (image.toFile().exists()) {
                                    val generatedImageId = UUID.randomUUID().toString()

                                    val routePointImageReference =
                                        ref.child("point_images/$generatedImageId")
                                    routePointImageReference.putFile(image).addOnSuccessListener {
                                        routePointImageReference.downloadUrl.apply {
                                            this.addOnSuccessListener {
                                                pointImageUrls.add(it.toString())
                                            }
                                            this.addOnCompleteListener {
                                                if (imageUrl == routePoint.imageList.last()) {
                                                    routePointDocument.update(hashMapOf("imageList" to routeImageUrls) as Map<String, Any>)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                }
            }
    }
}