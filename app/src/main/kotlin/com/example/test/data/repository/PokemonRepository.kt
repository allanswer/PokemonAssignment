package com.example.test.data.repository

import com.example.test.data.local.database.PokemonDao
import com.example.test.data.local.database.entities.CapturedPokemonEntity
import com.example.test.data.local.database.entities.PokemonEntity
import com.example.test.data.local.database.entities.toPokemonDetails
import com.example.test.data.local.database.entities.toPokemonItem
import com.example.test.data.remote.PokemonApi
import com.example.test.domain.model.PokemonDetails
import com.example.test.domain.model.PokemonItem
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PokemonRepository @Inject constructor(
    private val retrofit: Retrofit,
    private val pokemonDao: PokemonDao,
) {
    private val pokemonApi: PokemonApi by lazy {
        retrofit.create(PokemonApi::class.java)
    }

    suspend fun getPokemonList(limit: Int?): List<PokemonItem> {
        val cache = pokemonDao.getAll()
        if (cache.isNotEmpty()) return cache.map { it.toPokemonItem() }

        val apiResult = pokemonApi.getPokemonList(limit).results
        val fullDetails = coroutineScope {
            apiResult.map { item ->
                async {
                    val detail = pokemonApi.getPokemonDetail(item.name)
                    val species = pokemonApi.getPokemonSpecies(item.name)
                    PokemonEntity(
                        name = detail.name,
                        imageUrl = detail.sprites.other?.officialArtwork?.frontDefault.orEmpty(),
                        types = detail.types.map { it.type.name },
                        description = species.flavorTextEntries.find { it.language.name == "en" }?.flavorText ?: "",
                        evolvesFrom = species.evolvesFromSpecies?.name,
                        id = detail.id
                    )
                }
            }.map { it.await() }
        }

        pokemonDao.insertAll(fullDetails)
        return fullDetails.map { it.toPokemonItem() }
    }

    suspend fun getPokemonDetail(name: String): PokemonDetails {
        val cachedPokemon = pokemonDao.getByName(name)
        if (cachedPokemon != null) {
            val evolvesFromImageUrl = cachedPokemon.evolvesFrom?.let { evolvesFromName ->
                pokemonDao.getByName(evolvesFromName)?.imageUrl
            }

            return cachedPokemon.toPokemonDetails().copy(
                evolvesFromImageUrl = evolvesFromImageUrl
            )
        }

        val detailResponse = pokemonApi.getPokemonDetail(name)
        val speciesResponse = pokemonApi.getPokemonSpecies(name)

        val evolvesFrom = speciesResponse.evolvesFromSpecies?.name
        val entity = PokemonEntity(
            name = detailResponse.name,
            imageUrl = detailResponse.sprites.other?.officialArtwork?.frontDefault.orEmpty(),
            types = detailResponse.types.map { it.type.name },
            description = speciesResponse.flavorTextEntries.find { it.language.name == "en" }?.flavorText ?: "",
            evolvesFrom = speciesResponse.evolvesFromSpecies?.name,
            id = detailResponse.id,
        )
        pokemonDao.insertAll(listOf(entity))



        val evolvesFromEntity = evolvesFrom?.let { pokemonDao.getByName(it) }

        return entity.toPokemonDetails().copy(
            evolvesFromImageUrl = evolvesFromEntity?.imageUrl
        )
    }

    suspend fun capturePokemon(pokemonItem: PokemonItem): List<PokemonItem> {
        val entity = CapturedPokemonEntity(
            name = pokemonItem.name,
            imageUrl = pokemonItem.imageUrl,
        )
        pokemonDao.insertCapture(entity)
        val allCaptured = pokemonDao.getAllCaptured().map { it.toPokemonItem() }
        return allCaptured
    }

    suspend fun getCapturedPokemon(): List<PokemonItem> {
        return pokemonDao.getAllCaptured().map { it.toPokemonItem() }
    }

    suspend fun releasePokemon(id: Int): List<PokemonItem> {
        pokemonDao.deleteCapturedPokemon(id)
        return pokemonDao.getAllCaptured().map { it.toPokemonItem() }
    }
}
