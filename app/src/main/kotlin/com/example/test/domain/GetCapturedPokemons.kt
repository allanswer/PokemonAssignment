package com.example.test.domain

import com.example.test.data.repository.PokemonRepository
import com.example.test.domain.model.PokemonItem
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetCapturedPokemons @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend operator fun invoke(): Result<List<PokemonItem>> {
        return try {
            val capturedPokemons = pokemonRepository.getCapturedPokemon()
            Result.success(capturedPokemons)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
