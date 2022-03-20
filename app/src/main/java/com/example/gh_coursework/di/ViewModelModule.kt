package com.example.gh_coursework.di

import com.example.gh_coursework.ui.point_details.PointDetailsViewModel
import com.example.gh_coursework.ui.point_details.tag_dialog.TagDialogViewModel
import com.example.gh_coursework.ui.private_point.PointViewModel
import com.example.gh_coursework.ui.private_route.RouteViewModel
import com.example.gh_coursework.ui.route_details.RouteDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        PointViewModel(get(), get(), get(), get())
    }

    viewModel {
        RouteViewModel(get(), get(), get(), get(), get(), get())
    }

    viewModel { parameters ->
        TagDialogViewModel(parameters[0], get(), get(), get(), get(), get(), get())
    }

    viewModel { parameters ->
        PointDetailsViewModel(parameters[0], get(), get())
    }

    viewModel { parameters ->
        RouteDetailsViewModel(parameters[0], get(), get())
    }
}
