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
    private val queryProductsByName: Query
) : PagingSource<QuerySnapshot, PublicRouteModel>() {
    override fun getRefreshKey(state: PagingState<QuerySnapshot, PublicRouteModel>): QuerySnapshot? {
        return null
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, PublicRouteModel> {
        return try {
            withContext(Dispatchers.IO) {
                val currentPage = params.key ?: Tasks.await(queryProductsByName.get())
                val lastVisibleProduct = currentPage.documents[currentPage.size() - 1]
                val nextPage = Tasks.await(queryProductsByName.startAfter(lastVisibleProduct).get())
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