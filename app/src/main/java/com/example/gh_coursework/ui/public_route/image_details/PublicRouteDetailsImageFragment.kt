package com.example.gh_coursework.ui.public_route.image_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.gh_coursework.R
import com.example.gh_coursework.databinding.FragmentPublicImageDetailsBinding

class PublicRouteDetailsImageFragment : Fragment(R.layout.fragment_public_image_details) {
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var binding: FragmentPublicImageDetailsBinding
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

    private fun configToolbar() {

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