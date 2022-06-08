package com.example.gh_coursework.data.remote.paging_source

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gh_coursework.ui.public_route.model.PublicRouteModel
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PublicFavouritesPagingSource(
    private val queryRoutes: Query,
    private val routesIdList: List<String>
) : PagingSource<QuerySnapshot, PublicRouteModel>() {

    override fun getRefreshKey(state: PagingState<QuerySnapshot, PublicRouteModel>): QuerySnapshot? {
        return null
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, PublicRouteModel> {
        return try {
            withContext(Dispatchers.IO) {
                val currentPage = params.key ?: if (routesIdList.isNotEmpty()) {
                    Tasks.await(
                        queryRoutes.whereIn(
                            FieldPath.documentId(),
                            routesIdList
                        ).get()
                    )
                } else {
                    Tasks.await(
                        queryRoutes.get()
                    )
                }

                if (currentPage.isEmpty) {
                    return@withContext LoadResult.Page(
                        data = emptyList(),
                        prevKey = null,
                        nextKey = null
                    )
                }

                val lastVisibleProduct = currentPage.documents[currentPage.size() - 1]
                val nextPage = if (routesIdList.isNotEmpty()) {
                    Tasks.await(
                        queryRoutes.whereIn(
                            FieldPath.documentId(),
                            routesIdList
                        ).startAfter(lastVisibleProduct).get()
                    )
                } else {
                    Tasks.await(
                        queryRoutes.startAfter(lastVisibleProduct).get()
                    )
                }

                val data = mutableListOf<PublicRouteModel>()

                currentPage.documents.forEach {
                    data.add(
                        PublicRouteModel(
                            it.id,
                            it.getString("name")!!,
                            it.getString("description")!!,
                            (it.get("tagsList") ?: emptyList<String>()) as List<String>,
                            (it.get("imageList") ?: emptyList<String>()) as List<String>,
                            it.getString("userId")!!,
                            true
                        )
                    )
                }

                LoadResult.Page(
                    data = data,
                    prevKey = null,
                    nextKey = nextPage
                )
            }
        } catch (e: Exception) {
            Log.e("e", e.toString())
            LoadResult.Page(
                data = emptyList(),
                prevKey = null,
                nextKey = null
            )
        }
    }
}