package com.example.gh_coursework.ui.private_point.tag_dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gh_coursework.databinding.DialogFilterPointsByTagsBinding
import com.example.gh_coursework.ui.point_details.model.PointTagModel
import com.example.gh_coursework.ui.private_point.adapter.PointFilterTagAdapter
import com.example.gh_coursework.ui.private_route.tag_dialog.RouteFilterByTagDialogFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PointFilterByTagDialogFragment : DialogFragment() {
    companion object {
        val REQUEST_KEY = "point_filter_by_tag_dialog_fragment"
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

            addTagEditText.setOnFocusChangeListener { view, b ->
                if (b) {
                    tagRecycler.visibility = View.GONE
                    filterByTagsButton.visibility = View.GONE
                    clearTagsDialogButton.visibility = View.GONE
                    cancelTagAddingButton.visibility = View.VISIBLE
                } else {
                    tagRecycler.visibility = View.VISIBLE
                    filterByTagsButton.visibility = View.VISIBLE
                    clearTagsDialogButton.visibility = View.VISIBLE
                    cancelTagAddingButton.visibility = View.GONE

                    val inputManager =
                        context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(
                        view.windowToken, 0
                    )
                }
            }

            cancelTagAddingButton.setOnClickListener {
                addTagEditText.clearFocus()
                addTagEditText.text.clear()
            }

            addTagButton.setOnClickListener {
                addTagEditText.clearFocus()

                val tagName = addTagEditText.text.toString()

                if (tagName.isNotBlank() && tagName.isNotEmpty()) {
                    viewModelFilterBy.addTag(PointTagModel(null, tagName))
                }

                addTagEditText.text.clear()
            }
        }
    }
}