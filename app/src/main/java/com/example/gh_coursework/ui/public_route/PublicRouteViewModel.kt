package com.example.gh_coursework.ui.public_route

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.gh_coursework.data.remote.FirestorePagingSource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class PublicRouteViewModel(
    private val query: Query
) : ViewModel() {

    val routeList = Pager(
            PagingConfig(pageSize = 10)
        ) {
            FirestorePagingSource(query)
        }.flow.cachedIn(viewModelScope)
}