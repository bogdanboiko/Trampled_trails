package com.example.gh_coursework.ui.point_details

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gh_coursework.R
import com.example.gh_coursework.databinding.DialogTagBinding
import com.example.gh_coursework.databinding.FragmentPointDetailsBinding
import com.example.gh_coursework.ui.point_details.adapter.TagAdapter
import com.example.gh_coursework.ui.point_details.model.PointDetailsModel
import com.example.gh_coursework.ui.point_details.model.PointTagModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

interface OnSwitchActivityLayoutVisibility {
    fun switchActivityLayoutState(state: Int)
}

class PointDetailsFragment : Fragment(R.layout.fragment_point_details) {
    private lateinit var tagAdapter: TagAdapter
    private val arguments by navArgs<PointDetailsFragmentArgs>()
    private val viewModel: PointDetailsViewModel by viewModel { parametersOf(arguments.pointId) }
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
        configTagButton()
        configData()
    }

    private fun configData() {
        with(binding) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.pointDetails.collect {
                    pointCaptionText.setText(it?.caption)
                    pointDescriptionText.setText(it?.description)
                }
            }
        }
    }

    private fun configConfirmButton() {
        with(binding) {
            confirmEditButton.setOnClickListener {
                pointCaptionText.isEnabled = false
                pointDescriptionText.isEnabled = false
                confirmEditButton.visibility = View.INVISIBLE
                addTagButton.visibility = View.INVISIBLE
                pointCaptionText.hint = ""
                pointDescriptionText.hint = ""
                viewModel.addPointDetails(
                    PointDetailsModel(
                        arguments.pointId,
                        "default",
                        pointCaptionText.text.toString(),
                        pointDescriptionText.text.toString()
                    )
                )
            }
        }
    }

    private fun configTagButton() {
        binding.addTagButton.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val dialogBinding = DialogTagBinding.inflate(layoutInflater)
            tagAdapter = TagAdapter()

            dialogBinding.tagRecycler.apply {
                adapter = tagAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }

            dialogBinding.addTagButton.setOnClickListener {
                val tagName = dialogBinding.addTagEditText.text.toString()

                if (tagName.isNotBlank() && tagName.isNotEmpty()) {
                    viewModel.addTag(PointTagModel(null, tagName))
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.tags.collect {
                    tagAdapter.setList(it)
                }
            }

            builder.setView(dialogBinding.root)
            builder.create().show()
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
                addTagButton.visibility = View.VISIBLE
            }
        }
    }

    override fun onDetach() {
        (activity as OnSwitchActivityLayoutVisibility).switchActivityLayoutState(View.VISIBLE)
        super.onDetach()
    }
}