package com.example.gh_coursework.ui.point_details

import android.app.Activity
import android.content.Intent
import android.graphics.ImageDecoder
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gh_coursework.R
import com.example.gh_coursework.databinding.FragmentPointDetailsBinding
import com.example.gh_coursework.ui.point_details.model.PointDetailsModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PointDetailsFragment : Fragment(R.layout.fragment_point_details) {
    private val imageTakerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data?.clipData == null) {
                    val imageUri = data?.data

                    if (imageUri != null) {
                        val inStream = activity?.contentResolver?.openInputStream(imageUri)
                        inStream.use {
                            val image = Drawable.createFromStream(inStream, imageUri.toString())
                            binding.pointImage.setImageBitmap(image.toBitmap())
                        }
                    }
                } else {
                    val imageList = data.clipData
                }
            }
        }
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
                    //  it?.tagList?.let { it1 -> dialog.updatePointTagList(it1) }
                }
            }
        }
    }

    private fun configConfirmButton() {
        with(binding) {
            confirmEditButton.setOnClickListener {
                it.visibility = View.GONE
                pointDetailsEditButton.visibility = View.VISIBLE
                pointCaptionText.isEnabled = false
                pointDescriptionText.isEnabled = false
                pointCaptionText.hint = ""
                pointDescriptionText.hint = ""
                viewModel.addPointDetails(
                    PointDetailsModel(
                        arguments.pointId,
                        emptyList(),
                        pointCaptionText.text.toString(),
                        pointDescriptionText.text.toString()
                    )
                )
            }
        }
    }

    private fun configTagButton() {
        binding.pointDetailsTagButton.setOnClickListener {
            findNavController().navigate(
                PointDetailsFragmentDirections.actionPrivateDetailsFragmentToPointTagDialogFragment(
                    arguments.pointId
                )
            )
        }
    }

    private fun configToolBar() {
        with(binding) {
            pointDetailsEditButton.setOnClickListener {
                it.visibility = View.GONE
                confirmEditButton.visibility = View.VISIBLE
                pointCaptionText.isEnabled = true
                pointDescriptionText.isEnabled = true
                pointCaptionText.hint = "Put in point caption..."
                pointDescriptionText.hint = "Put in point description..."
            }

            pointImageAddButton.setOnClickListener {
                val transitionToGallery = Intent()
                transitionToGallery.type = "image/*"
                transitionToGallery.action = Intent.ACTION_GET_CONTENT
                transitionToGallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                imageTakerLauncher.launch(Intent.createChooser(transitionToGallery, "Select pictures"))
            }
        }
    }
}