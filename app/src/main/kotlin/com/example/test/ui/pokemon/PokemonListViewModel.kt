package com.example.test.ui.pokemon

import androidx.compose.runtime.Immutable
import androidx.lifecycle.viewModelScope
import com.example.test.domain.CapturePokemonUseCase
import com.example.test.domain.GetCapturedPokemons
import com.humanforce.features.base.MviViewModel
import com.example.test.domain.GetPokemonListUseCase
import com.example.test.domain.ReleasePokemonUseCase
import com.example.test.domain.model.PokemonItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface PokemonListEvent {
    data class LoadPokemon(val limit: Int? = null) : PokemonListEvent
    data class CapturePokemon(val pokemon: PokemonItem) : PokemonListEvent
    data class ReleasePokemon(val pokemon: PokemonItem) : PokemonListEvent
}

@Immutable
data class PokemonListViewState(
    val pokemonList: List<PokemonItem> = emptyList(),
    val capturedList: List<PokemonItem> = emptyList(),
    val groupedPokemonByTypes: Map<String, List<PokemonItem>> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val totalPokemonCount: Int = 0,
    val currentLoadedCount: Int = 0

)

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val capturePokemonUseCase: CapturePokemonUseCase,
    private val getCapturedPokemonUseCase: GetCapturedPokemons,
    private val releasePokemonUseCase: ReleasePokemonUseCase
) : MviViewModel<PokemonListViewState, Any>(PokemonListViewState()) {

    init {
        // Load with default limit (151) because use case's limit parameter will be null
        loadCapturedPokemon()
        loadPokemon(limit = 151)
    }

    fun process(event: PokemonListEvent) {
        when (event) {
            is PokemonListEvent.LoadPokemon -> loadPokemon(event.limit)
            is PokemonListEvent.CapturePokemon -> {
                capturePokemon(event.pokemon)
            }
            is PokemonListEvent.ReleasePokemon -> {
                releasePokemon(event.pokemon)
            }
        }
    }

    fun loadPokemon(limit: Int? = null) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getPokemonListUseCase(limit = limit).onSuccess { items ->
                val groupedItems = items
                    .groupBy { it.types.firstOrNull() ?: "Unknown" }
                    .mapValues { it.value.sortedBy { pokemon -> pokemon.name } }
                    .toSortedMap()
                _state.update { state ->
                    state.copy(
                        isLoading = false,
                        pokemonList = items,
                        groupedPokemonByTypes = groupedItems
                    )
                }
            }.onFailure { exception ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to load Pokemon"
                    )
                }
            }
        }
    }

    fun loadCapturedPokemon() {
        viewModelScope.launch {
            getCapturedPokemonUseCase.invoke().onSuccess { capturedList ->
                _state.update { state ->
                    state.copy(capturedList = capturedList)
                }
            }.onFailure { exception ->
                _state.update {
                    it.copy(error = exception.message ?: "Failed to load captured Pokemon")
                }
            }
        }
    }

    fun capturePokemon(pokemon: PokemonItem) {
        viewModelScope.launch {
            capturePokemonUseCase(pokemon).onSuccess {
                _state.update { state ->
                    state.copy(capturedList = it)
                }
            }.onFailure { exception ->
                _state.update { it.copy(error = exception.message ?: "Failed to capture Pokemon") }
            }
        }
    }

    fun releasePokemon(pokemon: PokemonItem) {
        viewModelScope.launch {
            releasePokemonUseCase(pokemon).onSuccess {
                _state.update { state ->
                    state.copy(capturedList = it)
                }
            }.onFailure { exception ->
                _state.update { it.copy(error = exception.message ?: "Failed to release Pokemon") }
            }
        }
    }

}
