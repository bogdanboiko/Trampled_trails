package com.example.gh_coursework.ui.public_route.image_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeFragment
import com.example.gh_coursework.databinding.FragmentPublicImageDetailsBinding
import com.example.gh_coursework.ui.themes.MyAppTheme

class PublicRouteDetailsImageFragment : ThemeFragment() {
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var binding: FragmentPublicImageDetailsBinding
    private lateinit var theme: MyAppTheme
    private val arguments by navArgs<PublicRouteDetailsImageFragmentArgs>()
    private val imageAdapter = PublicImageAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPublicImageDetailsBinding.inflate(inflater, container, false)
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
        binding.backImageButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun configRecycler() {
        PagerSnapHelper().attachToRecyclerView(binding.imageRecycler)
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        imageAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(
                positionStart: Int,
                itemCount: Int
            ) {
                layoutManager.scrollToPosition(arguments.clickedItemCount)
            }
        })


        binding.imageRecycler.apply {
            adapter = imageAdapter
            layoutManager = this@PublicRouteDetailsImageFragment.layoutManager
        }

        imageAdapter.submitList(arguments.imageList.asList())
    }
}