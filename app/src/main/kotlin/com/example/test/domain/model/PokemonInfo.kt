package com.example.test.domain.model

data class PokemonItem(
    val name: String,
    val imageUrl: String,
    val id: Int,
    val types: List<String> = emptyList(),
    val capturedAt: Long = 0L
)

data class PokemonDetails(
    val name: String,
    val id: Int,
    val imageUrl: String,
    val types: List<String>,
    val description: String,
    val evolvesFromName: String?,
    val evolvesFromImageUrl: String? = "",
    val isCaptured: Boolean = false
)