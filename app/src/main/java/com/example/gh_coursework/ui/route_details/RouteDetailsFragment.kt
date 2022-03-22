package com.example.gh_coursework.ui.route_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gh_coursework.R
import com.example.gh_coursework.databinding.FragmentRouteDetailsBinding
import com.example.gh_coursework.ui.route_details.model.RouteDetailsModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RouteDetailsFragment : Fragment(R.layout.fragment_route_details) {
    private val arguments by navArgs<RouteDetailsFragmentArgs>()
    private val viewModel: RouteDetailsViewModel by viewModel { parametersOf(arguments.routeId) }
    private lateinit var binding: FragmentRouteDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRouteDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configToolBar()
        configConfirmButton()
        configTagButton()
        configData()
    }

    private fun configData() {
        with(binding) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.route.collect {
                    routeCaptionText.setText(it.name)
                    routeDescriptionText.setText(it.description)
                }
            }
        }
    }

    private fun configConfirmButton() {
        with(binding) {
            confirmEditButton.setOnClickListener {
                it.visibility = View.GONE
                routeDetailsEditButton.visibility = View.VISIBLE
                routeCaptionText.isEnabled = false
                routeDescriptionText.isEnabled = false
                routeCaptionText.hint = ""
                routeDescriptionText.hint = ""
                viewModel.updateRouteDetails(
                    RouteDetailsModel(
                        arguments.routeId,
                        routeCaptionText.text.toString(),
                        routeDescriptionText.text.toString(),
                        0.0,
                        emptyList(),
                        null
                    )
                )
            }
        }
    }

    private fun configTagButton() {
        binding.routeDetailsTagButton.setOnClickListener {
            findNavController().navigate(
                RouteDetailsFragmentDirections.actionPrivateDetailsFragmentToRouteTagDialogFragment(
                    arguments.routeId
                )
            )
        }
    }

    private fun configToolBar() {
        with(binding) {
            routeDetailsEditButton.setOnClickListener {
                it.visibility = View.GONE
                confirmEditButton.visibility = View.VISIBLE
                routeCaptionText.isEnabled = true
                routeDescriptionText.isEnabled = true
                routeCaptionText.hint = "Put in route caption..."
                routeDescriptionText.hint = "Put in route description..."
            }
        }
    }
}