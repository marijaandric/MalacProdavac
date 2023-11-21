package com.example.front.model.DTO

data class FiltersDTO(
    val userId: Int,
    val categories: List<Int>? = null,
    val rating: Int? = null,
    val open: Boolean? = null,
    val range: Int? = null,
    val location: String? = null,
    val sort: Int,
    val search: String? = null,
    val page: Int,
    val currLat: Float?,
    val currLon: Float?
)
