package com.example.trampled_trails.ui.route_details.image_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.trampled_trails.R
import com.example.trampled_trails.databinding.FragmentImageDetailsBinding
import com.example.trampled_trails.ui.private_image_details.model.ImageModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RouteDetailsImageFragment : Fragment(R.layout.fragment_image_details) {

    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var binding: FragmentImageDetailsBinding
    private val arguments by navArgs<RouteDetailsImageFragmentArgs>()
    private val viewModel: RouteDetailsImageViewModel by viewModel { parametersOf(arguments.routeId) }
    private val imageAdapter = RouteImageDetailsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configRecycler()
        configToolbar()
    }

    private fun configToolbar() {
        binding.deleteImageButton.setOnClickListener {
            imageAdapter.currentList[layoutManager.findFirstVisibleItemPosition()]?.let { image ->
                if (imageAdapter.currentList.size > 1) {
                    if (image is ImageModel.PointImageModel) {
                        viewModel.deletePointImage(image)
                    } else if (image is ImageModel.RouteImageModel) {
                        viewModel.deleteRouteImage(image)
                    }
                } else {
                    if (image is ImageModel.PointImageModel) {
                        viewModel.deletePointImage(image)
                    } else if (image is ImageModel.RouteImageModel) {
                        viewModel.deleteRouteImage(image)
                    }

                    findNavController().popBackStack()
                }
            }
        }

        binding.backImageButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun configRecycler() {
        PagerSnapHelper().attachToRecyclerView(binding.pointImageRecycler)
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        imageAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                layoutManager.scrollToPosition(arguments.clickedItemCount)
            }
        })

        binding.pointImageRecycler.apply {
            adapter = imageAdapter
            layoutManager = this@RouteDetailsImageFragment.layoutManager
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.routeImages.combine(viewModel.pointImages) { routeImages, pointImages ->
                val images = mutableListOf<ImageModel>()

                images.addAll(routeImages)

                pointImages.forEach {
                    images.addAll(it.imagesList)
                }

                return@combine images
            }.collect {
                imageAdapter.submitList(it)
            }
        }
    }
}