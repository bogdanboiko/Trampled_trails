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
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeFragment
import com.example.gh_coursework.R
import com.example.gh_coursework.databinding.FragmentRouteDetailsBinding
import com.example.gh_coursework.ui.private_image_details.adapter.ImagesInDetailsAdapter
import com.example.gh_coursework.ui.private_image_details.model.ImageModel
import com.example.gh_coursework.ui.private_image_details.model.ImageModel.RouteImageModel
import com.example.gh_coursework.ui.route_details.model.RouteCompleteModel
import com.example.gh_coursework.ui.route_details.model.RouteDetailsModel
import com.example.gh_coursework.ui.themes.MyAppTheme
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File
import java.io.FileOutputStream
import java.util.*

class RouteDetailsFragment : ThemeFragment() {

    private lateinit var binding: FragmentRouteDetailsBinding
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var theme: MyAppTheme
    private val arguments by navArgs<RouteDetailsFragmentArgs>()
    private val viewModel: RouteDetailsViewModel by viewModel { parametersOf(arguments.routeId) }
    private val imageAdapter = ImagesInDetailsAdapter {
        findNavController().navigate(
            RouteDetailsFragmentDirections.actionRouteDetailsFragmentToPrivateRouteImageDetails(
                arguments.routeId,
                layoutManager.findFirstVisibleItemPosition()
            )
        )
    }

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
        configTagButton()
        configImageRecycler()
        configData()
    }

    override fun syncTheme(appTheme: AppTheme) {
        theme = appTheme as MyAppTheme

        with(binding) {
            DrawableCompat.wrap(routeDetailsAppBar.background)
                .setTint(theme.colorSecondary(requireContext()))
            routeDetailsCollapsingToolbar.setContentScrimColor(theme.colorSecondary(requireContext()))

            if (theme.id() == 0) {
                gradient.setImageResource(R.drawable.fg_gradient_toolbar_light)
            } else {
                gradient.setImageResource(R.drawable.fg_gradient_toolbar_dark)
            }
        }
    }

    private fun configImageRecycler() {
        PagerSnapHelper().attachToRecyclerView(binding.imageRecycler)
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.imageRecycler.apply {
            adapter = imageAdapter
            layoutManager = this@RouteDetailsFragment.layoutManager
        }
    }

    private fun configData() {
        with(binding) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.route.combine(viewModel.images) { route, images ->
                    RouteCompleteModel(route, images)
                }.collect { route ->
                    val routeImages = mutableListOf<ImageModel>()

                    routeCaptionText.setText(route.route.name)
                    routeDescriptionText.setText(route.route.description)

                    if (route.route.name?.isEmpty() == true && route.route.description?.isEmpty() == true) {
                        emptyDataPlaceholder.visibility = View.VISIBLE
                    } else {
                        emptyDataPlaceholder.visibility = View.GONE
                    }

                    route.pointsImagesList.forEach { image ->
                        routeImages.addAll(image.imagesList)
                    }

                    routeImages.addAll(route.route.imageList)

                    if (routeImages.isNotEmpty()) {
                        imageAdapter.submitList(routeImages)
                        imageRecycler.visibility = View.VISIBLE
                    } else {
                        imageRecycler.visibility = View.GONE
                    }

                    configConfirmButton(route.route.isPublic)
                }
            }
        }
    }

    private fun configConfirmButton(isPublic: Boolean) {
        with(binding) {
            confirmEditButton.setOnClickListener {
                it.visibility = View.GONE
                routeDetailsEditButton.visibility = View.VISIBLE
                routeDetailsTagButton.visibility = View.VISIBLE
                routeImageAddButton.visibility = View.VISIBLE
                routeCaptionText.isEnabled = false
                routeDescriptionText.isEnabled = false
                routeCaptionText.hint = ""
                routeDescriptionText.hint = ""
                viewModel.updateRouteDetails(
                    RouteDetailsModel(
                        arguments.routeId,
                        routeCaptionText.text.toString(),
                        routeDescriptionText.text.toString(),
                        emptyList(),
                        emptyList(),
                        isPublic
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
                emptyDataPlaceholder.visibility = View.GONE
                confirmEditButton.visibility = View.VISIBLE
                routeDetailsTagButton.visibility = View.GONE
                routeImageAddButton.visibility = View.GONE
                routeCaptionText.isEnabled = true
                routeDescriptionText.isEnabled = true
                routeCaptionText.hint = resources.getString(R.string.hint_caption)
                routeDescriptionText.hint = resources.getString(R.string.hint_description)
            }

            routeImageAddButton.setOnClickListener {
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

            backImageButton.setOnClickListener {
                findNavController().popBackStack()
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
                ).toString(),
                false
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