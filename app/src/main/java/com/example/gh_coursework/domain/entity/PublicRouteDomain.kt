package com.example.gh_coursework.domain.entity

data class PublicRouteDomain(
    val documentId: String,
    val name: String,
    val description: String,
    val tagsList: List<String>,
    val imageList: List<String>
)