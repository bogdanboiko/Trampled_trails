package com.example.gh_coursework.ui.route_details.tag_dialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gh_coursework.databinding.DialogTagBinding
import com.example.gh_coursework.databinding.RouteDialogTagBinding
import com.example.gh_coursework.ui.route_details.RouteDetailsFragmentArgs
import com.example.gh_coursework.ui.route_details.adapter.RouteTagAdapter
import com.example.gh_coursework.ui.route_details.model.RouteDetailsModel
import com.example.gh_coursework.ui.route_details.model.RouteTagModel
import com.example.gh_coursework.ui.route_details.model.RouteTagsModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RouteTagDialogFragment : DialogFragment() {

    private lateinit var binding: RouteDialogTagBinding
    private val viewModel: RouteTagDialogViewModel by viewModel { parametersOf(arguments.routeId) }
    private val tagAdapter = RouteTagAdapter()

    private val arguments by navArgs<RouteDetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RouteDialogTagBinding.inflate(inflater, container, false)
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
            Log.e("routeId", arguments.routeId.toString())
            viewModel.tags.collect { defaultTags ->
                tagAdapter.tagsList = defaultTags as MutableList<RouteTagModel>
            }

            viewModel.route.collect { route ->
                Log.e("route", route.toString())
                if (route.tagList.isNotEmpty()) {
                    tagAdapter.checkedTagList = route.tagList as MutableList<RouteTagModel>
                    Log.e("checked", tagAdapter.checkedTagList.toString())
                }
            }
        }
    }

    private fun configView() {
        with(binding) {
            tagRecycler.apply {
                adapter = tagAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }

            cancelTagsDialogButton.setOnClickListener {
                dismiss()
            }

            submitTagsButton.setOnClickListener {
                viewModel.addTagsToRoute(tagAdapter.checkedTagList.map {
                    RouteTagsModel(
                        arguments.routeId,
                        it.tagId
                    )
                })

                viewModel.deleteTagsFromRoute(tagAdapter.uncheckedTagList.map {
                    RouteTagsModel(
                        arguments.routeId,
                        it.tagId
                    )
                })

                tagAdapter.tagsList.clear()
                tagAdapter.checkedTagList.clear()
                tagAdapter.uncheckedTagList.clear()
                dismiss()
            }
        }
    }
}