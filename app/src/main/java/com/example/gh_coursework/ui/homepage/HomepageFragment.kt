package com.example.gh_coursework.ui.homepage

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.gh_coursework.R
import com.example.gh_coursework.databinding.FragmentHomepageBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

class HomepageFragment : Fragment(), HomepageCallback {

    private lateinit var binding: FragmentHomepageBinding
    private val homepageAdapter = HomepageAdapter(this as HomepageCallback)
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    private val imageTakerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val updatedUser = UserProfileChangeRequest.Builder()
                    .setPhotoUri(data?.data)
                    .build()

                firebaseUser?.updateProfile(updatedUser)
                Glide.with(requireActivity())
                    .load(data?.data)
                    .placeholder(binding.imgUserIcon.drawable)
                    .error(R.drawable.ic_user)
                    .transform(MultiTransformation(CenterCrop(), CircleCrop()))
                    .into(binding.imgUserIcon)
                } else {
                    Log.e("Fail: ", "to load image")
                }
            }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomepageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecycler()
        configHomepage()
    }

    private fun setUpRecycler() {
        binding.homepageRecycler.apply {
            adapter = homepageAdapter
            layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean = false
            }
        }
    }

    private fun configHomepage() {
        with(binding) {
            if (firebaseUser != null) {
                btnEditUserIcon.visibility = View.VISIBLE
                txtUsername.visibility = View.VISIBLE
                homepageRecycler.visibility = View.VISIBLE
                signInButton.visibility = View.GONE

                txtUsername.text = "Hello, ${firebaseUser?.displayName}!"
                Glide.with(requireActivity())
                    .load(firebaseUser!!.photoUrl)
                    .placeholder(imgUserIcon.drawable)
                    .error(R.drawable.ic_user)
                    .transform(MultiTransformation(CenterCrop(), CircleCrop()))
                    .into(imgUserIcon)

                btnEditUserIcon.setOnClickListener {
                    editUserIcon()
                }
            } else {
                btnEditUserIcon.visibility = View.GONE
                txtUsername.visibility = View.GONE
                homepageRecycler.visibility = View.GONE
                signInButton.visibility = View.VISIBLE

                Glide.with(requireActivity())
                    .load(R.drawable.ic_user)
                    .placeholder(imgUserIcon.drawable)
                    .transform(MultiTransformation(CenterCrop(), CircleCrop()))
                    .into(imgUserIcon)

                binding.signInButton.setOnClickListener {
                    signIn()
                }
            }
        }
    }

    private fun editUserIcon() {
        val transitionToGallery = Intent()
        transitionToGallery.type = "image/*"
        transitionToGallery.action = Intent.ACTION_OPEN_DOCUMENT
        imageTakerLauncher.launch(
            Intent.createChooser(
                transitionToGallery,
                "Select pictures"
            )
        )
    }

    private fun signIn() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build()

        )
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setIsSmartLockEnabled(false)
            .build()

        signInLauncher.launch(signInIntent)
    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.signInResult(res)
    }

    private fun signInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == ComponentActivity.RESULT_OK) {
            firebaseUser = FirebaseAuth.getInstance().currentUser
            firebaseUser?.let {
                configHomepage()
            }
        } else {
            Log.e("MainActivity.kt", "Error logging in " + response?.error?.errorCode)
        }
    }

    override fun onEditClick() {
        with(binding) {
            btnEditUserIcon.visibility = View.GONE
            txtUsername.visibility = View.GONE
            homepageRecycler.visibility = View.GONE
            btnSave.visibility = View.VISIBLE
            editUsername.visibility = View.VISIBLE

            editUsername.hint = firebaseUser?.displayName.toString()
            editUsername.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    editUsername.hint = ""
                }
            }

            btnSave.setOnClickListener {
                if (editUsername.text.isBlank()) {
                    val updatedUser = UserProfileChangeRequest.Builder()
                        .setDisplayName("username")
                        .build()

                    firebaseUser?.updateProfile(updatedUser)
                    txtUsername.text = "Hello, username!"
                    editUsername.text.clear()
                } else {
                    val updatedUser = UserProfileChangeRequest.Builder()
                        .setDisplayName(editUsername.text.toString())
                        .build()

                    firebaseUser?.updateProfile(updatedUser)
                    txtUsername.text = "Hello, ${editUsername.text}!"
                    editUsername.text.clear()
                }

                btnEditUserIcon.visibility = View.VISIBLE
                txtUsername.visibility = View.VISIBLE
                homepageRecycler.visibility = View.VISIBLE
                editUsername.visibility = View.GONE
                btnSave.visibility = View.GONE
            }
        }
    }

    override fun onLogOutClick() {
        AuthUI.getInstance().signOut(requireContext()).addOnSuccessListener {
            val id = findNavController().currentDestination?.id
            findNavController().popBackStack(id!!, true)
            findNavController().navigate(id)
        }
    }
}