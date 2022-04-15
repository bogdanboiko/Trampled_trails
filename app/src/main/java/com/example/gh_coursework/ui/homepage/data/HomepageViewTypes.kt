package com.example.gh_coursework.ui.homepage.data

sealed class HomepageViewTypes {
    data class DefaultItem(val viewType: Int, val image: Int, val text: String)
    data class SwitchableItem(val viewType: Int, val image: Int, val text: String)
}

