package com.example.trampled_trails.di

import com.example.trampled_trails.ActivityViewModel
import com.example.trampled_trails.ui.point_details.PointDetailsViewModel
import com.example.trampled_trails.ui.private_image_details.ImageDetailsViewModel
import com.example.trampled_trails.ui.point_details.tag_dialog.TagDialogViewModel
import com.example.trampled_trails.ui.private_point.PointViewModel
import com.example.trampled_trails.ui.private_point.tag_dialog.PointFilterByTagDialogViewModel
import com.example.trampled_trails.ui.private_route.PrivateRouteViewModel
import com.example.trampled_trails.ui.private_route.tag_dialog.RouteFilterByTagDialogViewModel
import com.example.trampled_trails.ui.public_route.PublicRouteViewModel
import com.example.trampled_trails.ui.route_details.RouteDetailsViewModel
import com.example.trampled_trails.ui.route_details.image_details.RouteDetailsImageViewModel
import com.example.trampled_trails.ui.route_details.tag_dialog.RouteTagDialogViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        PointViewModel(get(), get(), get())
    }

    viewModel {
        PrivateRouteViewModel(get(), get(), get(), get(), get(), get())
    }

    viewModel { parameters ->
        TagDialogViewModel(parameters[0], get(), get(), get(), get())
    }

    viewModel { parameters ->
        RouteTagDialogViewModel(parameters[0], get(), get(), get(), get())
    }

    viewModel { parameters ->
        PointDetailsViewModel(parameters[0], get(), get(), get())
    }

    viewModel { parameters ->
        ImageDetailsViewModel(parameters[0], get(), get())
    }

    viewModel { parameters ->
        RouteDetailsViewModel(parameters[0], get(), get(), get(), get())
    }

    viewModel { parameters ->
        RouteDetailsImageViewModel(parameters[0], get(), get(), get(), get())
    }

    viewModel {
        PublicRouteViewModel(get(), get(), get(), get(), get())
    }

    viewModel {
        RouteFilterByTagDialogViewModel(get())
    }

    viewModel {
        PointFilterByTagDialogViewModel(get())
    }

    viewModel {
        ActivityViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get())
    }
}
