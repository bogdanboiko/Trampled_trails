package com.example.gh_coursework.ui.point_details.tag_dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gh_coursework.databinding.DialogTagBinding
import com.example.gh_coursework.ui.point_details.PointDetailsFragmentArgs
import com.example.gh_coursework.ui.point_details.PointDetailsViewModel
import com.example.gh_coursework.ui.point_details.adapter.DeleteTag
import com.example.gh_coursework.ui.point_details.adapter.TagAdapter
import com.example.gh_coursework.ui.point_details.model.PointTagModel
import com.example.gh_coursework.ui.point_details.model.PointsTagsModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TagDialogFragment : DialogFragment(), DeleteTag {
    private val tagAdapter = TagAdapter(this)
    private lateinit var binding: DialogTagBinding
    private val arguments by navArgs<PointDetailsFragmentArgs>()
    private val viewModel: PointDetailsViewModel by viewModel { parametersOf(arguments.pointId) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogTagBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configTagRecycler()
        configView()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun configTagRecycler() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.tags.collect {
                tagAdapter.submitList(it)
            }
        }
    }

    private fun configView() {
        with(binding) {
            tagRecycler.apply {
                adapter = tagAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }

            addTagEditText.setOnFocusChangeListener { view, b ->
                if (b) {
                    tagRecycler.visibility = View.GONE
                    submitTagsButton.visibility = View.GONE
                    cancelTagsDialogButton.visibility = View.GONE
                    cancelTagAddingButton.visibility = View.VISIBLE
                } else {
                    tagRecycler.visibility = View.VISIBLE
                    submitTagsButton.visibility = View.VISIBLE
                    cancelTagsDialogButton.visibility = View.VISIBLE
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
                    viewModel.addTag(PointTagModel(null, tagName))
                }

                addTagEditText.text.clear()
            }

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

    override fun deleteTag(tag: PointTagModel) {
        viewModel.deletePointTag(tag)
    }
}