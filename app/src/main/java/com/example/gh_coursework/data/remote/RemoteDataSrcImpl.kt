package com.example.gh_coursework.data.remote

import android.net.Uri
import com.example.gh_coursework.data.datasource.TravelDatasource
import com.example.gh_coursework.data.remote.entity.PublicRoutePointResponseEntity
import com.example.gh_coursework.data.remote.mapper.mapPublicRoutePointResponseEntityToDomain
import com.example.gh_coursework.data.remote.mapper.mapRouteDomainToPublicRouteEntity
import com.example.gh_coursework.data.remote.mapper.mapRoutePointDomainToPublicRoutePointEntity
import com.example.gh_coursework.domain.entity.RouteDomain
import com.example.gh_coursework.domain.entity.RoutePointDomain
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.*

class RemoteDataSrcImpl(
    private val db: FirebaseFirestore,
    private val storage: FirebaseStorage
) : TravelDatasource.Remote {
    private val ref = storage.reference

    override fun publishRoute(route: RouteDomain, routePoints: List<RoutePointDomain>) {
        val routeDocRef = db.collection("routes").document()
        val routeImagesAddTasks = mutableListOf<StorageTask<UploadTask.TaskSnapshot>>()
        val routeImagesGetUriTasks = mutableListOf<Task<Uri>>()

        routePoints.forEachIndexed { index, point ->
            val routePointImageAddTasks = mutableListOf<StorageTask<UploadTask.TaskSnapshot>>()
            val routePointImageGetUriTasks = mutableListOf<Task<Uri>>()
            val routePointDocRef =
                db.collection("routes").document(routeDocRef.id).collection("points").document()

            point.imageList.forEach {
                val generatedImageId = UUID.randomUUID().toString()
                val pointImageRef = ref.child("point_images/$generatedImageId")
                routePointImageAddTasks.add(
                    pointImageRef.putFile(Uri.parse(it.image)).addOnSuccessListener {
                        val task = pointImageRef.downloadUrl
                        routePointImageGetUriTasks.add(task)
                        routeImagesGetUriTasks.add(task)
                    })
            }

            routeImagesAddTasks.addAll(routePointImageAddTasks)

            Tasks.whenAll(routePointImageAddTasks).addOnSuccessListener {
                Tasks.whenAllSuccess<Uri>(routePointImageGetUriTasks).addOnSuccessListener {
                    routePointDocRef.set(
                        mapRoutePointDomainToPublicRoutePointEntity(
                            point,
                            it.map { imageUrl -> imageUrl.toString() },
                            index)
                    )
                }
            }
        }

        route.imageList.forEach {
            val generatedImageId = UUID.randomUUID().toString()
            val pointImageRef = ref.child("route_images/$generatedImageId")
            routeImagesAddTasks.add(
                pointImageRef.putFile(Uri.parse(it.image)).addOnSuccessListener {
                    routeImagesGetUriTasks.add(pointImageRef.downloadUrl)
                })
        }

        Tasks.whenAll(routeImagesAddTasks).addOnSuccessListener {
            Tasks.whenAllSuccess<Uri>(routeImagesGetUriTasks).addOnSuccessListener {
                routeDocRef.set(
                    mapRouteDomainToPublicRouteEntity(
                        route,
                        it.map { imageUrl -> imageUrl.toString() })
                )
            }
        }
    }

    override fun fetchRoutePoints(routeId: String) = flow {
        val points = Tasks.await(
            db.collection("routes")
                .document(routeId)
                .collection("points")
                .get()
        )
        val data = mutableListOf<PublicRoutePointResponseEntity>()

        points.documents.forEach {
            data.add(
                PublicRoutePointResponseEntity(
                    it.id,
                    it.getString("caption")!!,
                    it.getString("description")!!,
                    (it.get("imageList") ?: emptyList<String>()) as List<String>,
                    it.getDouble("x")!!,
                    it.getDouble("y")!!,
                    it.getBoolean("routePoint")!!,
                    it.getLong("position")!!
                )
            )
        }

        data.sortBy { it.position }

        this.emit(data.map(::mapPublicRoutePointResponseEntityToDomain))
    }.flowOn(Dispatchers.IO)
}