package com.example.gh_coursework.ui.homepage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
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


class HomepageFragment : Fragment() {

    private lateinit var binding: FragmentHomepageBinding
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

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

        configHomepage(firebaseUser != null)

        binding.signInButton.setOnClickListener {
            signIn()
        }
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
                configHomepage(firebaseUser != null)
            }
        } else {
            Log.e("MainActivity.kt", "Error logging in " + response?.error?.errorCode)

        }
    }

    private fun configHomepage(isUserLoggedIn: Boolean) {
        with(binding) {
            if (isUserLoggedIn) {
                txtUsername.text = "Hello, ${firebaseUser!!.displayName}!"
                editIconButton.visibility = View.VISIBLE
                signInButton.visibility = View.GONE

                Glide.with(requireActivity())
                    .load(firebaseUser!!.photoUrl)
                    .placeholder(imgUserIcon.drawable)
                    .error(R.drawable.ic_user)
                    .transform(MultiTransformation(CenterCrop(), CircleCrop()))
                    .into(imgUserIcon)
            } else {
                txtUsername.text = ""
                signInButton.visibility = View.VISIBLE
                editIconButton.visibility = View.GONE

                Glide.with(requireActivity())
                    .load(R.drawable.ic_user)
                    .placeholder(imgUserIcon.drawable)
                    .transform(MultiTransformation(CenterCrop(), CircleCrop()))
                    .into(imgUserIcon)
            }
        }
    }
}