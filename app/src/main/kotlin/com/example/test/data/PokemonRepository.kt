package com.example.test.data

import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepository @Inject constructor(
    private val retrofit: Retrofit
) {
    private val pokemonApi: PokemonApi by lazy {
        retrofit.create(PokemonApi::class.java)
    }

    suspend fun getPokemonList(limit: Int?): List<PokemonListItem> {
        // TODO: Handle pagination if needed
        return pokemonApi.getPokemonList(limit = limit).results
    }
}


