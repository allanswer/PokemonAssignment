package com.example.test.domain

import com.example.test.data.repository.PokemonRepository
import com.example.test.domain.model.PokemonDetails
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetPokemonDetailsUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend operator fun invoke(name: String): Result<PokemonDetails> {
        return try {
            val pokemonDetails = pokemonRepository.getPokemonDetail(name = name)
            Result.success(pokemonDetails)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
