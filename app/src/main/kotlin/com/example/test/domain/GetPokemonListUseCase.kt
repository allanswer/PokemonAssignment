package com.example.test.domain

import com.example.test.data.PokemonListItem // Or a domain-specific model if you add mapping
import com.example.test.data.PokemonRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetPokemonListUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend operator fun invoke(limit: Int?): Result<List<PokemonListItem>> {
        return try {
            val pokemonList = pokemonRepository.getPokemonList(limit = limit)
            Result.success(pokemonList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
