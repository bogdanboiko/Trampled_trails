package com.example.gh_coursework.ui.public_route.tag_dialog

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
import com.example.gh_coursework.databinding.DialogFilterRoutesByTagsBinding
import com.example.gh_coursework.ui.data.routeTags
import com.example.gh_coursework.ui.public_route.adapter.PublicRouteFilterByTagAdapter
import kotlinx.coroutines.launch

class PublicRouteFilterByTagFragment : DialogFragment() {
    companion object {
        const val REQUEST_KEY = "public_route_filter_by_tag_dialog_fragment"
    }

    private lateinit var binding: DialogFilterRoutesByTagsBinding
    private val tagAdapter = PublicRouteFilterByTagAdapter()
    private val arguments by navArgs<PublicRouteFilterByTagFragmentArgs>()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFilterRoutesByTagsBinding.inflate(inflater, container, false)
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
            tagAdapter.submitList(routeTags)
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