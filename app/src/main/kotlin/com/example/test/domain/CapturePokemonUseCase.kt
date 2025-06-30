package com.example.test.domain

import com.example.test.data.repository.PokemonRepository
import com.example.test.domain.model.PokemonItem
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class CapturePokemonUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend operator fun invoke(pokemonItem: PokemonItem): Result<List<PokemonItem>> {
        return try {
            val capturedPokemons = pokemonRepository.capturePokemon(pokemonItem = pokemonItem)
            // Timber.d("Pokemon captured: ${pokemonItem.name}")
            Result.success(capturedPokemons)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
