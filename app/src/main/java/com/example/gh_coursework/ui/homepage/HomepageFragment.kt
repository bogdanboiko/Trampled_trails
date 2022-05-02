package com.example.gh_coursework.ui.homepage

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
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
import com.example.gh_coursework.ui.homepage.data.SyncingProgressState
import com.example.gh_coursework.ui.themes.MyAppTheme
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

interface LoginCallback {
    fun onSuccessLogin()
    fun onSuccessLogOut()
}

interface GetSyncStateCallback {
    fun getStateSubscribeTo(): SharedFlow<SyncingProgressState>
}

class HomepageFragment : ThemeFragment(), HomepageCallback {

    private lateinit var binding: FragmentHomepageBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var homepageAdapter: HomepageAdapter
    private lateinit var theme: MyAppTheme
    private var firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var loginCallback: LoginCallback? = null
    private var syncStateCallback: GetSyncStateCallback? = null

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.signInResult(res)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        loginCallback = context as? LoginCallback
        syncStateCallback = context as? GetSyncStateCallback
    }

    override fun onDetach() {
        super.onDetach()

        loginCallback = null
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

            lifecycleScope.launch {
                syncStateCallback?.getStateSubscribeTo()?.collect { state ->
                    when(state) {
                        is SyncingProgressState.Loading -> switchSpinnerVisibility(View.VISIBLE)
                        is SyncingProgressState.FinishedSync -> switchSpinnerVisibility(View.GONE)
                        else -> switchSpinnerVisibility(View.GONE)
                    }
                }
            }
            loginCallback?.onSuccessLogin()
        } else {
            Log.e("MainActivity.kt", "Error logging in " + response?.error?.errorCode)
        }
    }

    private fun switchSpinnerVisibility(visibility: Int) {
        binding.dataSyncProgressBar.visibility = visibility
        binding.loadBackground.visibility = visibility
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
            loginCallback?.onSuccessLogOut()
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