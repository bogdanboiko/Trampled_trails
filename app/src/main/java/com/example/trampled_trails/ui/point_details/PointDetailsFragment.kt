package com.example.trampled_trails.ui.point_details

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
import com.example.trampled_trails.R
import com.example.trampled_trails.databinding.FragmentPointDetailsBinding
import com.example.trampled_trails.ui.point_details.model.PointDetailsModel
import com.example.trampled_trails.ui.private_image_details.adapter.ImagesInDetailsAdapter
import com.example.trampled_trails.ui.private_image_details.model.ImageModel.PointImageModel
import com.example.trampled_trails.ui.themes.MyAppTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File
import java.io.FileOutputStream
import java.util.*

class PointDetailsFragment : ThemeFragment() {

    private lateinit var binding: FragmentPointDetailsBinding
    private lateinit var theme: MyAppTheme

    private lateinit var layoutManager: LinearLayoutManager

    private val arguments by navArgs<PointDetailsFragmentArgs>()
    private val viewModel: PointDetailsViewModel by viewModel { parametersOf(arguments.pointId) }

    private val imageAdapter = ImagesInDetailsAdapter {
        findNavController().navigate(
            PointDetailsFragmentDirections.actionPointDetailsFragmentToPrivateImageDetails(
                arguments.pointId,
                layoutManager.findFirstVisibleItemPosition()
            )
        )
    }

    private val imageTakerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.clipData?.let { imageList ->
                    val imageUriList = mutableListOf<PointImageModel>()

                    for (i in 0 until imageList.itemCount) {
                        imageUriList.add(createPointImageModel(imageList.getItemAt(i).uri))
                    }

                    viewModel.addPointImageList(imageUriList)

                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

    override fun syncTheme(appTheme: AppTheme) {
        theme = appTheme as MyAppTheme

        with(binding) {
            DrawableCompat.wrap(pointDetailsAppBar.background).setTint(theme.colorSecondary(requireContext()))
            pointDetailsCollapsingToolbar.setContentScrimColor(theme.colorSecondary(requireContext()))

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
            layoutManager = this@PointDetailsFragment.layoutManager
        }
    }

    private fun configData() {
        with(binding) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.pointDetails.collect { pointDetailsModel ->
                    if (pointDetailsModel != null) {
                        pointCaptionText.setText(pointDetailsModel.caption)
                        pointDescriptionText.setText(pointDetailsModel.description)
                        imageAdapter.submitList(pointDetailsModel.imageList)

                        if (pointDetailsModel.caption.isEmpty() && pointDetailsModel.description.isEmpty()) {
                            emptyDataPlaceholder.visibility = View.VISIBLE
                        } else {
                            emptyDataPlaceholder.visibility = View.INVISIBLE
                        }

                        if (pointDetailsModel.imageList.isNotEmpty()) {
                            imageRecycler.visibility = View.VISIBLE
                        } else {
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
                pointDetailsEditButton.visibility = View.VISIBLE
                pointDetailsTagButton.visibility = View.VISIBLE
                pointImageAddButton.visibility = View.VISIBLE

                pointCaptionText.isEnabled = false
                pointDescriptionText.isEnabled = false
                pointCaptionText.hint = ""
                pointDescriptionText.hint = ""

                viewModel.addPointDetails(
                    PointDetailsModel(
                        pointId = arguments.pointId,
                        caption = pointCaptionText.text.toString(),
                        description = pointDescriptionText.text.toString()
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
                emptyDataPlaceholder.visibility = View.INVISIBLE
                confirmEditButton.visibility = View.VISIBLE
                pointDetailsTagButton.visibility = View.GONE
                pointImageAddButton.visibility = View.GONE

                pointCaptionText.isEnabled = true
                pointDescriptionText.isEnabled = true
                pointCaptionText.hint = resources.getString(R.string.hint_caption)
                pointDescriptionText.hint = resources.getString(R.string.hint_description)
            }

            pointImageAddButton.setOnClickListener {
                Intent().let { transitionToGallery ->
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

            backImageButton.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun createPointImageModel(imageUri: Uri): PointImageModel {
        context?.contentResolver?.openInputStream(imageUri).use {
            Drawable.createFromStream(it, imageUri.toString()).let { image ->
                return PointImageModel(
                    arguments.pointId,
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