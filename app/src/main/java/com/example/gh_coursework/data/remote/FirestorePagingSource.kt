package com.example.gh_coursework.data.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gh_coursework.ui.public_route.model.PublicRouteModel
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirestorePagingSource(
    private val queryRoutes: Query,
    private val tagsFilter: List<String>
) : PagingSource<QuerySnapshot, PublicRouteModel>() {
    override fun getRefreshKey(state: PagingState<QuerySnapshot, PublicRouteModel>): QuerySnapshot? {
        return null
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, PublicRouteModel> {
        return try {
            withContext(Dispatchers.IO) {
                var currentPage = params.key ?: if (tagsFilter.isNotEmpty()) {
                    Tasks.await(
                        queryRoutes.whereArrayContainsAny(
                            "tagsList",
                            tagsFilter
                        ).get()
                    )
                } else {
                    Tasks.await(
                        queryRoutes.get()
                    )
                }


                val lastVisibleProduct = currentPage.documents[currentPage.size() - 1]
                val nextPage = if (tagsFilter.isNotEmpty()) {
                    Tasks.await(
                        queryRoutes.whereArrayContainsAny(
                            "tagsList",
                            tagsFilter
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
                            0.0,
                            (it.get("tagsList") ?: emptyList<String>()) as List<String>,
                            (it.get("imageList") ?: emptyList<String>()) as List<String>
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
            LoadResult.Error(e)
        }
    }
}