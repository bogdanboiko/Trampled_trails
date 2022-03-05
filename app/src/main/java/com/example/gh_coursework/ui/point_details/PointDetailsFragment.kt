package com.example.gh_coursework.ui.point_details

import android.os.Bundle
import android.text.InputType
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
        configToolBar()
        configConfirmButton()
    }

    private fun configConfirmButton() {
        with(binding) {
            confirmEditButton.setOnClickListener {
                pointCaptionText.isEnabled = false
                pointDescriptionText.isEnabled = false
                confirmEditButton.visibility = View.INVISIBLE
            }
        }
    }

    private fun configToolBar() {
        with(binding) {
            pointDetailsEditButton.setOnClickListener {
                pointCaptionText.isEnabled = true
                pointDescriptionText.isEnabled = true
                pointCaptionText.hint = "Put in point caption..."
                pointDescriptionText.hint = "Put in point description..."
                confirmEditButton.visibility = View.VISIBLE
            }
        }
    }



    override fun onDetach() {
        (activity as OnSwitchActivityLayoutVisibility).switchActivityLayoutState(View.VISIBLE)
        super.onDetach()
    }
}