package com.example.gh_coursework.ui.point_details

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.gh_coursework.R
import com.example.gh_coursework.databinding.FragmentPointDetailsBinding
import com.example.gh_coursework.ui.point_details.adapter.DeleteImage
import com.example.gh_coursework.ui.point_details.adapter.ImageAdapter
import com.example.gh_coursework.ui.point_details.model.PointDetailsModel
import com.example.gh_coursework.ui.point_details.model.PointImageModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File
import java.io.FileOutputStream
import java.util.*


class PointDetailsFragment : Fragment(R.layout.fragment_point_details), DeleteImage {
    private val arguments by navArgs<PointDetailsFragmentArgs>()
    private val viewModel: PointDetailsViewModel by viewModel { parametersOf(arguments.pointId) }
    private lateinit var binding: FragmentPointDetailsBinding
    private val imageAdapter = ImageAdapter(this)

    private val imageTakerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val imageList = data?.clipData
                if (imageList != null) {
                    val imageUriList = mutableListOf<PointImageModel>()

                    for (i in 0 until imageList.itemCount) {
                        imageUriList.add(createPointImageModel(imageList.getItemAt(i).uri))
                    }

                    viewModel.addPointImageList(imageUriList)
                } else {
                    val imageUri = data?.data

                    if (imageUri != null) {
                        viewModel.addPointImageList(listOf(createPointImageModel(imageUri)))
                    }
                }
            }
        }

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
        configImageRecycler()
        configData()
    }

    private fun configImageRecycler() {
        PagerSnapHelper().attachToRecyclerView(binding.imageRecycler)
        binding.imageRecycler.apply {
            adapter = imageAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun configData() {
        with(binding) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.pointDetails.collect {
                    pointCaptionText.setText(it?.caption)
                    pointDescriptionText.setText(it?.description)
                    imageAdapter.submitList(it?.imageList)
                    Log.e("e", it?.imageList.toString())
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
                transitionToGallery.action = Intent.ACTION_OPEN_DOCUMENT
                transitionToGallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                transitionToGallery.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                imageTakerLauncher.launch(
                    Intent.createChooser(
                        transitionToGallery,
                        "Select pictures"
                    )
                )
            }
        }
    }

    override fun deleteImage(tag: PointImageModel) {
        TODO("Not yet implemented")
    }

    private fun createPointImageModel(imageUri: Uri): PointImageModel {
        context?.contentResolver?.openInputStream(imageUri).use {
            val image = Drawable.createFromStream(it, imageUri.toString())
            return PointImageModel(
                arguments.pointId,
                saveToCacheAndGetUri(
                    image.toBitmap((image.intrinsicWidth * 0.9).toInt(),
                        (image.intrinsicHeight * 0.9).toInt()
                    ),
                    Date().time.toString()
                ).toString()
            )
        }
    }

    private fun saveToCacheAndGetUri(bitmap: Bitmap, name: String): Uri {
        val file = saveImgToCache(bitmap, name)
        return getImageUri(file, name)
    }

    private fun saveImgToCache(bitmap: Bitmap, name: String): File {
        val fileName: String = name
        val cachePath = File(context?.cacheDir, "/images")
        cachePath.mkdirs()
        FileOutputStream("$cachePath/$fileName.jpeg").use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }

        return cachePath
    }

    private fun getImageUri(fileDir: File, name: String): Uri {
        val newFile = File(fileDir, "$name.jpeg")
        return newFile.toUri()
    }
}