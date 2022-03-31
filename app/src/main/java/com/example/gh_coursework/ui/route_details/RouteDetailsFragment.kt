package com.example.gh_coursework.ui.route_details

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
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
import com.example.gh_coursework.databinding.FragmentRouteDetailsBinding
import com.example.gh_coursework.ui.point_details.model.PointImageModel
import com.example.gh_coursework.ui.route_details.adapter.RouteImageAdapter
import com.example.gh_coursework.ui.route_details.mapper.mapPointImageToRouteImageModel
import com.example.gh_coursework.ui.route_details.model.RouteDetailsModel
import com.example.gh_coursework.ui.route_details.model.RouteImageModel
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File
import java.io.FileOutputStream
import java.util.*

class RouteDetailsFragment : Fragment(R.layout.fragment_route_details) {

    private lateinit var binding: FragmentRouteDetailsBinding
    private lateinit var layoutManager: LinearLayoutManager
    private val arguments by navArgs<RouteDetailsFragmentArgs>()
    private val viewModel: RouteDetailsViewModel by viewModel { parametersOf(arguments.routeId) }
    private val imageAdapter = RouteImageAdapter {
        findNavController().navigate(
            RouteDetailsFragmentDirections.actionRouteDetailsFragmentToPrivateRouteImageDetails(
                arguments.routeId,
                layoutManager.findFirstVisibleItemPosition()
            )
        )
    }

    private var pointsImages = mutableListOf<PointImageModel>()

    private val imageTakerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val imageList = data?.clipData
                if (imageList != null) {
                    val imageUriList = mutableListOf<RouteImageModel>()

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
    ): View {
        binding = FragmentRouteDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configToolBar()
        configConfirmButton()
        configTagButton()
        configImageRecycler()
        fetchRoutePointsImages()
        configData()
    }

    private fun configImageRecycler() {
        PagerSnapHelper().attachToRecyclerView(binding.imageRecycler)
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.imageRecycler.apply {
            adapter = imageAdapter
            layoutManager = this@RouteDetailsFragment.layoutManager
        }
    }

    private fun fetchRoutePointsImages() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.images.collect { imagesList ->
                if (imagesList.isNotEmpty()) {
                    imagesList.forEach { image ->
                        pointsImages.addAll(image.imagesList)
                    }
                }
            }
        }
    }

    private fun configData() {
        with(binding) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.route.collect {
                    routeCaptionText.setText(it.name)
                    routeDescriptionText.setText(it.description)

                    if (it.name?.isEmpty() == true && it.description?.isEmpty() == true) {
                        emptyDataPlaceholder.visibility = View.VISIBLE
                    } else {
                        emptyDataPlaceholder.visibility = View.INVISIBLE
                    }

                    when {
                        it.imageList.isNotEmpty() -> {
                            imageAdapter.submitList(it.imageList)
                            imageRecycler.visibility = View.VISIBLE
                        }
                        pointsImages.isNotEmpty() -> {
                            imageAdapter.submitList(listOf(pointsImages.map(::mapPointImageToRouteImageModel), it.imageList).flatten())
                            imageRecycler.visibility = View.VISIBLE
                        }
                        else -> {
                            imageRecycler.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun configConfirmButton() {
        with(binding) {
            confirmEditButton.setOnClickListener {
                it.visibility = View.GONE
                routeDetailsEditButton.visibility = View.VISIBLE
                routeCaptionText.isEnabled = false
                routeDescriptionText.isEnabled = false
                routeCaptionText.hint = ""
                routeDescriptionText.hint = ""
                viewModel.updateRouteDetails(
                    RouteDetailsModel(
                        arguments.routeId,
                        routeCaptionText.text.toString(),
                        routeDescriptionText.text.toString(),
                        0.0,
                        emptyList(),
                        emptyList()
                    )
                )
            }
        }
    }

    private fun configTagButton() {
        binding.routeDetailsTagButton.setOnClickListener {
            findNavController().navigate(
                RouteDetailsFragmentDirections.actionPrivateDetailsFragmentToRouteTagDialogFragment(
                    arguments.routeId
                )
            )
        }
    }

    private fun configToolBar() {
        with(binding) {
            routeDetailsEditButton.setOnClickListener {
                it.visibility = View.GONE
                emptyDataPlaceholder.visibility = View.INVISIBLE
                confirmEditButton.visibility = View.VISIBLE
                routeCaptionText.isEnabled = true
                routeDescriptionText.isEnabled = true
                routeCaptionText.hint = "Put in point caption..."
                routeDescriptionText.hint = "Put in point description..."
            }

            pointImageAddButton.setOnClickListener {
                val transitionToGallery = Intent()
                transitionToGallery.type = "image/*"
                transitionToGallery.action = Intent.ACTION_OPEN_DOCUMENT
                transitionToGallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                imageTakerLauncher.launch(
                    Intent.createChooser(
                        transitionToGallery,
                        "Select pictures"
                    )
                )
            }
        }
    }

    private fun createPointImageModel(imageUri: Uri): RouteImageModel {

        context?.contentResolver?.openInputStream(imageUri).use {
            val image = Drawable.createFromStream(it, imageUri.toString())

            return RouteImageModel(
                arguments.routeId,
                saveToCacheAndGetUri(
                    image.toBitmap(
                        (image.intrinsicWidth * 0.9).toInt(),
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