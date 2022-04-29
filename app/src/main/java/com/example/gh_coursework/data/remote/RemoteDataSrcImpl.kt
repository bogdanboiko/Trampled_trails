package com.example.gh_coursework.data.remote

import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import com.example.gh_coursework.data.datasource.TravelDatasource
import com.example.gh_coursework.data.remote.entity.PublicRoutePointResponseEntity
import com.example.gh_coursework.data.remote.entity.PublicRouteResponseEntity
import com.example.gh_coursework.data.remote.mapper.mapPublicRoutePointResponseEntityToDomain
import com.example.gh_coursework.data.remote.mapper.mapPublicRouteResponseToDomain
import com.example.gh_coursework.data.remote.mapper.mapRouteDomainToPublicRouteEntity
import com.example.gh_coursework.data.remote.mapper.mapRoutePointDomainToPublicRoutePointEntity
import com.example.gh_coursework.domain.entity.*
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FieldValue
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

    override suspend fun deletePoint(pointId: String) {
        db.collection("points").document(pointId)
            .delete()
            .addOnFailureListener {
                Log.e("Error deleting point ", it.toString())
            }
    }

    override suspend fun deleteRoute(routeId: String) {
        db.collection("routes").document(routeId).collection("points")
        db.collection("routes").document(routeId)
            .delete()
            .addOnFailureListener {
                Log.e("Error deleting route ", it.toString())
            }
    }

    override suspend fun uploadRouteToFirebase(
        route: RouteDomain,
        routePoints: List<PointDomain>,
        currentUser: String
    ) {
        val routeDocRef = db.collection("routes").document(route.routeId)
        routeDocRef.set(
            mapRouteDomainToPublicRouteEntity(
                route,
                route.imageList.filter { it.isUploaded }.map { it.image },
                currentUser
            )
        )

        uploadPointsToFirebase(routePoints)
        saveRouteImages(route.imageList.filter { !it.isUploaded }, route.routeId)
    }

    override suspend fun uploadPointsToFirebase(points: List<PointDomain>) {
        points.forEachIndexed { index, point ->
            val routePointDocRef =
                db.collection("points").document(point.pointId)
            routePointDocRef.set(
                mapRoutePointDomainToPublicRoutePointEntity(
                    point,
                    point.imageList.filter { it.isUploaded }.map { it.image },
                    index
                )
            )

            savePointImages(
                point.imageList.filter { !it.isUploaded },
                point.pointId
            )
        }
    }

    override suspend fun savePointImages(
        imageList: List<PointImageDomain>,
        pointId: String
    ) {
        val routePointImageAddTasks = mutableListOf<StorageTask<UploadTask.TaskSnapshot>>()
        val routePointImageGetUriTasks = mutableListOf<Task<Uri>>()

        imageList.forEach { image ->
            if (Uri.parse(image.image).toFile().exists()) {
                val generatedImageId = UUID.randomUUID().toString()
                val pointImageRef = ref.child("point_images/$generatedImageId")
                routePointImageAddTasks.add(
                    pointImageRef.putFile(Uri.parse(image.image)).addOnSuccessListener {
                        routePointImageGetUriTasks.add(pointImageRef.downloadUrl)
                    })
            }
        }

        Tasks.await(Tasks.whenAllSuccess<UploadTask.TaskSnapshot>(routePointImageAddTasks))
        val uriList = Tasks.await(Tasks.whenAllSuccess<Uri>(routePointImageGetUriTasks))
        db.collection("points").document(pointId)
            .update(
                "imageList",
                FieldValue.arrayUnion(*uriList.map { imageUrl -> imageUrl.toString() }
                    .toTypedArray())
            )

        imageList.forEach { image ->
            if (Uri.parse(image.image).toFile().exists()) {
                Uri.parse(image.image).toFile().delete()
            }
        }
    }

    override suspend fun saveRouteImages(imageList: List<RouteImageDomain>, routeId: String) {
        val routeImagesAddTasks = mutableListOf<StorageTask<UploadTask.TaskSnapshot>>()
        val routeImagesGetUriTasks = mutableListOf<Task<Uri>>()
        val routeDocRef = db.collection("routes").document(routeId)

        imageList.forEach {
            if (Uri.parse(it.image).toFile().exists()) {
                val generatedImageId = UUID.randomUUID().toString()
                val pointImageRef = ref.child("route_images/$generatedImageId")
                routeImagesAddTasks.add(
                    pointImageRef.putFile(Uri.parse(it.image)).addOnSuccessListener {
                        routeImagesGetUriTasks.add(pointImageRef.downloadUrl)
                    })
            }
        }

        Tasks.await(Tasks.whenAllSuccess<UploadTask.TaskSnapshot>(routeImagesAddTasks))

        val uriList = Tasks.await(Tasks.whenAllSuccess<Uri>(routeImagesGetUriTasks))
        routeDocRef.update(
            "imageList",
            FieldValue.arrayUnion(*uriList.map { imageUrl -> imageUrl.toString() }
                .toTypedArray())
        )
        imageList.forEach { image ->
            if (Uri.parse(image.image).toFile().exists()) {
                Uri.parse(image.image).toFile().delete()
            }
        }
    }

    override fun fetchRoutePoints(routeId: String) = flow {
        val points = Tasks.await(
            db.collection("points").whereEqualTo("routeId", routeId)
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
                    it.getString("routeId"),
                    it.getBoolean("routePoint")!!,
                    it.getLong("position")!!
                )
            )
        }

        data.sortBy { it.position }

        emit(data.map(::mapPublicRoutePointResponseEntityToDomain))
    }.flowOn(Dispatchers.IO)

    override fun getUserRoutes(userId: String) = flow {
        val routes = Tasks.await(db.collection("routes").whereEqualTo("userId", userId).get())

        val data = mutableListOf<PublicRouteResponseEntity>()

        routes.documents.forEach {
            data.add(
                PublicRouteResponseEntity(
                    it.id,
                    it.getString("name")!!,
                    it.getString("description")!!,
                    (it.get("tagsList") ?: emptyList<String>()) as List<String>,
                    (it.get("imageList") ?: emptyList<String>()) as List<String>,
                    it.getBoolean("public")!!
                )
            )
        }

        emit(data.map(::mapPublicRouteResponseToDomain))
    }.flowOn(Dispatchers.IO)

    override fun makePrivateRoutePublic(routeId: String) {
        db.collection("routes").document(routeId).update("public", true)
    }
}