package com.example.trampled_trails.ui.private_image_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeFragment
import com.example.trampled_trails.databinding.FragmentImageDetailsBinding
import com.example.trampled_trails.ui.themes.MyAppTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File
import java.net.URI

class ImageDetailsFragment : ThemeFragment() {
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var binding: FragmentImageDetailsBinding
    private lateinit var theme: MyAppTheme
    private val arguments by navArgs<ImageDetailsFragmentArgs>()
    private val viewModel: ImageDetailsViewModel by viewModel { parametersOf(arguments.pointId) }
    private val imageAdapter = ImageDetailsAdapter()

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

    override fun syncTheme(appTheme: AppTheme) {
        theme = appTheme as MyAppTheme

        with(binding) {
            DrawableCompat.wrap(topToolbar.background)
                .setTint(theme.colorSecondary(requireContext()))
        }
    }

    private fun configToolbar() {
        binding.deleteImageButton.setOnClickListener {
            imageAdapter.currentList[layoutManager.findFirstVisibleItemPosition()].let { imageData ->
                File(URI.create(imageData.image)).let { imageFile ->
                    if (imageFile.exists() && imageAdapter.currentList.size > 1) {
                        imageFile.delete()
                        viewModel.deletePointImage(imageData)
                    } else {
                        imageFile.delete()
                        viewModel.deletePointImage(imageData)
                        findNavController().popBackStack()
                    }
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