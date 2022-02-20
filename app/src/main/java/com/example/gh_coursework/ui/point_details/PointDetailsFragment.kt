package com.example.gh_coursework.ui.point_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gh_coursework.R
import com.example.gh_coursework.databinding.FragmentPointDetailsBinding

interface OnSwitchActivityLayoutVisibility {
    fun switchActivityLayoutState(state: Int)
}

class PointDetailsFragment : Fragment(R.layout.fragment_point_details) {
    private lateinit var binding: FragmentPointDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPointDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as OnSwitchActivityLayoutVisibility).switchActivityLayoutState(View.GONE)
    }

    override fun onDetach() {
        (activity as OnSwitchActivityLayoutVisibility).switchActivityLayoutState(View.VISIBLE)
        super.onDetach()
    }
}