package com.example.gh_coursework.ui.homepage

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeFragment
import com.example.gh_coursework.R
import com.example.gh_coursework.databinding.FragmentHomepageBinding
import com.example.gh_coursework.ui.themes.MyAppTheme
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

class HomepageFragment : HomepageCallback, ThemeFragment() {

    private lateinit var binding: FragmentHomepageBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var homepageAdapter: HomepageAdapter
    private lateinit var theme: MyAppTheme
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.signInResult(res)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        homepageAdapter = HomepageAdapter(this as HomepageCallback, sharedPreferences)
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
                txtUsername.visibility = View.VISIBLE
                homepageRecycler.visibility = View.VISIBLE
                btnSingIn.visibility = View.GONE

                txtUsername.text = "Hello, ${firebaseUser?.displayName}!"
            } else {
                txtUsername.visibility = View.GONE
                homepageRecycler.visibility = View.GONE
                btnSingIn.visibility = View.VISIBLE

                btnSingIn.setOnClickListener {
                    signIn()
                }
            }

            Glide.with(requireActivity())
                .load(R.drawable.ic_user)
                .placeholder(binding.imgUserIcon.drawable)
                .transform(MultiTransformation(CenterCrop(), CircleCrop()))
                .into(binding.imgUserIcon)
        }
    }

    private fun signIn() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.FacebookBuilder()
                .setPermissions(listOf("public_profile"))
                .build(),
            AuthUI.IdpConfig.GoogleBuilder()
                .build(),
            AuthUI.IdpConfig.PhoneBuilder()
                .setDefaultCountryIso("ua")
                .build()
        )

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setIsSmartLockEnabled(false)
            .setLogo(R.drawable.ic_user)
            .build()

        signInLauncher.launch(signInIntent)
    }

    private fun signInResult(result: FirebaseAuthUIAuthenticationResult) {
        val response = result.idpResponse
        if (result.resultCode == ComponentActivity.RESULT_OK) {
            firebaseUser = FirebaseAuth.getInstance().currentUser
            configHomepage()
        } else {
            Log.e("MainActivity.kt", "Error logging in " + response?.error?.errorCode)
        }
    }

    override fun onEditClick() {
        with(binding) {
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

    override fun syncTheme(appTheme: AppTheme) {
        theme = appTheme as MyAppTheme
        with(binding) {
            root.setBackgroundColor(theme.colorPrimary(requireContext()))
            txtUsername.setTextColor(theme.colorSecondaryVariant(requireContext()))
        }
    }
}