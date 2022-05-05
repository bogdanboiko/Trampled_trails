package com.example.gh_coursework.ui.point_details.tag_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gh_coursework.databinding.DialogTagBinding
import com.example.gh_coursework.ui.point_details.PointDetailsFragmentArgs
import com.example.gh_coursework.ui.point_details.adapter.TagAdapter
import com.example.gh_coursework.ui.point_details.model.PointsTagsModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TagDialogFragment : DialogFragment() {
    private lateinit var binding: DialogTagBinding
    private val tagAdapter = TagAdapter()
    private val arguments by navArgs<PointDetailsFragmentArgs>()
    private val viewModel: TagDialogViewModel by viewModel { parametersOf(arguments.pointId) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogTagBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configTagRecycler()
        configView()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun configTagRecycler() {
        binding.tagRecycler.apply {
            adapter = tagAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.tags.collect {
                tagAdapter.submitList(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.pointTags.collect {
                tagAdapter.insertCheckedTagList(it)
            }
        }
    }

    private fun configView() {
        with(binding) {
            cancelTagsDialogButton.setOnClickListener {
                dismiss()
            }

            submitTagsButton.setOnClickListener {
                viewModel.addTagsToPoint(tagAdapter.addTagList.map {
                    PointsTagsModel(
                        arguments.pointId,
                        it.tagId!!
                    )
                })

                viewModel.removeTagsToPoint(tagAdapter.removeTagList.map {
                    PointsTagsModel(
                        arguments.pointId,
                        it.tagId!!
                    )
                })

                tagAdapter.clearTagsLists()
                dismiss()
            }
        }
    }
}