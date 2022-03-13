package com.example.gh_coursework.ui.point_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gh_coursework.domain.usecase.AddPointDetailsUseCase
import com.example.gh_coursework.domain.usecase.AddPointTagUseCase
import com.example.gh_coursework.domain.usecase.GetPointDetailsUseCase
import com.example.gh_coursework.domain.usecase.GetPointTagListUseCase
import com.example.gh_coursework.ui.point_details.model.PointDetailsModel
import com.example.gh_coursework.ui.point_details.mapper.mapPointDetailsDomainToModel
import com.example.gh_coursework.ui.point_details.mapper.mapPointDetailsModelToDomain
import com.example.gh_coursework.ui.point_details.mapper.mapPointTagDomainToModel
import com.example.gh_coursework.ui.point_details.mapper.mapPointTagModelToDomain
import com.example.gh_coursework.ui.point_details.model.PointTagModel
import com.example.gh_coursework.ui.private_point.mapper.mapPointModelToDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PointDetailsViewModel(
    private val pointId: Int,
    private val addPointDetailsUseCase: AddPointDetailsUseCase,
    private val getPointDetailsUseCase: GetPointDetailsUseCase,
    private val addPointTagUseCase: AddPointTagUseCase,
    private val getPointTagListUseCase: GetPointTagListUseCase
) : ViewModel() {
    val pointDetails = getPointDetailsUseCase.invoke(pointId).map { mapPointDetailsDomainToModel(it) }
    val tags = getPointTagListUseCase.invoke().map { tagList -> tagList.map(::mapPointTagDomainToModel) }

    fun addTag(tag: PointTagModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addPointTagUseCase.invoke(mapPointTagModelToDomain(tag))
        }
    }

    fun addPointDetails(details: PointDetailsModel) {
        viewModelScope.launch(Dispatchers.IO) {
            addPointDetailsUseCase.invoke(mapPointDetailsModelToDomain(details))
        }
    }
}