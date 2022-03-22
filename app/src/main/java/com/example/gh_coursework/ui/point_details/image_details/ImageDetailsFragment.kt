package com.example.gh_coursework.ui.point_details.image_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.gh_coursework.R
import com.example.gh_coursework.databinding.FragmentImageDetailsBinding
import com.example.gh_coursework.ui.point_details.adapter.ImageDetailsAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ImageDetailsFragment : Fragment(R.layout.fragment_image_details) {
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var binding: FragmentImageDetailsBinding
    private val arguments by navArgs<ImageDetailsFragmentArgs>()
    private val viewModel: ImageDetailsViewModel by viewModel { parametersOf(arguments.pointId) }
    private val imageAdapter = ImageDetailsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            viewModel.deletePointImage(
                imageAdapter.currentList[layoutManager.findFirstVisibleItemPosition()]
            )
        }
    }

    private fun configRecycler() {
        PagerSnapHelper().attachToRecyclerView(binding.pointImageRecycler)
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        imageAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(
                positionStart: Int,
                itemCount: Int
            ) {
                layoutManager.scrollToPosition(arguments.clickedItemCount)
            }
        })
        binding.pointImageRecycler.apply {
            adapter = imageAdapter
            layoutManager = this@ImageDetailsFragment.layoutManager
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pointImages.collect {
                imageAdapter.submitList(it)
            }
        }
    }
}