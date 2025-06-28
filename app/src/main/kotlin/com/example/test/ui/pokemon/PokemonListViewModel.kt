package com.example.test.ui.pokemon

import androidx.lifecycle.viewModelScope
import com.humanforce.features.base.MviViewModel
import com.example.test.domain.GetPokemonListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PokemonListViewState(
    val pokemonNames: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase
) : MviViewModel<PokemonListViewState, Any>(PokemonListViewState()) {

    init {
        // Load with default limit (151) because use case's limit parameter will be null
        loadPokemon(limit = 151)
    }

    fun loadPokemon(limit: Int? = null) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getPokemonListUseCase(limit = limit).onSuccess { items ->
                _state.update { state ->
                    state.copy(
                        isLoading = false,
                        pokemonNames = items.map { it.name }
                    )
                }
            }.onFailure { exception ->
                _state.update{
                    it.copy(
                        isLoading = false,
                        error = exception.message ?: "Failed to load Pokemon"
                    )
                }
            }
        }
    }
}
