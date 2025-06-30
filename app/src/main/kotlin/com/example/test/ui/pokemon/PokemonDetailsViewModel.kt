package com.example.test.ui.pokemon

import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.test.data.model.PokemonListItem
import com.example.test.domain.GetPokemonDetailsUseCase
import com.humanforce.features.base.MviViewModel
import com.example.test.domain.GetPokemonListUseCase
import com.example.test.domain.model.PokemonDetails
import com.example.test.domain.model.PokemonItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@Immutable
data class PokemonDetailsViewState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val pokemonName: String? = null,
    val pokemonDetails: PokemonDetails? = null,
    val evolvesFromPokemon: PokemonDetails? = null,
)

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    private val getPokemonDetailsUseCase: GetPokemonDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : MviViewModel<PokemonDetailsViewState, Any>(
    PokemonDetailsViewState(
        pokemonName = savedStateHandle["pokemonName"]
    )
) {

    init {
        state.value.pokemonName?.let { name ->
            loadPokemonDetails()
        } ?: run {
            _state.update { it.copy(error = "Pokemon name is not provided") }
        }
    }

    fun loadPokemonDetails() {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            getPokemonDetailsUseCase.invoke(name = state.value.pokemonName ?: "")
                .onSuccess { pokemonDetails ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            pokemonDetails = pokemonDetails,
                        )
                    }
                }
                .onFailure { exception ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = exception.message ?: "Failed to load Pokemon details"
                        )
                    }
                }

        }
    }
}
