package com.example.gh_coursework.ui.homepage.data

sealed class SyncingProgressState {
    object Loading : SyncingProgressState()
    object FinishedSync : SyncingProgressState()
}
