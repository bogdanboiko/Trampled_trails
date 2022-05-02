package com.example.gh_coursework.data.remote

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
                val currentPage = params.key ?: if (tagsFilter.isNotEmpty()) {
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

                if (currentPage.isEmpty) {
                    return@withContext LoadResult.Page(
                        data = emptyList(),
                        prevKey = null,
                        nextKey = null
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
            LoadResult.Error(e)
        }
    }
}