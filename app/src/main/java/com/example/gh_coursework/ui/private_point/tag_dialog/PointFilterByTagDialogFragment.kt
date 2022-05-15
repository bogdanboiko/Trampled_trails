package com.example.gh_coursework.ui.private_point.tag_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gh_coursework.databinding.DialogFilterPointsByTagsBinding
import com.example.gh_coursework.ui.private_point.adapter.PointFilterTagAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PointFilterByTagDialogFragment : DialogFragment() {
    companion object {
        const val REQUEST_KEY = "point_filter_by_tag_dialog_fragment"
    }

    private lateinit var binding: DialogFilterPointsByTagsBinding
    private val viewModelFilterBy: PointFilterByTagDialogViewModel by viewModel()
    private val tagAdapter = PointFilterTagAdapter()
    private val arguments by navArgs<PointFilterByTagDialogFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFilterPointsByTagsBinding.inflate(inflater, container, false)
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewModelFilterBy.tags.collect { defaultTags ->
                tagAdapter.submitList(defaultTags)
            }
        }
    }

    private fun configView() {
        with(binding) {
            tagRecycler.apply {
                adapter = tagAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }

            tagAdapter.setFilteredTags(arguments.checkedTags.toMutableList())

            clearTagsDialogButton.setOnClickListener {
                tagAdapter.setFilteredTags(mutableListOf())
                setFragmentResult(REQUEST_KEY, bundleOf("tags" to tagAdapter.filterByTagList.toTypedArray()))
                dismiss()
            }

            filterByTagsButton.setOnClickListener {
                setFragmentResult(REQUEST_KEY, bundleOf("tags" to tagAdapter.filterByTagList.toTypedArray()))
                dismiss()
            }
        }
    }
}