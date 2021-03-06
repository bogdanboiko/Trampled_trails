package com.example.trampled_trails.ui.homepage

import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeFragment
import com.example.trampled_trails.R
import com.example.trampled_trails.databinding.FragmentHomepageBinding
import com.example.trampled_trails.ui.homepage.data.SyncingProgressState
import com.example.trampled_trails.ui.themes.MyAppTheme
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
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
    private var syncState: SyncingProgressState = SyncingProgressState.FinishedSync

    private val signInLauncher =
        registerForActivityResult(FirebaseAuthUIActivityResultContract()) { res ->
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

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
               when (syncState) {
                   is SyncingProgressState.FinishedSync -> findNavController().popBackStack()
                   else -> return
               }
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, callback)
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
        val circularProgressDrawable =  CircularProgressDrawable(binding.root.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

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
                .placeholder(circularProgressDrawable)
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
        if (result.resultCode == ComponentActivity.RESULT_OK) {
            firebaseUser = FirebaseAuth.getInstance().currentUser
            configHomepage()

            lifecycleScope.launch {
                syncStateCallback?.getStateSubscribeTo()?.collect { state ->
                    syncState = state
                    when (state) {
                        is SyncingProgressState.Loading -> binding.progressBar.loadBackground.visibility =
                            View.VISIBLE
                        is SyncingProgressState.FinishedSync -> binding.progressBar.loadBackground.visibility =
                            View.GONE
                    }
                }
            }
            loginCallback?.onSuccessLogin()
        } else {
            Log.e("MainActivity.kt", "Error logging in " + result.idpResponse?.error?.errorCode)
        }
    }

    override fun onEditClick() {
        with(binding) {
            txtUsername.visibility = View.GONE
            homepageRecycler.visibility = View.GONE
            editUsername.visibility = View.VISIBLE
            btnSave.visibility = View.VISIBLE

            editUsername.hint = firebaseUser?.displayName.toString()
            editUsername.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    editUsername.hint = ""
                }
            }

            btnSave.setOnClickListener {
                if (editUsername.text.isBlank()) {
                    UserProfileChangeRequest.Builder()
                        .setDisplayName("username")
                        .build().let { updatedUser -> firebaseUser?.updateProfile(updatedUser) }

                    txtUsername.text = "Hello, username!"
                    editUsername.text.clear()
                } else {
                    UserProfileChangeRequest.Builder()
                        .setDisplayName(editUsername.text.toString())
                        .build().let { updatedUser -> firebaseUser?.updateProfile(updatedUser) }

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
            findNavController().popBackStack()
            loginCallback?.onSuccessLogOut()
        }
    }

    override fun syncTheme(appTheme: AppTheme) {
        theme = appTheme as MyAppTheme

        with(binding) {
            root.setBackgroundColor(theme.colorPrimary(requireContext()))
            txtUsername.setTextColor(theme.colorSecondaryVariant(requireContext()))
            progressBar.dataSyncProgressBar.indeterminateTintList =
                ColorStateList.valueOf(theme.colorSecondary(requireContext()))
            progressBar.txtProgressBar.setTextColor(theme.colorSecondaryVariant(requireContext()))
        }
    }
}