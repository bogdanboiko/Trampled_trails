package com.example.gh_coursework.di

import com.example.gh_coursework.ui.private_point.PointViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        PointViewModel(get(), get())
    }

    viewModel {
        TODO("Private routes fragment vm")
    }
}
