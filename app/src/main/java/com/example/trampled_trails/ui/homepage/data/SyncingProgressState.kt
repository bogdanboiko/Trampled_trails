package com.example.trampled_trails.ui.homepage.data

sealed class SyncingProgressState {
    object Loading : SyncingProgressState()
    object FinishedSync : SyncingProgressState()
}
