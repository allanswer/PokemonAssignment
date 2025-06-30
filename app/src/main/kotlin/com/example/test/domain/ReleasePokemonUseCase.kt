package com.example.test.domain

import com.example.test.data.repository.PokemonRepository
import com.example.test.domain.model.PokemonItem
import jakarta.inject.Inject

class ReleasePokemonUseCase @Inject constructor(
    private val repository: PokemonRepository
) {
    suspend operator fun invoke(id: Int): Result<List<PokemonItem>> {
        return runCatching {
            repository.releasePokemon(id)
        }
    }
}
