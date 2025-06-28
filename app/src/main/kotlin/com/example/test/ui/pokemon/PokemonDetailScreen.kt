package com.example.test.ui.pokemon

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PokemonDetailScreen(pokemonName: String?) {
    Text(text = "Details for $pokemonName")
}
