package com.example.test.navigation

sealed class Screen(val route: String) {
    object PokemonList : Screen("pokemonList")
    object PokemonDetail : Screen("pokemonDetail/{pokemonName}") {
        fun createRoute(pokemonName: String) = "pokemonDetail/$pokemonName"
    }
}
