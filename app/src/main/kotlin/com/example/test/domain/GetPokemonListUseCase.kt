package com.example.test.domain

import com.example.test.data.repository.PokemonRepository
import com.example.test.domain.model.PokemonItem
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetPokemonListUseCase @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    suspend operator fun invoke(limit: Int?): Result<List<PokemonItem>> {
        return try {
            val pokemonList = pokemonRepository.getPokemonList(limit = limit).sortedBy { it.name }
            Result.success(pokemonList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
